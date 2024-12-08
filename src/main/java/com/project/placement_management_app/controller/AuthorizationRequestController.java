package com.project.placement_management_app.controller;

import com.project.placement_management_app.dto.request.FilterPlacementAuthRequestDto;
import com.project.placement_management_app.dto.request.PlacementAuthRequestDto;
import com.project.placement_management_app.model.PlacementAuthorizationRequest;
import com.project.placement_management_app.service.placementAuthRequest.PlacementAuthRequestService;
import com.project.placement_management_app.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/placement-management")
public class AuthorizationRequestController {

    @Autowired
    private PlacementAuthRequestService placementAuthRequestService;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/requests/user-id/{userId}")
    public ResponseEntity<List<PlacementAuthorizationRequest>> getPlacementAuthorizationRequestByUserId(@PathVariable String userId){
        return new ResponseEntity<>(placementAuthRequestService.getPlacementAuthRequestByUserId(userId), HttpStatus.OK);
    }

    @GetMapping(value = "/requests/request-id/{requestId}")
    public ResponseEntity<PlacementAuthorizationRequest> getPlacementAuthorizationRequestById(@PathVariable String requestId){
        return new ResponseEntity<>(placementAuthRequestService.getPlacementAuthRequestById(requestId), HttpStatus.OK);
    }

    @PostMapping(value = "/submit-authorization-request")
    public ResponseEntity<PlacementAuthorizationRequest> submitPlacementAuthorizationRequest(@Valid @RequestBody PlacementAuthRequestDto placementAuthRequestDto){
        return new ResponseEntity<>(placementAuthRequestService.submitPlacementAuthRequest(placementAuthRequestDto), HttpStatus.CREATED);
    }

    @PostMapping(value = "/save-authorization-request")
    public ResponseEntity<PlacementAuthorizationRequest> savePlacementAuthorizationRequest(@RequestBody PlacementAuthRequestDto placementAuthRequestDto){
        return new ResponseEntity<>(placementAuthRequestService.savePlacementAuthRequest(placementAuthRequestDto), HttpStatus.CREATED);
    }

    @GetMapping(value = "/requests/distinct-provider-names")
    public ResponseEntity<List<String>> getDistinctPlacementProviderNames(){
        return new ResponseEntity<>(placementAuthRequestService.getDistinctPlacementProviderNames(), HttpStatus.OK);
    }

    @PutMapping(value = "/requests/filter")
    public ResponseEntity<List<PlacementAuthorizationRequest>> getFilteredPlacementAuthRequests(@RequestBody FilterPlacementAuthRequestDto filterPlacementAuthRequestDto){
        return new ResponseEntity<>(placementAuthRequestService.getFilteredPlacementAuthRequests(filterPlacementAuthRequestDto), HttpStatus.OK);
    }
}
