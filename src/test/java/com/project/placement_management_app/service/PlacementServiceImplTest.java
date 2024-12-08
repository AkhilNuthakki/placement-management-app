package com.project.placement_management_app.service;

import com.project.placement_management_app.dto.request.PlacementDto;
import com.project.placement_management_app.exception.PreConditionFailedException;
import com.project.placement_management_app.exception.ResourceNotFoundException;
import com.project.placement_management_app.model.*;
import com.project.placement_management_app.repository.PlacementRepository;
import com.project.placement_management_app.service.placement.PlacementServiceImpl;
import com.project.placement_management_app.service.user.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlacementServiceImplTest {

    @InjectMocks
    private PlacementServiceImpl placementService;
    @Mock
    private UserService userService;
    @Mock
    private PlacementRepository placementRepository;
    @Mock
    private MongoTemplate mongoTemplate;
    private static final String formId = UUID.randomUUID().toString();
    private static final String requestId = UUID.randomUUID().toString();
    private static final String placementId = UUID.randomUUID().toString();
    private static final String studentUserId = UUID.randomUUID().toString();
    private static final String tutorUserId = UUID.randomUUID().toString();

    private static final ProviderForm providerForm = ProviderForm.buildPlacementProviderFormWith()
            .id(formId)
            .placementProvider(PlacementProvider.buildPlacementProviderWith().build())
            .build();

    private static final PlacementAuthorizationRequest placementAuthorizationRequest =
            PlacementAuthorizationRequest.buildPlacementAuthorizationRequestWith()
                    .id(requestId)
                    .placementRole(PlacementRole.buildPlacementRoleWith().build())
                    .student(Student.buildStudentWith().build())
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

    private static final User studentUser = User.buildUserWith()
            .id(studentUserId)
            .firstName("firstName")
            .lastName("lastName")
            .school("Computing")
            .userRole(Role.STUDENT)
            .emailId("XXX15@student.le.ac.uk")
            .password("PASS1234")
            .build();

    private static final User tutotUser = User.buildUserWith()
            .id(tutorUserId)
            .firstName("firstName")
            .lastName("lastName")
            .school("Computing")
            .userRole(Role.TUTOR)
            .emailId("XXX15@student.le.ac.uk")
            .password("PASS1234")
            .build();

    private static final PlacementDto placementDto = PlacementDto.buildPlacementDtoWith()
            .id(placementId)
            .requestId(requestId)
            .startDate(new Date())
            .endDate(new Date())
            .placementProvider(PlacementProvider.buildPlacementProviderWith()
                    .contactName("contactName")
                    .contactEmail("TEST@test.com")
                    .telephone("0123456789")
                    .contactJobTitle("HR Manager").build())
            .build();

    private static List<Placement> placements;

    @BeforeAll
    public static void initialiseVariables(){
        placements = new ArrayList<>();
        placements.add(placement);
    }


    @Test
    void givenPlacementRequestAndProviderFormThenSavePlacement(){
        when(placementRepository.save(any())).thenReturn(placement);
        Placement placement1 = placementService.addPlacement(placementAuthorizationRequest, providerForm);
        assertEquals(placement1.getRequestId(), requestId);
    }

    @Test
    void givenInvalidUserIdToGetPlacementsThenReturnException()
    {
        when(userService.getUserById(studentUserId)).thenReturn(null);
        assertThrows(PreConditionFailedException.class,() -> placementService.getPlacementsByUserId(studentUserId));
    }

    @Test
    void givenValidStudentUserIdToGetPlacementsThenReturnException(){
        when(userService.getUserById(studentUserId)).thenReturn(studentUser);
        when(placementRepository.findPlacementsByStudentEmailID(studentUser.getEmailId())).thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class,() -> placementService.getPlacementsByUserId(studentUserId));
    }

    @Test
    void givenValidTutorUserIdToGetPlacementsThenReturnException(){
        when(userService.getUserById(tutorUserId)).thenReturn(tutotUser);
        when(placementRepository.findPlacementsBySchool(tutotUser.getSchool())).thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class,() -> placementService.getPlacementsByUserId(tutorUserId));
    }

    @Test
    void givenValidStudentUserIdToGetPlacementsThenReturnPlacements(){
        when(userService.getUserById(studentUserId)).thenReturn(studentUser);
        when(placementRepository.findPlacementsByStudentEmailID(studentUser.getEmailId())).thenReturn(placements);
        List<Placement> placementList = placementService.getPlacementsByUserId(studentUserId);
        assertEquals(1, placementList.size());
    }

    @Test
    void givenPlacementDtoToUpdatePlacementThenReturnSaveExecuted(){
        when(placementRepository.findPlacementByID(placementDto.getId())).thenReturn(placement);
        Placement placement1 = placementService.updatePlacement(placementDto);
        assertEquals(placement1.getPlacementProvider().getContactJobTitle(), placement.getPlacementProvider().getContactJobTitle());
    }
}
