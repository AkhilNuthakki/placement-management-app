package com.project.placement_management_app.service;

import com.project.placement_management_app.dto.request.AssessmentRequestDto;
import com.project.placement_management_app.dto.response.AutoFillAssessmentFormResponseDto;
import com.project.placement_management_app.dto.response.MatchInfoAssessmentResponseDto;
import com.project.placement_management_app.exception.ResourceAlreadyExistsException;
import com.project.placement_management_app.model.*;
import com.project.placement_management_app.repository.AssessmentRepository;
import com.project.placement_management_app.service.assessment.AssessmentServiceImpl;
import com.project.placement_management_app.service.placement.PlacementService;
import com.project.placement_management_app.service.placementAuthRequest.PlacementAuthRequestService;
import com.project.placement_management_app.service.provider.ProviderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AssessmentServiceImplTest {
    @InjectMocks
    private AssessmentServiceImpl assessmentService;
    @Mock
    private AssessmentRepository assessmentRepository;
    @Mock
    private PlacementAuthRequestService placementAuthRequestService;
    @Mock
    private ProviderService providerService;
    @Mock
    private PlacementService placementService;

    private static final String providerFormId = UUID.randomUUID().toString();
    private static final String assessmentId = UUID.randomUUID().toString();
    private static final String requestId = UUID.randomUUID().toString();
    private static final String placementId = UUID.randomUUID().toString();

    private static final AssessmentRequestDto assessmentRequestDto = AssessmentRequestDto
            .buildAssessmentRequestDtoWith()
            .id(assessmentId)
            .requestId(requestId)
            .placementAuthorizationRequestStatus("AUTHORIZED")
            .build();

    private static final PlacementAssessment savedPlacementAssessment = PlacementAssessment.buildPlacementWith()
            .id(assessmentId)
            .status(PlacementAuthorizationRequestStatus.DRAFT)
            .placementAuthorizationRequestStatus(PlacementAuthorizationRequestStatus.AUTHORIZED)
            .build();

    private static final PlacementAssessment submittedPlacementAssessment = PlacementAssessment.buildPlacementWith()
            .id(assessmentId)
            .status(PlacementAuthorizationRequestStatus.SUBMITTED)
            .placementAuthorizationRequestStatus(PlacementAuthorizationRequestStatus.AUTHORIZED)
            .build();

    private static final PlacementAuthorizationRequest placementAuthorizationRequest =
            PlacementAuthorizationRequest.buildPlacementAuthorizationRequestWith()
                    .id(requestId)
                    .providerFormId(providerFormId)
                    .student(Student.buildStudentWith()
                            .firstName("firstName")
                            .lastName("lastName")
                            .build())
                    .placementProvider(PlacementProvider
                            .buildPlacementProviderWith()
                            .name("providerName")
                            .address("address")
                            .postcode("postcode")
                            .build())
                    .build();

    private static final ProviderForm providerForm = ProviderForm.buildPlacementProviderFormWith()
            .id(providerFormId)
            .studentName("firstName lastName")
            .placementProvider(PlacementProvider.
                    buildPlacementProviderWith()
                    .name("providerName")
                    .address("address")
                    .postcode("postcode")
                    .build())
            .locationAndRegionFactor(LocationAndRegionFactor
                    .buildLocationAndRegionFactorWith()
                    .providerUKOrNonUK("UK")
                    .build())
            .policiesAndInsuranceProviderFactor(PoliciesAndInsuranceProviderFactor
                    .buildPoliciesAndInsuranceProviderFactorWith()
                    .hasPublicLiabilityInsurance("No")
                    .hasEmployerLiabilityInsurance("Yes")
                    .hasProfessionalIndemnityInsurance("Yes")
                    .build())
            .healthAndSafetyProviderFactor(HealthAndSafetyProviderFactor
                    .buildHealthAndSafetyProviderFactorWith()
                    .hasHealthAndSafetyPolicy("Yes")
                    .build())
            .universityAccessAndSupportFactor(UniversityAccessAndSupportFactor
                    .buildUniversityAccessAndSupportFactorWith()
                    .canUndertakePlacementVisits("Yes")
                    .confidentialityToBeTakenToAccount("Yes")
                    .build())
            .status(PlacementAuthorizationRequestStatus.SUBMITTED)
            .build();

    private static final ProviderForm providerForm1 = ProviderForm.buildPlacementProviderFormWith()
            .id(providerFormId)
            .studentName("firstName Name")
            .placementProvider(PlacementProvider.
                    buildPlacementProviderWith()
                    .name("providerNameTest")
                    .address("addressTest")
                    .postcode("postcodeTest")
                    .build())
            .locationAndRegionFactor(LocationAndRegionFactor
                    .buildLocationAndRegionFactorWith()
                    .providerUKOrNonUK("Non-UK")
                    .build())
            .policiesAndInsuranceNonUKProviderFactor(PoliciesAndInsuranceNonUKProviderFactor
                    .buildPoliciesAndInsuranceNonUKProviderFactorWith()
                    .hasPublicLiabilityInsurance("Yes")
                    .hasEmployerLiabilityInsurance("Yes")
                    .build())
            .healthAndSafetyProviderFactor(HealthAndSafetyProviderFactor
                    .buildHealthAndSafetyProviderFactorWith()
                    .hasHealthAndSafetyPolicy("Yes")
                    .build())
            .universityAccessAndSupportFactor(UniversityAccessAndSupportFactor
                    .buildUniversityAccessAndSupportFactorWith()
                    .canUndertakePlacementVisits("No")
                    .confidentialityToBeTakenToAccount("Yes")
                    .build())
            .status(PlacementAuthorizationRequestStatus.SUBMITTED)
            .build();

    private static final Placement placement = Placement.buildPlacementWith()
            .id(placementId)
            .requestId(requestId)
            .startDate(new Date())
            .endDate(new Date())
            .placementProvider(PlacementProvider.buildPlacementProviderWith()
                    .contactName("contactName")
                    .contactEmail("TEST@test.com")
                    .telephone("0123456789")
                    .contactJobTitle("HR Delivery Manager").build())
            .build();



    @Test
    void givenSubmittedAssessmentDetailsToSaveThenReturnException(){
        when(assessmentRepository.findPlacementAssessmentById(assessmentRequestDto.getId())).thenReturn(submittedPlacementAssessment);
        assertThrows(ResourceAlreadyExistsException.class, () -> assessmentService.saveAssessment(assessmentRequestDto));
    }
    @Test
    void givenValidAssessmentDetailsThenSaveAssessmentForm(){
        when(assessmentRepository.findPlacementAssessmentById(assessmentRequestDto.getId())).thenReturn(savedPlacementAssessment);
        when(assessmentRepository.save(any())).thenReturn(savedPlacementAssessment);
        when(placementAuthRequestService.updateAssessmentDetails(requestId,
                assessmentId,
                savedPlacementAssessment.getStatus(),
                PlacementAuthorizationRequestStatus.PENDING)).thenReturn(placementAuthorizationRequest);
        PlacementAssessment placementAssessment = assessmentService.saveAssessment(assessmentRequestDto);
        assertEquals(placementAssessment.getId(), assessmentId);
    }

    @Test
    void givenSubmittedAssessmentDetailsToSubmitThenReturnException(){
        when(assessmentRepository.findPlacementAssessmentById(assessmentRequestDto.getId())).thenReturn(submittedPlacementAssessment);
        assertThrows(ResourceAlreadyExistsException.class, () -> assessmentService.submitAssessment(assessmentRequestDto));
    }

    @Test
    void givenValidAssessmentDetailsThenSubmitAssessmentForm(){
        when(assessmentRepository.findPlacementAssessmentById(assessmentRequestDto.getId())).thenReturn(savedPlacementAssessment);
        when(assessmentRepository.save(any())).thenReturn(submittedPlacementAssessment);
        when(placementAuthRequestService.updateAssessmentDetails(requestId,
                assessmentId,
                submittedPlacementAssessment.getStatus(),
                submittedPlacementAssessment.getPlacementAuthorizationRequestStatus())).thenReturn(placementAuthorizationRequest);
        when(placementService.addPlacement(placementAuthorizationRequest,providerForm)).thenReturn(placement);
        when(providerService.getProviderFromById(providerForm.getId())).thenReturn(providerForm);
        PlacementAssessment placementAssessment = assessmentService.submitAssessment(assessmentRequestDto);
        assertEquals(placementAssessment.getId(), assessmentId);
    }

    @Test
    void givenDetailsThenReturnMatchedInfoDetails(){
        when(providerService.getProviderFromById(providerForm.getId())).thenReturn(providerForm);
        when(placementAuthRequestService.getPlacementAuthRequestById(requestId)).thenReturn(placementAuthorizationRequest);
        List<MatchInfoAssessmentResponseDto> matchInfoAssessmentResponseDto = assessmentService.matchInformationOfProviderFormAndRequestForm(requestId);
        assertEquals(true, matchInfoAssessmentResponseDto.stream().filter(response -> response.getFieldName().equals("Student Name")).findFirst().map(response -> response.isMatched()).orElse(false));
        assertEquals(true, matchInfoAssessmentResponseDto.stream().filter(response -> response.getFieldName().equals("Employer Name")).findFirst().map(response -> response.isMatched()).orElse(false));
        assertEquals(true, matchInfoAssessmentResponseDto.stream().filter(response -> response.getFieldName().equals("Employer Address")).findFirst().map(response -> response.isMatched()).orElse(false));

    }

    @Test
    void givenDetailsThenReturnUnMatchedInfoDetails(){
        when(providerService.getProviderFromById(providerForm.getId())).thenReturn(providerForm1);
        when(placementAuthRequestService.getPlacementAuthRequestById(requestId)).thenReturn(placementAuthorizationRequest);
        List<MatchInfoAssessmentResponseDto> matchInfoAssessmentResponseDto = assessmentService.matchInformationOfProviderFormAndRequestForm(requestId);
        assertEquals(false, matchInfoAssessmentResponseDto.stream().filter(response -> response.getFieldName().equals("Student Name")).findFirst().map(response -> response.isMatched()).orElse(false));
        assertEquals(false, matchInfoAssessmentResponseDto.stream().filter(response -> response.getFieldName().equals("Employer Name")).findFirst().map(response -> response.isMatched()).orElse(false));
        assertEquals(false, matchInfoAssessmentResponseDto.stream().filter(response -> response.getFieldName().equals("Employer Address")).findFirst().map(response -> response.isMatched()).orElse(false));
    }

    @Test
    void givenDetailsThenReturnAutoFillAssessmentDetailsCase1(){
        when(providerService.getProviderFromById(providerForm.getId())).thenReturn(providerForm);
        when(placementAuthRequestService.getPlacementAuthRequestById(requestId)).thenReturn(placementAuthorizationRequest);
        AutoFillAssessmentFormResponseDto autoFillAssessmentFormResponseDto = assessmentService.autoFillAssessmentDetails(requestId);
        assertEquals(providerForm.getPoliciesAndInsuranceProviderFactor().getHasPublicLiabilityInsurance(), autoFillAssessmentFormResponseDto.getHasPublicLiabilityInsurance());
    }

    @Test
    void givenDetailsThenReturnAutoFillAssessmentDetailsCase2(){
        when(providerService.getProviderFromById(providerForm.getId())).thenReturn(providerForm1);
        when(placementAuthRequestService.getPlacementAuthRequestById(requestId)).thenReturn(placementAuthorizationRequest);
        AutoFillAssessmentFormResponseDto autoFillAssessmentFormResponseDto = assessmentService.autoFillAssessmentDetails(requestId);
        assertEquals(providerForm1.getPoliciesAndInsuranceNonUKProviderFactor().getHasPublicLiabilityInsurance(), autoFillAssessmentFormResponseDto.getHasPublicLiabilityInsurance());
    }



}
