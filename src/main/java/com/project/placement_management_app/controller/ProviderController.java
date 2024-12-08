package com.project.placement_management_app.controller;

import com.project.placement_management_app.dto.request.ProviderFormRequestDto;
import com.project.placement_management_app.dto.request.RegisterProviderRequestDto;
import com.project.placement_management_app.model.ProviderForm;
import com.project.placement_management_app.model.User;
import com.project.placement_management_app.service.provider.ProviderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1.0/placement-management")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @GetMapping("/provider-form/{providerFormId}")
    public ResponseEntity<ProviderForm> getProviderFormById(@PathVariable String providerFormId){
        return new ResponseEntity<>(providerService.getProviderFromById(providerFormId), HttpStatus.OK);
    }

    @PostMapping(value = "/submit-provider-form")
    public ResponseEntity<ProviderForm> submitPlacementAuthorizationRequest(@Valid @RequestBody ProviderFormRequestDto providerFormRequestDto){
        return new ResponseEntity<>(providerService.submitProviderForm(providerFormRequestDto), HttpStatus.CREATED);
    }

    @PostMapping(value = "/save-provider-form")
    public ResponseEntity<ProviderForm> savePlacementAuthorizationRequest(@RequestBody ProviderFormRequestDto providerFormRequestDto){
        return new ResponseEntity<>(providerService.saveProviderForm(providerFormRequestDto), HttpStatus.CREATED);
    }

    @PostMapping(value = "/register-and-notify-provider")
    public ResponseEntity<User> registerProvider(@Valid @RequestBody RegisterProviderRequestDto registerProviderRequestDto){
        return new ResponseEntity<>(providerService.registerAndNotifyProvider(registerProviderRequestDto), HttpStatus.CREATED);
    }

}
