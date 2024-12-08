package com.project.placement_management_app.service.provider;

import com.project.placement_management_app.dto.request.ProviderFormRequestDto;
import com.project.placement_management_app.dto.request.RegisterProviderRequestDto;
import com.project.placement_management_app.exception.EmailMessagingException;
import com.project.placement_management_app.exception.ResourceAlreadyExistsException;
import com.project.placement_management_app.exception.ResourceNotFoundException;
import com.project.placement_management_app.model.*;
import com.project.placement_management_app.repository.ProviderFormRepository;
import com.project.placement_management_app.service.email.EmailService;
import com.project.placement_management_app.service.placementAuthRequest.PlacementAuthRequestService;
import com.project.placement_management_app.service.user.UserService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProviderServiceImpl implements ProviderService{

    @Autowired
    private ProviderFormRepository providerFormRepository;
    @Autowired
    private PlacementAuthRequestService placementAuthRequestService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Value("${frontend.url}")
    private String portalLink;
    @Value("${email.notifications.enable}")
    private boolean isEmailNotificationEnable;
    private static final Logger LOG = LoggerFactory.getLogger(ProviderServiceImpl.class);

    public ProviderForm saveProviderForm(ProviderFormRequestDto providerFormRequestDto){

        ProviderForm providerForm = ProviderForm.buildPlacementProviderFormWith().
                studentName(providerFormRequestDto.getStudentName()).
                placementProvider(providerFormRequestDto.getPlacementProvider()).
                placementRole(providerFormRequestDto.getPlacementRole()).
                workFactor(providerFormRequestDto.getWorkFactor()).
                travelFactor(providerFormRequestDto.getTravelFactor()).
                locationAndRegionFactor(providerFormRequestDto.getLocationAndRegionFactor()).
                healthFactor(providerFormRequestDto.getHealthFactor()).
                personalFactor(providerFormRequestDto.getPersonalFactor()).
                policiesAndInsuranceProviderFactor(providerFormRequestDto.getPoliciesAndInsuranceProviderFactor()).
                policiesAndInsuranceNonUKProviderFactor(providerFormRequestDto.getPoliciesAndInsuranceNonUKProviderFactor()).
                healthAndSafetyProviderFactor(providerFormRequestDto.getHealthAndSafetyProviderFactor()).
                universityAccessAndSupportFactor(providerFormRequestDto.getUniversityAccessAndSupportFactor()).
                status(PlacementAuthorizationRequestStatus.DRAFT).
                statusDate(new Date()).
                build();

        if(providerFormRequestDto.getId() != null && !providerFormRequestDto.getId().isEmpty()){
            LOG.info("Checking if the placement provider form is already available");
            ProviderForm providerFormDB = providerFormRepository.findProviderFormById(providerFormRequestDto.getId());

            if(providerFormDB != null){
                if(providerFormDB.getStatus() == PlacementAuthorizationRequestStatus.SUBMITTED){
                    LOG.error("Placement Provider Form is already in submitted state");
                    throw new ResourceAlreadyExistsException("Placement Authorization Request is already in submitted state");
                }
                providerForm.setId(providerFormDB.getId());
            }
        }

        providerForm = providerFormRepository.save(providerForm);

        placementAuthRequestService.updateProviderFormDetails(providerFormRequestDto.getRequestId(), providerForm.getId(), providerForm.getStatus());

        return providerForm;
    }

    public ProviderForm submitProviderForm(ProviderFormRequestDto providerFormRequestDto){
        ProviderForm providerForm = ProviderForm.buildPlacementProviderFormWith().
                studentName(providerFormRequestDto.getStudentName()).
                placementProvider(providerFormRequestDto.getPlacementProvider()).
                placementRole(providerFormRequestDto.getPlacementRole()).
                workFactor(providerFormRequestDto.getWorkFactor()).
                travelFactor(providerFormRequestDto.getTravelFactor()).
                locationAndRegionFactor(providerFormRequestDto.getLocationAndRegionFactor()).
                healthFactor(providerFormRequestDto.getHealthFactor()).
                personalFactor(providerFormRequestDto.getPersonalFactor()).
                policiesAndInsuranceProviderFactor(providerFormRequestDto.getPoliciesAndInsuranceProviderFactor()).
                policiesAndInsuranceNonUKProviderFactor(providerFormRequestDto.getPoliciesAndInsuranceNonUKProviderFactor()).
                healthAndSafetyProviderFactor(providerFormRequestDto.getHealthAndSafetyProviderFactor()).
                universityAccessAndSupportFactor(providerFormRequestDto.getUniversityAccessAndSupportFactor()).
                status(PlacementAuthorizationRequestStatus.SUBMITTED).
                statusDate(new Date()).
                build();

        if(providerFormRequestDto.getId() != null && !providerFormRequestDto.getId().isEmpty()){
            LOG.info("Checking if the placement provider form is already available");
            ProviderForm providerFormDB = providerFormRepository.findProviderFormById(providerFormRequestDto.getId());

            if(providerFormDB != null){
                if(providerFormDB.getStatus() == PlacementAuthorizationRequestStatus.SUBMITTED){
                    LOG.error("Placement Provider Form is already in submitted state");
                    throw new ResourceAlreadyExistsException("Placement Authorization Request is already in submitted state");
                }
                providerForm.setId(providerFormDB.getId());
            }
        }

        providerForm = providerFormRepository.save(providerForm);

        PlacementAuthorizationRequest placementAuthorizationRequest =
                placementAuthRequestService.updateProviderFormDetails(providerFormRequestDto.getRequestId(), providerForm.getId(), providerForm.getStatus());

        //Sending Email Notification
        if(isEmailNotificationEnable){
            List<User> userList = userService.getUsersBySchoolAndRole(placementAuthorizationRequest.getStudent().getSchool(),Role.TUTOR);

            if(!userList.isEmpty()){
                EmailTemplate emailTemplate = emailService.getEmailTemplate("ProviderFormSubmittedNotification");

                Map<String,String> emailParameters = new HashMap<String,String>();
                emailParameters.put("providerContactName", placementAuthorizationRequest.getPlacementProvider().getContactName());

                String subject = emailService.createSubject(emailTemplate.getSubject(),emailParameters);

                emailParameters.put("studentName", placementAuthorizationRequest.getStudent().getFirstName()
                        + ' ' + placementAuthorizationRequest.getStudent().getLastName());
                emailParameters.put("providerName", placementAuthorizationRequest.getPlacementProvider().getName());
                emailParameters.put("providerContactJobTitle", placementAuthorizationRequest.getPlacementProvider().getContactJobTitle());
                String emailContent = emailService.createEmailContext(emailTemplate.getEmailContent(),emailParameters);

                String[] toEmails = userList.stream().map(user -> user.getEmailId()).collect(Collectors.toList()).toArray(new String[userList.size()]);

                try {
                    emailService.sendEmail(toEmails,subject,emailContent,emailTemplate.isContentHTML(),new String[]{});
                } catch (MessagingException e) {
                    throw new EmailMessagingException("Sending email notification is failed");
                }
            }
        }

        return providerForm;
    }

    public ProviderForm getProviderFromById(String providerFormId){
        ProviderForm providerForm = providerFormRepository
                .findProviderFormById(providerFormId);

        if(providerForm == null){
            LOG.error("No provider form found by the given id: " + providerFormId);
            throw new ResourceNotFoundException("No provider form found by the given id: " + providerFormId);
        }
        return providerForm;
    }

    public User registerAndNotifyProvider(RegisterProviderRequestDto registerProviderRequestDto) {

        User user = userService.getUserByEmailId(registerProviderRequestDto.getUserEmailId());

        if(user == null){
            user = userService.registerProvider(registerProviderRequestDto.getUserEmailId(),
                    registerProviderRequestDto.getUserName());
        }

        if(user != null){
            PlacementAuthorizationRequest placementAuthorizationRequest =
                    placementAuthRequestService.updateProviderRegistrationDetails(registerProviderRequestDto.getAuthorizationRequestId());

            //Sending Email Notification
            if(isEmailNotificationEnable){
                EmailTemplate emailTemplate = emailService.getEmailTemplate("SubmitProviderFormNotification");

                Map<String, String> emailParameters = new HashMap<String, String>();
                emailParameters.put("providerName",user.getFirstName() + ' ' + user.getLastName());
                emailParameters.put("studentName", placementAuthorizationRequest.getStudent().getFirstName());
                emailParameters.put("portalLink",portalLink);
                emailParameters.put("providerEmail", user.getEmailId());
                emailParameters.put("providerPassword", user.getPassword());

                String emailContext = emailService.createEmailContext(emailTemplate.getEmailContent(),emailParameters);

                Map<String, String> subjectParameters = new HashMap<String, String>();
                subjectParameters.put("studentName", placementAuthorizationRequest.getStudent().getFirstName());

                String subject = emailService.createSubject(emailTemplate.getSubject(), subjectParameters);

                try {
                    emailService.sendEmail(new String[] {user.getEmailId()},
                            subject,
                            emailContext,
                            emailTemplate.isContentHTML(),
                            new String[]{});
                } catch (MessagingException e) {
                    throw new EmailMessagingException("Sending email notification is failed");
                }
            }
        }

        return user;
    }
}
