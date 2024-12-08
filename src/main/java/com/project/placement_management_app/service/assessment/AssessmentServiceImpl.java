package com.project.placement_management_app.service.assessment;

import com.project.placement_management_app.dto.request.AssessmentRequestDto;
import com.project.placement_management_app.dto.response.AutoFillAssessmentFormResponseDto;
import com.project.placement_management_app.dto.response.MatchInfoAssessmentResponseDto;
import com.project.placement_management_app.exception.EmailMessagingException;
import com.project.placement_management_app.exception.ResourceAlreadyExistsException;
import com.project.placement_management_app.exception.ResourceNotFoundException;
import com.project.placement_management_app.model.*;
import com.project.placement_management_app.repository.AssessmentRepository;
import com.project.placement_management_app.service.email.EmailService;
import com.project.placement_management_app.service.placement.PlacementService;
import com.project.placement_management_app.service.placementAuthRequest.PlacementAuthRequestService;
import com.project.placement_management_app.service.provider.ProviderService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AssessmentServiceImpl implements AssessmentService{

    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private PlacementAuthRequestService placementAuthRequestService;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private PlacementService placementService;
    @Autowired
    private EmailService emailService;
    @Value("${email.notifications.enable}")
    private boolean isEmailNotificationEnable;

    private static final Logger LOG = LoggerFactory.getLogger(AssessmentServiceImpl.class);

    public PlacementAssessment saveAssessment(AssessmentRequestDto assessmentRequestDto){

        PlacementAssessment placementAssessment = PlacementAssessment.buildPlacementWith()
                .userId(assessmentRequestDto.getUserId())
                .studentName(assessmentRequestDto.getStudentName())
                .providerAssessment(assessmentRequestDto.getProviderAssessment())
                .infoMatchAssessment(assessmentRequestDto.getInfoMatchAssessment())
                .roleAssessment(assessmentRequestDto.getRoleAssessment())
                .workFactorAssessment(assessmentRequestDto.getWorkFactorAssessment())
                .travelFactorAssessment(assessmentRequestDto.getTravelFactorAssessment())
                .locationFactorAssessment(assessmentRequestDto.getLocationFactorAssessment())
                .healthFactorAssessment(assessmentRequestDto.getHealthFactorAssessment())
                .personalFactorAssessment(assessmentRequestDto.getPersonalFactorAssessment())
                .policiesAndInsuranceAssessment(assessmentRequestDto.getPoliciesAndInsuranceAssessment())
                .universityAccessSupportAssessment(assessmentRequestDto.getUniversityAccessSupportAssessment())
                .status(PlacementAuthorizationRequestStatus.DRAFT)
                .statusDate(new Date())
                .placementAuthorizationRequestStatus(PlacementAuthorizationRequestStatus.valueOf(assessmentRequestDto.getPlacementAuthorizationRequestStatus().toUpperCase()))
                .authorizationComments(assessmentRequestDto.getAuthorizationComments())
                .build();

        if(assessmentRequestDto.getId() != null && !assessmentRequestDto.getId().isEmpty()){
            LOG.info("Checking if the placement assessment is already available");
            PlacementAssessment placementAssessmentDB = assessmentRepository.findPlacementAssessmentById(assessmentRequestDto.getId());

            if(placementAssessmentDB != null){
                if(placementAssessmentDB.getStatus() == PlacementAuthorizationRequestStatus.SUBMITTED){
                    LOG.error("Placement assessment is already in submitted state");
                    throw new ResourceAlreadyExistsException("Placement assessment is already in submitted state");
                }
                placementAssessment.setId(placementAssessmentDB.getId());
            }
        }

        placementAssessment = assessmentRepository.save(placementAssessment);

        placementAuthRequestService.updateAssessmentDetails(assessmentRequestDto.getRequestId(),
                placementAssessment.getId(),
                placementAssessment.getStatus(),
                PlacementAuthorizationRequestStatus.PENDING);

        return placementAssessment;
    }

    public PlacementAssessment submitAssessment(AssessmentRequestDto assessmentRequestDto){

        PlacementAssessment placementAssessment = PlacementAssessment.buildPlacementWith()
                .userId(assessmentRequestDto.getUserId())
                .studentName(assessmentRequestDto.getStudentName())
                .providerAssessment(assessmentRequestDto.getProviderAssessment())
                .infoMatchAssessment(assessmentRequestDto.getInfoMatchAssessment())
                .roleAssessment(assessmentRequestDto.getRoleAssessment())
                .workFactorAssessment(assessmentRequestDto.getWorkFactorAssessment())
                .travelFactorAssessment(assessmentRequestDto.getTravelFactorAssessment())
                .locationFactorAssessment(assessmentRequestDto.getLocationFactorAssessment())
                .healthFactorAssessment(assessmentRequestDto.getHealthFactorAssessment())
                .personalFactorAssessment(assessmentRequestDto.getPersonalFactorAssessment())
                .policiesAndInsuranceAssessment(assessmentRequestDto.getPoliciesAndInsuranceAssessment())
                .universityAccessSupportAssessment(assessmentRequestDto.getUniversityAccessSupportAssessment())
                .status(PlacementAuthorizationRequestStatus.SUBMITTED)
                .statusDate(new Date())
                .placementAuthorizationRequestStatus(PlacementAuthorizationRequestStatus.valueOf(assessmentRequestDto.getPlacementAuthorizationRequestStatus().toUpperCase()))
                .authorizationRequestStatusDate(new Date())
                .authorizationComments(assessmentRequestDto.getAuthorizationComments())
                .build();

        if(assessmentRequestDto.getId() != null && !assessmentRequestDto.getId().isEmpty()){
            LOG.info("Checking if the placement assessment is already available");
            PlacementAssessment placementAssessmentDB = assessmentRepository.findPlacementAssessmentById(assessmentRequestDto.getId());

            if(placementAssessmentDB != null){
                if(placementAssessmentDB.getStatus() == PlacementAuthorizationRequestStatus.SUBMITTED
                        && placementAssessmentDB.getPlacementAuthorizationRequestStatus() != PlacementAuthorizationRequestStatus.ONHOLD){
                    LOG.error("Placement assessment is already in submitted state");
                    throw new ResourceAlreadyExistsException("Placement assessment is already in submitted state");
                }
                placementAssessment.setId(placementAssessmentDB.getId());
            }
        }

        placementAssessment = assessmentRepository.save(placementAssessment);

        PlacementAuthorizationRequest placementAuthorizationRequest = placementAuthRequestService.updateAssessmentDetails(assessmentRequestDto.getRequestId(),
                placementAssessment.getId(),
                placementAssessment.getStatus(),
                placementAssessment.getPlacementAuthorizationRequestStatus());

        if(placementAssessment.getPlacementAuthorizationRequestStatus() == PlacementAuthorizationRequestStatus.AUTHORIZED){
            placementService.addPlacement(placementAuthorizationRequest,
                    providerService.getProviderFromById(placementAuthorizationRequest.getProviderFormId()));
        }

        //Sending Email Notification
        if(isEmailNotificationEnable){

            Map<String,String> emailParameters = new HashMap<String,String>();
            emailParameters.put("studentName", placementAuthorizationRequest.getStudent().getFirstName()
                    + ' ' + placementAuthorizationRequest.getStudent().getLastName());
            emailParameters.put("tutorComments", placementAssessment.getAuthorizationComments());

            EmailTemplate emailTemplate = new EmailTemplate();
            if(placementAssessment.getPlacementAuthorizationRequestStatus() == PlacementAuthorizationRequestStatus.AUTHORIZED){
                 emailTemplate = emailService.getEmailTemplate("PlacementRequestAuthorizedNotification");
            }else if(placementAssessment.getPlacementAuthorizationRequestStatus() == PlacementAuthorizationRequestStatus.ONHOLD){
                 emailTemplate = emailService.getEmailTemplate("PlacementRequestOnHoldNotification");
            }else if(placementAssessment.getPlacementAuthorizationRequestStatus() == PlacementAuthorizationRequestStatus.REJECTED){
                 emailTemplate = emailService.getEmailTemplate("PlacementRequestRejectedNotification");
            }

            String emailContext = emailService.createEmailContext(emailTemplate.getEmailContent(), emailParameters);

            try {
                emailService.sendEmail(new String[] {placementAuthorizationRequest.getStudent().getEmailId()},
                        emailTemplate.getSubject(),
                        emailContext,
                        emailTemplate.isContentHTML(),
                        new String[] {});
            } catch (MessagingException e) {
                throw new EmailMessagingException("Sending email notification is failed");
            }
        }

        return placementAssessment;
    }

    public PlacementAssessment getAssessmentById(String assessmentId){
        PlacementAssessment placementAssessment = assessmentRepository
                .findPlacementAssessmentById(assessmentId);

        if(placementAssessment == null){
            LOG.error("No placement assessment found by the given id: " + assessmentId);
            throw new ResourceNotFoundException("No placement assessment found by the given id: " + assessmentId);
        }
        return placementAssessment;
    }

    public List<MatchInfoAssessmentResponseDto> matchInformationOfProviderFormAndRequestForm(String requestId) {

        PlacementAuthorizationRequest placementAuthorizationRequest =
                placementAuthRequestService.getPlacementAuthRequestById(requestId);

        ProviderForm providerForm =
                providerService.getProviderFromById(placementAuthorizationRequest.getProviderFormId());

        List<MatchInfoAssessmentResponseDto> matchInfoAssessmentResponseDtoList = new ArrayList<>();

        //Match StudentName Field
        MatchInfoAssessmentResponseDto matchStudentNameAssessmentResponseDto =
                MatchInfoAssessmentResponseDto.buildMatchInfoAssessmentResponseDtoWith().build();
        matchStudentNameAssessmentResponseDto.setFieldName("Student Name");
        if(providerForm.getStudentName().toLowerCase().contains(
                placementAuthorizationRequest.getStudent().getFirstName().toLowerCase()) &&
                providerForm.getStudentName().toLowerCase().contains(
                placementAuthorizationRequest.getStudent().getLastName().toLowerCase())){
            matchStudentNameAssessmentResponseDto.setMatched(true);
        }else {
            matchStudentNameAssessmentResponseDto.setMatched(false);
        }
        matchStudentNameAssessmentResponseDto.setResponseByStudent(
                placementAuthorizationRequest.getStudent().getFirstName() + " " +
                placementAuthorizationRequest.getStudent().getLastName());
        matchStudentNameAssessmentResponseDto.setResponseByProvider(providerForm.getStudentName());
        matchInfoAssessmentResponseDtoList.add(matchStudentNameAssessmentResponseDto);


        //Match ProviderName Field
        MatchInfoAssessmentResponseDto matchProviderNameAssessmentResponseDto =
                MatchInfoAssessmentResponseDto.buildMatchInfoAssessmentResponseDtoWith().build();
        matchProviderNameAssessmentResponseDto.setFieldName("Employer Name");
        if(placementAuthorizationRequest.getPlacementProvider().getName().toLowerCase().equals(providerForm.getPlacementProvider().getName().toLowerCase())){
            matchProviderNameAssessmentResponseDto.setMatched(true);
        }else {
            matchProviderNameAssessmentResponseDto.setMatched(false);
        }
        matchProviderNameAssessmentResponseDto.setResponseByStudent(placementAuthorizationRequest.getPlacementProvider().getName());
        matchProviderNameAssessmentResponseDto.setResponseByProvider(providerForm.getPlacementProvider().getName());
        matchInfoAssessmentResponseDtoList.add(matchProviderNameAssessmentResponseDto);


        //Match ProviderAddress Field
        MatchInfoAssessmentResponseDto matchProviderAddressAssessmentResponseDto =
                MatchInfoAssessmentResponseDto.buildMatchInfoAssessmentResponseDtoWith().build();
        matchProviderAddressAssessmentResponseDto.setFieldName("Employer Address");
        if(placementAuthorizationRequest.getPlacementProvider().getAddress().toLowerCase().equals(providerForm.getPlacementProvider().getAddress().toLowerCase()) &&
        placementAuthorizationRequest.getPlacementProvider().getPostcode().toLowerCase().equals(providerForm.getPlacementProvider().getPostcode().toLowerCase())){
            matchProviderAddressAssessmentResponseDto.setMatched(true);
        }else {
            matchProviderAddressAssessmentResponseDto.setMatched(false);
        }
        matchProviderAddressAssessmentResponseDto.setResponseByStudent(placementAuthorizationRequest.getPlacementProvider().getAddress()
                + ", " + placementAuthorizationRequest.getPlacementProvider().getPostcode());
        matchProviderAddressAssessmentResponseDto.setResponseByProvider(providerForm.getPlacementProvider().getAddress()
                + ", " + providerForm.getPlacementProvider().getPostcode());
        matchInfoAssessmentResponseDtoList.add(matchProviderAddressAssessmentResponseDto);


        return matchInfoAssessmentResponseDtoList;
    }

    public List<RequestCountGroupByStatus> checkForRedFlagsOnProvider(String providerName) {
        return placementAuthRequestService.getRequestsFromProviderGroupByStatus(providerName);
    }

    public AutoFillAssessmentFormResponseDto autoFillAssessmentDetails(String requestId){
        AutoFillAssessmentFormResponseDto autoFillAssessmentFormResponseDto = AutoFillAssessmentFormResponseDto.buildAutoFillAssessmentFormResponseDtoWith().build();

        PlacementAuthorizationRequest placementAuthorizationRequest =
                placementAuthRequestService.getPlacementAuthRequestById(requestId);

        ProviderForm providerForm =
                providerService.getProviderFromById(placementAuthorizationRequest.getProviderFormId());

        if(providerForm.getLocationAndRegionFactor().getProviderUKOrNonUK().equals("UK")){
            if(providerForm.getPoliciesAndInsuranceProviderFactor().getHasPublicLiabilityInsurance().equals("No")){
                autoFillAssessmentFormResponseDto.setHasPublicLiabilityInsurance(providerForm.getPoliciesAndInsuranceProviderFactor().getHasPublicLiabilityInsurance());
            }else{
                autoFillAssessmentFormResponseDto.setHasPublicLiabilityInsurance("Yes");
            }
            autoFillAssessmentFormResponseDto.setHasEmployerLiabilityInsurance(providerForm.getPoliciesAndInsuranceProviderFactor().getHasEmployerLiabilityInsurance());
            autoFillAssessmentFormResponseDto.setHasProfessionalIndemnityInsurance(providerForm.getPoliciesAndInsuranceProviderFactor().getHasProfessionalIndemnityInsurance());
        }else{
            autoFillAssessmentFormResponseDto.setHasPublicLiabilityInsurance(providerForm.getPoliciesAndInsuranceNonUKProviderFactor().getHasPublicLiabilityInsurance());
            autoFillAssessmentFormResponseDto.setHasEmployerLiabilityInsurance(providerForm.getPoliciesAndInsuranceNonUKProviderFactor().getHasEmployerLiabilityInsurance());
            autoFillAssessmentFormResponseDto.setHasProfessionalIndemnityInsurance("NA");
        }

        autoFillAssessmentFormResponseDto.setHasHealthAndSafetyPolicy(providerForm.getHealthAndSafetyProviderFactor().getHasHealthAndSafetyPolicy());

        if(providerForm.getUniversityAccessAndSupportFactor().getCanUndertakePlacementVisits().equals("Yes")){
            autoFillAssessmentFormResponseDto.setHasProviderHasAnyObligationsToVisits("No");
        }else{
            autoFillAssessmentFormResponseDto.setHasProviderHasAnyObligationsToVisits("Yes");
        }

        autoFillAssessmentFormResponseDto.setHasAnyIssuesRelatedToConfidentiality(providerForm.getUniversityAccessAndSupportFactor().getConfidentialityToBeTakenToAccount());

        return autoFillAssessmentFormResponseDto;
    }

}
