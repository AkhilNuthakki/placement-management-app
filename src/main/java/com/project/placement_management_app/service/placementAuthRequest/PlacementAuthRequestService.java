package com.project.placement_management_app.service.placementAuthRequest;

import com.project.placement_management_app.dto.request.FilterPlacementAuthRequestDto;
import com.project.placement_management_app.dto.request.PlacementAuthRequestDto;
import com.project.placement_management_app.model.*;

import java.util.List;

public interface PlacementAuthRequestService {

    public List<PlacementAuthorizationRequest> getPlacementAuthRequestByUserId(String userId);

    public PlacementAuthorizationRequest getPlacementAuthRequestById(String requestId);

    public PlacementAuthorizationRequest submitPlacementAuthRequest(PlacementAuthRequestDto placementAuthRequestDto);

    public PlacementAuthorizationRequest savePlacementAuthRequest(PlacementAuthRequestDto placementAuthRequestDto);

    PlacementAuthorizationRequest updateAssessmentDetails(String requestId,
                                        String assessmentId,
                                        PlacementAuthorizationRequestStatus tutorAssessmentSubmissionStatus,
                                        PlacementAuthorizationRequestStatus placementAuthorizationRequestStatus);

    PlacementAuthorizationRequest updateProviderFormDetails(String requestId, String providerFormId, PlacementAuthorizationRequestStatus status);
    PlacementAuthorizationRequest updateProviderRegistrationDetails(String requestId);
    List<RequestCountGroupByStatus> getRequestsFromProviderGroupByStatus(String providerName);
    List<RequestCountGroupByStatus> getRequestsFromSchoolGroupByStatus(String schoolName);
    List<RequestCountGroupByStatusAndCourse> getRequestsFromSchoolGroupByStatusAndCourse(String schoolName);
    List<RequestCountGroupByStatusAndProviderName> getRequestsFromSchoolGroupByStatusAndProviderName(String schoolName);
    List<String> getDistinctPlacementProviderNames();
    List<PlacementAuthorizationRequest> getFilteredPlacementAuthRequests(FilterPlacementAuthRequestDto filterPlacementAuthRequestDto);
}
