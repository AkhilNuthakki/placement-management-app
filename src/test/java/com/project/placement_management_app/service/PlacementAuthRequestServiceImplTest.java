package com.project.placement_management_app.service;

import com.project.placement_management_app.dto.request.PlacementAuthRequestDto;
import com.project.placement_management_app.exception.PreConditionFailedException;
import com.project.placement_management_app.exception.ResourceAlreadyExistsException;
import com.project.placement_management_app.exception.ResourceNotFoundException;
import com.project.placement_management_app.model.*;
import com.project.placement_management_app.repository.PlacementAuthorizationRequestRepository;
import com.project.placement_management_app.service.placementAuthRequest.PlacementAuthRequestServiceImpl;
import com.project.placement_management_app.service.user.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlacementAuthRequestServiceImplTest {

    @InjectMocks
    private PlacementAuthRequestServiceImpl placementAuthRequestService;
    @Mock
    private PlacementAuthorizationRequestRepository placementAuthorizationRequestRepository;
    @Mock
    private UserService userService;
    private static final String providerFormId = UUID.randomUUID().toString();
    private static final String assessmentId = UUID.randomUUID().toString();
    private static final String requestId = UUID.randomUUID().toString();
    private static final String placementId = UUID.randomUUID().toString();
    private static final String userId = UUID.randomUUID().toString();
    private static final User studentUser = User.buildUserWith()
            .firstName("firstName")
            .lastName("lastName")
            .userRole(Role.STUDENT)
            .emailId("XXX15@student.le.ac.uk")
            .password("PASS1234")
            .build();

    private static final User tutorUser = User.buildUserWith()
            .firstName("firstName")
            .lastName("lastName")
            .userRole(Role.TUTOR)
            .emailId("XXX15@student.le.ac.uk")
            .password("PASS1234")
            .school("school")
            .build();

    private static final User providerUser = User.buildUserWith()
            .firstName("firstName")
            .lastName("lastName")
            .userRole(Role.PROVIDER)
            .emailId("XXX15@student.le.ac.uk")
            .password("PASS1234")
            .build();
    private static final PlacementAuthorizationRequest placementAuthorizationRequest = PlacementAuthorizationRequest
            .buildPlacementAuthorizationRequestWith()
            .id(requestId)
            .providerFormId(providerFormId)
            .tutorAssessmentId(assessmentId)
            .requestSubmissionStatus(PlacementAuthorizationRequestStatus.SUBMITTED)
            .providerRegistrationStatus(PlacementAuthorizationRequestStatus.REGISTERED)
            .providerFormSubmissionStatus(PlacementAuthorizationRequestStatus.SUBMITTED)
            .tutorAssessmentSubmissionStatus(PlacementAuthorizationRequestStatus.SUBMITTED)
            .build();

    private static final PlacementAuthorizationRequest savedPlacementAuthorizationRequest = PlacementAuthorizationRequest
            .buildPlacementAuthorizationRequestWith()
            .id(requestId)
            .providerFormId(providerFormId)
            .tutorAssessmentId(assessmentId)
            .requestSubmissionStatus(PlacementAuthorizationRequestStatus.DRAFT)
            .build();

    private static final PlacementAuthRequestDto placementAuthRequestDto = PlacementAuthRequestDto
            .buildSubmitPlacementAuthRequestDtoWith()
            .id(requestId)
            .build();

    private static List<PlacementAuthorizationRequest> placementAuthorizationRequests;

    @BeforeAll
    public static void initialiseVariables(){
        placementAuthorizationRequests = new ArrayList<>();
        placementAuthorizationRequests.add(placementAuthorizationRequest);
    }


    @Test
    void givenInvalidUserIdThenReturnException(){
        when(userService.getUserById(anyString())).thenReturn(null);
        assertThrows(PreConditionFailedException.class, () -> placementAuthRequestService.getPlacementAuthRequestByUserId(userId));
    }

    @Test
    void givenValidStudentUserThenReturnRequests(){
        when(userService.getUserById(anyString())).thenReturn(studentUser);
        when(placementAuthorizationRequestRepository.findPlacementAuthRequestsByUserId(anyString())).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () -> placementAuthRequestService.getPlacementAuthRequestByUserId(userId));
    }

    @Test
    void givenValidTutorUserThenReturnRequests(){
        when(userService.getUserById(anyString())).thenReturn(tutorUser);
        when(placementAuthorizationRequestRepository.findPlacementAuthRequestsBySchoolAndStatus(anyString(),anyString())).thenReturn(placementAuthorizationRequests);
        assertEquals(placementAuthorizationRequests.size(), placementAuthRequestService.getPlacementAuthRequestByUserId(userId).size());
    }

    @Test
    void givenValidProviderUserThenReturnRequests(){
        when(userService.getUserById(anyString())).thenReturn(providerUser);
        when(placementAuthorizationRequestRepository.findPlacementAuthRequestsByProviderContactEmailIdAndStatus(anyString(),anyString())).thenReturn(placementAuthorizationRequests);
        assertEquals(placementAuthorizationRequests.size(), placementAuthRequestService.getPlacementAuthRequestByUserId(userId).size());
    }

    @Test
    void givenInvalidRequestIdThenReturnException(){
        when(placementAuthorizationRequestRepository.findPlacementAuthRequestById(requestId)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> placementAuthRequestService.getPlacementAuthRequestById(requestId));
    }

    @Test
    void givenValidRequestIdThenReturnPlacementAuthRequests(){
        when(placementAuthorizationRequestRepository.findPlacementAuthRequestById(anyString())).thenReturn(placementAuthorizationRequest);
        assertEquals(requestId, placementAuthRequestService.getPlacementAuthRequestById(requestId).getId());
    }

    @Test
    void givenSubmittedPlacementRequestDtoToSubmitThenReturnException(){
        when(placementAuthorizationRequestRepository.findPlacementAuthRequestById(anyString())).thenReturn(placementAuthorizationRequest);
        assertThrows(ResourceAlreadyExistsException.class, () -> placementAuthRequestService.submitPlacementAuthRequest(placementAuthRequestDto));
    }

    @Test
    void givenPlacementRequestDtoToSubmitThenReturnSubmittedRequest() {
        when(placementAuthorizationRequestRepository.findPlacementAuthRequestById(anyString())).thenReturn(savedPlacementAuthorizationRequest);
        when(placementAuthorizationRequestRepository.save(any())).thenReturn(placementAuthorizationRequest);
        PlacementAuthorizationRequest placementAuthorizationRequest1 = placementAuthRequestService.submitPlacementAuthRequest(placementAuthRequestDto);
        assertEquals(PlacementAuthorizationRequestStatus.SUBMITTED, placementAuthorizationRequest1.getRequestSubmissionStatus());
    }

    @Test
    void givenSubmittedPlacementRequestDtoToSaveThenReturnException(){
        when(placementAuthorizationRequestRepository.findPlacementAuthRequestById(anyString())).thenReturn(placementAuthorizationRequest);
        assertThrows(ResourceAlreadyExistsException.class, () -> placementAuthRequestService.savePlacementAuthRequest(placementAuthRequestDto));
    }

    @Test
    void givenPlacementRequestDtoToSaveThenReturnSavedRequest() {
        when(placementAuthorizationRequestRepository.findPlacementAuthRequestById(anyString())).thenReturn(savedPlacementAuthorizationRequest);
        when(placementAuthorizationRequestRepository.save(any())).thenReturn(savedPlacementAuthorizationRequest);
        PlacementAuthorizationRequest placementAuthorizationRequest1 = placementAuthRequestService.savePlacementAuthRequest(placementAuthRequestDto);
        assertEquals(PlacementAuthorizationRequestStatus.DRAFT, placementAuthorizationRequest1.getRequestSubmissionStatus());
    }

    @Test
    void givenRequestIdToUpdateAssessmentDetailsThenReturnUpdatedRequest(){
        when(placementAuthorizationRequestRepository.findPlacementAuthRequestById(any())).thenReturn(placementAuthorizationRequest);
        when(placementAuthorizationRequestRepository.save(any())).thenReturn(placementAuthorizationRequest);
        PlacementAuthorizationRequest placementAuthorizationRequest1 = placementAuthRequestService.updateAssessmentDetails(requestId,
                assessmentId,
                PlacementAuthorizationRequestStatus.SUBMITTED,
                PlacementAuthorizationRequestStatus.AUTHORIZED);
        assertEquals(PlacementAuthorizationRequestStatus.SUBMITTED, placementAuthorizationRequest1.getTutorAssessmentSubmissionStatus());
    }

    @Test
    void givenRequestIdToUpdateAssessmentDetailsThenReturnExceptionCase1(){
        PlacementAuthorizationRequest placementAuthorizationRequest1 = PlacementAuthorizationRequest
                .buildPlacementAuthorizationRequestWith()
                .id(requestId)
                .providerFormId(providerFormId)
                .tutorAssessmentId(assessmentId)
                .requestSubmissionStatus(PlacementAuthorizationRequestStatus.PENDING)
                .build();
        when(placementAuthorizationRequestRepository.findPlacementAuthRequestById(any())).thenReturn(placementAuthorizationRequest1);
        assertThrows(PreConditionFailedException.class, () -> placementAuthRequestService.updateAssessmentDetails(requestId,
                assessmentId,
                PlacementAuthorizationRequestStatus.SUBMITTED,
                PlacementAuthorizationRequestStatus.AUTHORIZED));
    }

    @Test
    void givenRequestIdToUpdateAssessmentDetailsThenReturnExceptionCase2(){
        PlacementAuthorizationRequest placementAuthorizationRequest1 = PlacementAuthorizationRequest
                .buildPlacementAuthorizationRequestWith()
                .id(requestId)
                .providerFormId(providerFormId)
                .tutorAssessmentId(assessmentId)
                .placementAuthorizationRequestStatus(PlacementAuthorizationRequestStatus.AUTHORIZED)
                .build();
        when(placementAuthorizationRequestRepository.findPlacementAuthRequestById(any())).thenReturn(placementAuthorizationRequest1);
        assertThrows(PreConditionFailedException.class, () -> placementAuthRequestService.updateAssessmentDetails(requestId,
                assessmentId,
                PlacementAuthorizationRequestStatus.SUBMITTED,
                PlacementAuthorizationRequestStatus.AUTHORIZED));
    }

    @Test
    void givenRequestIdToUpdateAssessmentDetailsThenReturnExceptionCase3(){
        PlacementAuthorizationRequest placementAuthorizationRequest1 = PlacementAuthorizationRequest
                .buildPlacementAuthorizationRequestWith()
                .id(requestId)
                .providerFormId(providerFormId)
                .tutorAssessmentId(assessmentId)
                .providerFormSubmissionStatus(PlacementAuthorizationRequestStatus.PENDING)
                .build();
        when(placementAuthorizationRequestRepository.findPlacementAuthRequestById(any())).thenReturn(placementAuthorizationRequest1);
        assertThrows(PreConditionFailedException.class, () -> placementAuthRequestService.updateAssessmentDetails(requestId,
                assessmentId,
                PlacementAuthorizationRequestStatus.SUBMITTED,
                PlacementAuthorizationRequestStatus.AUTHORIZED));
    }
    @Test
    void givenRequestIdToUpdateProviderFormDetailsThenReturnUpdatedRequest(){
        when(placementAuthorizationRequestRepository.findPlacementAuthRequestById(any())).thenReturn(placementAuthorizationRequest);
        when(placementAuthorizationRequestRepository.save(any())).thenReturn(placementAuthorizationRequest);
        PlacementAuthorizationRequest placementAuthorizationRequest1 = placementAuthRequestService.updateProviderFormDetails(requestId, providerFormId, PlacementAuthorizationRequestStatus.SUBMITTED);
        assertEquals(PlacementAuthorizationRequestStatus.SUBMITTED, placementAuthorizationRequest1.getProviderFormSubmissionStatus());
    }


    @Test
    void givenRequestIdToUpdateProviderRegistrationDetailsThenReturnUpdatedRequest(){
        when(placementAuthorizationRequestRepository.findPlacementAuthRequestById(any())).thenReturn(placementAuthorizationRequest);
        when(placementAuthorizationRequestRepository.save(any())).thenReturn(placementAuthorizationRequest);
        PlacementAuthorizationRequest placementAuthorizationRequest1 = placementAuthRequestService.updateProviderRegistrationDetails(requestId);
        assertEquals(PlacementAuthorizationRequestStatus.REGISTERED, placementAuthorizationRequest1.getProviderRegistrationStatus());
    }

}
