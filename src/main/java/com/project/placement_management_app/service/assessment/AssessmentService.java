package com.project.placement_management_app.service.assessment;

import com.project.placement_management_app.dto.request.AssessmentRequestDto;
import com.project.placement_management_app.dto.response.AutoFillAssessmentFormResponseDto;
import com.project.placement_management_app.dto.response.MatchInfoAssessmentResponseDto;
import com.project.placement_management_app.model.PlacementAssessment;
import com.project.placement_management_app.model.RequestCountGroupByStatus;

import java.util.List;

public interface AssessmentService {
    PlacementAssessment saveAssessment(AssessmentRequestDto assessmentRequestDto);
    PlacementAssessment submitAssessment(AssessmentRequestDto assessmentRequestDto);
    PlacementAssessment getAssessmentById(String assessmentId);
    List<MatchInfoAssessmentResponseDto> matchInformationOfProviderFormAndRequestForm(String requestId);
    List<RequestCountGroupByStatus> checkForRedFlagsOnProvider(String providerName);
    AutoFillAssessmentFormResponseDto autoFillAssessmentDetails(String requestId);
}
