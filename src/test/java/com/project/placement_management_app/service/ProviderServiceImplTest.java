package com.project.placement_management_app.service;

import com.project.placement_management_app.dto.request.ProviderFormRequestDto;
import com.project.placement_management_app.dto.request.RegisterProviderRequestDto;
import com.project.placement_management_app.exception.ResourceAlreadyExistsException;
import com.project.placement_management_app.model.*;
import com.project.placement_management_app.repository.ProviderFormRepository;
import com.project.placement_management_app.service.placementAuthRequest.PlacementAuthRequestService;
import com.project.placement_management_app.service.provider.ProviderServiceImpl;
import com.project.placement_management_app.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProviderServiceImplTest {

    @InjectMocks
    private ProviderServiceImpl providerService;
    @Mock
    private ProviderFormRepository providerFormRepository;
    @Mock
    private PlacementAuthRequestService placementAuthRequestService;
    @Mock
    private UserService userService;

    private static final String formId = UUID.randomUUID().toString();

    private static final String requestId = UUID.randomUUID().toString();

    private static final ProviderFormRequestDto providerFormRequestDto =
            ProviderFormRequestDto.buildProviderFormRequestDtoWith()
                    .requestId(requestId)
                    .id(formId)
                    .build();

    private static final ProviderForm savedProviderForm = ProviderForm.buildPlacementProviderFormWith()
            .id(formId)
            .status(PlacementAuthorizationRequestStatus.DRAFT)
            .build();

    private static final ProviderForm submittedProviderForm = ProviderForm.buildPlacementProviderFormWith()
            .id(formId)
            .status(PlacementAuthorizationRequestStatus.SUBMITTED)
            .build();

    private static final PlacementAuthorizationRequest placementAuthorizationRequest =
            PlacementAuthorizationRequest.buildPlacementAuthorizationRequestWith()
                    .id(requestId)
                    .build();

    private static final RegisterProviderRequestDto registerProviderRequestDto =
            RegisterProviderRequestDto.buildRegisterUserRequestDtoWith()
                    .userName("firstName LastName")
                    .userEmailId("XXX15@student.le.ac.uk")
                    .build();

    private static final User user = User.buildUserWith()
            .firstName("firstName")
            .lastName("lastName")
            .userRole(Role.PROVIDER)
            .emailId("XXX15@student.le.ac.uk")
            .password("PASS1234")
            .build();


    @Test
    void givenValidProviderFormDetailsThenSaveProviderForm(){

        when(providerFormRepository.findProviderFormById(providerFormRequestDto.getId())).thenReturn(savedProviderForm);
        when(placementAuthRequestService.updateProviderFormDetails(requestId, providerFormRequestDto.getId(), PlacementAuthorizationRequestStatus.DRAFT)).thenReturn(placementAuthorizationRequest);
        when(providerFormRepository.save(any())).thenReturn(savedProviderForm);

        ProviderForm providerForm1 = providerService.saveProviderForm(providerFormRequestDto);
        assertEquals(providerForm1.getId(), formId);
        assertEquals(providerForm1.getStatus(), PlacementAuthorizationRequestStatus.DRAFT);
    }

    @Test
    void givenSubmittedProviderFormDetailsToSaveFormThenReturnException(){
        when(providerFormRepository.findProviderFormById(providerFormRequestDto.getId())).thenReturn(submittedProviderForm);
        assertThrows(ResourceAlreadyExistsException.class, () -> providerService.saveProviderForm(providerFormRequestDto));
    }

    @Test
    void givenValidProviderFormDetailsThenSubmitProviderForm(){

        when(providerFormRepository.findProviderFormById(providerFormRequestDto.getId())).thenReturn(savedProviderForm);
        when(placementAuthRequestService.updateProviderFormDetails(requestId, providerFormRequestDto.getId(), PlacementAuthorizationRequestStatus.SUBMITTED)).thenReturn(placementAuthorizationRequest);
        when(providerFormRepository.save(any())).thenReturn(submittedProviderForm);

        ProviderForm providerForm1 = providerService.submitProviderForm(providerFormRequestDto);
        assertEquals(providerForm1.getId(), formId);
        assertEquals(providerForm1.getStatus(), PlacementAuthorizationRequestStatus.SUBMITTED);
    }

    @Test
    void givenSubmittedProviderFormDetailsToSubmitFormThenReturnException(){
        when(providerFormRepository.findProviderFormById(providerFormRequestDto.getId())).thenReturn(submittedProviderForm);
        assertThrows(ResourceAlreadyExistsException.class, () -> providerService.submitProviderForm(providerFormRequestDto));
    }

    @Test
    void givenProviderDetailsToRegisterThenReturnRegisteredProvider(){
        when(userService.getUserByEmailId(registerProviderRequestDto.getUserEmailId())).thenReturn(null);
        when(userService.registerProvider(registerProviderRequestDto.getUserEmailId(), registerProviderRequestDto.getUserName())).thenReturn(user);

        User user = providerService.registerAndNotifyProvider(registerProviderRequestDto);
        assertEquals(user.getEmailId(), registerProviderRequestDto.getUserEmailId());
    }





}
