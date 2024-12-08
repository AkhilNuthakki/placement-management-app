package com.project.placement_management_app.service.placementAuthRequest;

import com.project.placement_management_app.dto.request.FilterPlacementAuthRequestDto;
import com.project.placement_management_app.dto.request.PlacementAuthRequestDto;
import com.project.placement_management_app.exception.EmailMessagingException;
import com.project.placement_management_app.exception.PreConditionFailedException;
import com.project.placement_management_app.exception.ResourceAlreadyExistsException;
import com.project.placement_management_app.exception.ResourceNotFoundException;
import com.project.placement_management_app.model.*;
import com.project.placement_management_app.repository.PlacementAuthorizationRequestRepository;
import com.project.placement_management_app.service.email.EmailService;
import com.project.placement_management_app.service.user.UserService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlacementAuthRequestServiceImpl implements PlacementAuthRequestService{

    private static final Logger LOG = LoggerFactory.getLogger(PlacementAuthRequestServiceImpl.class);
    @Autowired
    private PlacementAuthorizationRequestRepository placementAuthorizationRequestRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Value("${email.notifications.enable}")
    private boolean isEmailNotificationEnable;

    public List<PlacementAuthorizationRequest> getPlacementAuthRequestByUserId(String userId){

        LOG.info("Get user details by provided userId");
        User user = userService.getUserById(userId);
        if (user == null){
            LOG.error("user is Invalid");
            throw new PreConditionFailedException("user is Invalid");
        }

        List<PlacementAuthorizationRequest> placementAuthorizationRequestList = new ArrayList<PlacementAuthorizationRequest>();
        if(user.getUserRole() == Role.STUDENT){
            LOG.info("Fetching the requests of the student");
            placementAuthorizationRequestList =
                    placementAuthorizationRequestRepository.findPlacementAuthRequestsByUserId(userId);
        } else if (user.getUserRole() == Role.TUTOR){
            LOG.info("Fetching the submitted requests that belongs to the school of tutor");
            placementAuthorizationRequestList =
                    placementAuthorizationRequestRepository.findPlacementAuthRequestsBySchoolAndStatus(user.getSchool(),
                            PlacementAuthorizationRequestStatus.SUBMITTED.toString());
        }else if (user.getUserRole() == Role.PROVIDER){
            LOG.info("Fetching the submitted requests that belongs to the provider");
            placementAuthorizationRequestList =
                    placementAuthorizationRequestRepository.findPlacementAuthRequestsByProviderContactEmailIdAndStatus(user.getEmailId(),
                            PlacementAuthorizationRequestStatus.SUBMITTED.toString());
        }

        if(placementAuthorizationRequestList.isEmpty()){
            LOG.error("No placement authorization requests found");
            throw new ResourceNotFoundException("No placement authorization requests found");
        }

        return placementAuthorizationRequestList;
    }

    public PlacementAuthorizationRequest getPlacementAuthRequestById(String requestId){

        PlacementAuthorizationRequest placementAuthorizationRequest = placementAuthorizationRequestRepository
                .findPlacementAuthRequestById(requestId);

        if(placementAuthorizationRequest == null){
            LOG.error("No placement authorization requests found");
            throw new ResourceNotFoundException("No placement authorization requests found");
        }
        return placementAuthorizationRequest;
    }

    public PlacementAuthorizationRequest submitPlacementAuthRequest(PlacementAuthRequestDto placementAuthRequestDto){

        PlacementAuthorizationRequest placementAuthorizationRequest = PlacementAuthorizationRequest.buildPlacementAuthorizationRequestWith()
                .userId(placementAuthRequestDto.getUserId())
                .student(placementAuthRequestDto.getStudent())
                .placementProvider(placementAuthRequestDto.getPlacementProvider())
                .placementRole(placementAuthRequestDto.getPlacementRole())
                .workFactor(placementAuthRequestDto.getWorkFactor())
                .travelFactor(placementAuthRequestDto.getTravelFactor())
                .locationAndRegionFactor(placementAuthRequestDto.getLocationAndRegionFactor())
                .healthFactor(placementAuthRequestDto.getHealthFactor())
                .personalFactor(placementAuthRequestDto.getPersonalFactor())
                .policiesAndInsuranceFactor(placementAuthRequestDto.getPoliciesAndInsuranceFactor())
                .requestSubmissionStatus(PlacementAuthorizationRequestStatus.SUBMITTED)
                .requestSubmissionDate(new Date())
                .providerRegistrationStatus(PlacementAuthorizationRequestStatus.PENDING)
                .providerFormSubmissionStatus(PlacementAuthorizationRequestStatus.PENDING)
                .tutorAssessmentSubmissionStatus(PlacementAuthorizationRequestStatus.PENDING)
                .placementAuthorizationRequestStatus(PlacementAuthorizationRequestStatus.PENDING)
                .build();

        if(placementAuthRequestDto.getId() != null && !placementAuthRequestDto.getId().isEmpty()){
            LOG.info("Checking if the Placement Authorization Request is already available");
            PlacementAuthorizationRequest placementAuthorizationRequestDB = placementAuthorizationRequestRepository.findPlacementAuthRequestById(placementAuthRequestDto.getId());

            if(placementAuthorizationRequestDB != null){
                if(placementAuthorizationRequestDB.getRequestSubmissionStatus() == PlacementAuthorizationRequestStatus.SUBMITTED){
                    LOG.error("Placement Authorization Request is already in submitted state");
                    throw new ResourceAlreadyExistsException("Placement Authorization Request is already in submitted state");
                }
                placementAuthorizationRequest.setId(placementAuthorizationRequestDB.getId());
            }
        }
        placementAuthorizationRequestRepository.save(placementAuthorizationRequest);

        //Sending Email Notification
        if(isEmailNotificationEnable){
            List<User> userList = userService.getUsersBySchoolAndRole(placementAuthorizationRequest.getStudent().getSchool(),Role.TUTOR);

            if(!userList.isEmpty()){
                EmailTemplate emailTemplate = emailService.getEmailTemplate("PlacementRequestSubmittedNotification");

                Map<String,String> emailParameters = new HashMap<String,String>();
                emailParameters.put("studentName", placementAuthorizationRequest.getStudent().getFirstName()
                        + ' ' + placementAuthorizationRequest.getStudent().getLastName());

                String emailContent = emailService.createEmailContext(emailTemplate.getEmailContent(),emailParameters);
                String subject = emailService.createSubject(emailTemplate.getSubject(),emailParameters);

                String[] toEmails = userList.stream().map(user -> user.getEmailId()).collect(Collectors.toList()).toArray(new String[userList.size()]);

                try {
                    emailService.sendEmail(toEmails,subject,emailContent,emailTemplate.isContentHTML(),new String[]{});
                } catch (MessagingException e) {
                    throw new EmailMessagingException("Sending email notification is failed");
                }
            }
        }

        return placementAuthorizationRequest;
    }

    public PlacementAuthorizationRequest savePlacementAuthRequest(PlacementAuthRequestDto placementAuthRequestDto){

        PlacementAuthorizationRequest placementAuthorizationRequest = PlacementAuthorizationRequest.buildPlacementAuthorizationRequestWith()
                .userId(placementAuthRequestDto.getUserId())
                .student(placementAuthRequestDto.getStudent())
                .placementProvider(placementAuthRequestDto.getPlacementProvider())
                .placementRole(placementAuthRequestDto.getPlacementRole())
                .workFactor(placementAuthRequestDto.getWorkFactor())
                .travelFactor(placementAuthRequestDto.getTravelFactor())
                .locationAndRegionFactor(placementAuthRequestDto.getLocationAndRegionFactor())
                .healthFactor(placementAuthRequestDto.getHealthFactor())
                .personalFactor(placementAuthRequestDto.getPersonalFactor())
                .policiesAndInsuranceFactor(placementAuthRequestDto.getPoliciesAndInsuranceFactor())
                .requestSubmissionStatus(PlacementAuthorizationRequestStatus.DRAFT)
                .requestSubmissionDate(new Date())
                .build();

        if(placementAuthRequestDto.getId() != null && !placementAuthRequestDto.getId().isEmpty()){
            LOG.info("Checking if the Placement Authorization Request is already available");
            PlacementAuthorizationRequest placementAuthorizationRequestDB = placementAuthorizationRequestRepository.findPlacementAuthRequestById(placementAuthRequestDto.getId());

            if(placementAuthorizationRequestDB != null){
                if(placementAuthorizationRequestDB.getRequestSubmissionStatus() == PlacementAuthorizationRequestStatus.SUBMITTED){
                    LOG.error("Placement Authorization Request is already in submitted state");
                    throw new ResourceAlreadyExistsException("Placement Authorization Request is already in submitted state");
                }
                placementAuthorizationRequest.setId(placementAuthorizationRequestDB.getId());
            }
        }

        placementAuthorizationRequestRepository.save(placementAuthorizationRequest);

        return placementAuthorizationRequest;
    }

    public PlacementAuthorizationRequest updateAssessmentDetails(String requestId,
                                        String assessmentId,
                                        PlacementAuthorizationRequestStatus tutorAssessmentSubmissionStatus,
                                        PlacementAuthorizationRequestStatus placementAuthorizationRequestStatus) {

        LOG.info("Fetching the placement authorization request for the provided request Id");
        PlacementAuthorizationRequest placementAuthorizationRequest = getPlacementAuthRequestById(requestId);

        if(placementAuthorizationRequest.getPlacementAuthorizationRequestStatus() == PlacementAuthorizationRequestStatus.AUTHORIZED){
            LOG.error("Request is already authorized");
            throw new PreConditionFailedException("Request is already authorized");
        }else if(placementAuthorizationRequest.getProviderFormSubmissionStatus() != PlacementAuthorizationRequestStatus.SUBMITTED){
            LOG.error("Request can't be approved unless provider form is submitted by provider");
            throw new PreConditionFailedException("Request can't be approved unless provider form is submitted by provider");
        } else if(placementAuthorizationRequest.getRequestSubmissionStatus() != PlacementAuthorizationRequestStatus.SUBMITTED){
            LOG.error("Request can't be approved unless it is submitted by student");
            throw new PreConditionFailedException("Request can't be approved unless it is submitted by student");
        }

        placementAuthorizationRequest.setPlacementAuthorizationRequestStatus(placementAuthorizationRequestStatus);
        if(placementAuthorizationRequestStatus != PlacementAuthorizationRequestStatus.PENDING){
            placementAuthorizationRequest.setAuthorizationRequestStatusDate(new Date());
        }
        placementAuthorizationRequest.setTutorAssessmentSubmissionStatus(tutorAssessmentSubmissionStatus);
        placementAuthorizationRequest.setTutorAssessmentSubmissionDate(new Date());
        placementAuthorizationRequest.setTutorAssessmentId(assessmentId);

        return placementAuthorizationRequestRepository.save(placementAuthorizationRequest);
    }

    public PlacementAuthorizationRequest updateProviderFormDetails(String requestId, String providerFormId, PlacementAuthorizationRequestStatus status){

        PlacementAuthorizationRequest placementAuthorizationRequest = getPlacementAuthRequestById(requestId);

        placementAuthorizationRequest.setProviderFormId(providerFormId);
        placementAuthorizationRequest.setProviderFormSubmissionStatus(status);
        placementAuthorizationRequest.setProviderFormSubmissionDate(new Date());

        return placementAuthorizationRequestRepository.save(placementAuthorizationRequest);
    }

    public PlacementAuthorizationRequest updateProviderRegistrationDetails(String requestId){
        PlacementAuthorizationRequest placementAuthorizationRequest = getPlacementAuthRequestById(requestId);

        placementAuthorizationRequest.setProviderRegistrationStatus(PlacementAuthorizationRequestStatus.REGISTERED);
        placementAuthorizationRequest.setProviderNotifiedDate(new Date());

        return placementAuthorizationRequestRepository.save(placementAuthorizationRequest);
    }

    public List<String> getDistinctPlacementProviderNames() {
        return placementAuthorizationRequestRepository.findDistinctPlacementProviderName();
    }

    public List<RequestCountGroupByStatus> getRequestsFromProviderGroupByStatus(String providerName){
        return placementAuthorizationRequestRepository.findRequestsFromProviderGroupByStatus(providerName);
    }

    public List<RequestCountGroupByStatus> getRequestsFromSchoolGroupByStatus(String schoolName){
        return placementAuthorizationRequestRepository.findRequestsFromSchoolGroupByStatus(schoolName);
    }

    public List<RequestCountGroupByStatusAndCourse> getRequestsFromSchoolGroupByStatusAndCourse(String schoolName){
        return placementAuthorizationRequestRepository.findRequestsFromSchoolGroupByStatusAndCourse(schoolName);
    }

    public List<RequestCountGroupByStatusAndProviderName> getRequestsFromSchoolGroupByStatusAndProviderName(String schoolName){
        return placementAuthorizationRequestRepository.findRequestsFromSchoolGroupByStatusAndProviderName(schoolName);
    }

    public List<PlacementAuthorizationRequest> getFilteredPlacementAuthRequests(FilterPlacementAuthRequestDto filterPlacementAuthRequestDto){
        Query query = new Query();

        if(filterPlacementAuthRequestDto.getProviderName() != null && !filterPlacementAuthRequestDto.getProviderName().isBlank()){
            query.addCriteria(
                    Criteria.where("placementProvider.name").is(filterPlacementAuthRequestDto.getProviderName()));
        }
        if(filterPlacementAuthRequestDto.getStudentCourse() != null && !filterPlacementAuthRequestDto.getStudentCourse().isBlank()){
            query.addCriteria(
                    Criteria.where("student.course").is(filterPlacementAuthRequestDto.getStudentCourse()));
        }
        if(filterPlacementAuthRequestDto.getPlacementAuthRequestStatus() != null && !filterPlacementAuthRequestDto.getPlacementAuthRequestStatus().isBlank()){
            query.addCriteria(Criteria.where("placementAuthorizationRequestStatus")
                            .is(PlacementAuthorizationRequestStatus.valueOf(filterPlacementAuthRequestDto.getPlacementAuthRequestStatus().toUpperCase())));
        }

        List<PlacementAuthorizationRequest> placementAuthorizationRequestList
                = mongoTemplate.find(query, PlacementAuthorizationRequest.class);

        if(placementAuthorizationRequestList.isEmpty()){
            LOG.error("No placement authorization requests found based on given filter details");
            throw new ResourceNotFoundException("No placement authorization requests found based on given filter details");
        }

        return placementAuthorizationRequestList;
    }

}
