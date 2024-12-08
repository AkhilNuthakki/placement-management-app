package com.project.placement_management_app.controller;

import com.project.placement_management_app.dto.request.AssessmentRequestDto;
import com.project.placement_management_app.dto.response.AutoFillAssessmentFormResponseDto;
import com.project.placement_management_app.dto.response.MatchInfoAssessmentResponseDto;
import com.project.placement_management_app.model.PlacementAssessment;
import com.project.placement_management_app.model.RequestCountGroupByStatus;
import com.project.placement_management_app.service.assessment.AssessmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/placement-management")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping("/placement-assessment/{assessmentId}")
    public ResponseEntity<PlacementAssessment> getPlacementAssessmentById(@PathVariable String assessmentId){
        return new ResponseEntity<>(assessmentService.getAssessmentById(assessmentId), HttpStatus.OK);
    }

    @PostMapping(value = "/submit-placement-assessment")
    public ResponseEntity<PlacementAssessment> submitPlacementAssessment(@Valid @RequestBody AssessmentRequestDto assessmentRequestDto){
        return new ResponseEntity<>(assessmentService.submitAssessment(assessmentRequestDto), HttpStatus.CREATED);
    }

    @PostMapping(value = "/save-placement-assessment")
    public ResponseEntity<PlacementAssessment> savePlacementAssessment(@RequestBody AssessmentRequestDto assessmentRequestDto){
        return new ResponseEntity<>(assessmentService.saveAssessment(assessmentRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/placement-assessment/match-info/{requestId}")
    public ResponseEntity<List<MatchInfoAssessmentResponseDto>> matchInformationOfProviderFormAndRequestForm(@PathVariable String requestId){
        return new ResponseEntity<>(assessmentService.matchInformationOfProviderFormAndRequestForm(requestId), HttpStatus.OK);
    }

    @GetMapping("/placement-assessment/auto-fill/{requestId}")
    public ResponseEntity<AutoFillAssessmentFormResponseDto> autoFillAssessmentDetails(@PathVariable String requestId){
        return new ResponseEntity<>(assessmentService.autoFillAssessmentDetails(requestId), HttpStatus.OK);
    }

    @GetMapping("/placement-assessment/flags/{providerName}")
    public ResponseEntity<List<RequestCountGroupByStatus>> checkForRedFlagsOnProvider(@PathVariable String providerName){
        return new ResponseEntity<>(assessmentService.checkForRedFlagsOnProvider(providerName), HttpStatus.OK);
    }
}
