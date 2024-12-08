package com.project.placement_management_app.service;


import com.project.placement_management_app.dto.request.PlacementVisitDto;
import com.project.placement_management_app.dto.request.PlacementVisitSlotDto;
import com.project.placement_management_app.exception.ResourceNotFoundException;
import com.project.placement_management_app.model.PlacementVisit;
import com.project.placement_management_app.model.PlacementVisitSlot;
import com.project.placement_management_app.model.PlacementVisitType;
import com.project.placement_management_app.model.VisitingSlot;
import com.project.placement_management_app.repository.PlacementVisitRepository;
import com.project.placement_management_app.repository.PlacementVisitSlotRepository;
import com.project.placement_management_app.service.placementVisit.PlacementVisitServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlacementVisitServiceImplTest {
    @InjectMocks
    private PlacementVisitServiceImpl placementVisitService;
    @Mock
    private PlacementVisitRepository placementVisitRepository;
    @Mock
    private PlacementVisitSlotRepository placementVisitSlotRepository;

    private static final String placementVisitId = UUID.randomUUID().toString();
    private static final String placementId = UUID.randomUUID().toString();
    private static final String placementVisitSlotId = UUID.randomUUID().toString();

    private static final PlacementVisitSlot unAvailablePlacementVisitSlot = PlacementVisitSlot.buildPlacementVisitSlotWith()
            .id(placementVisitSlotId)
            .school("school")
            .slots(Collections.emptyList())
            .build();

    private static final VisitingSlot visitingSlot = VisitingSlot.
            buildVisitingSlotWith()
            .startTime(new Date())
            .endTime(new Date())
            .placementVisitIds(Collections.emptyList())
            .build();
    private static final PlacementVisitSlot placementVisitSlot = PlacementVisitSlot.buildPlacementVisitSlotWith()
            .id(placementVisitSlotId)
            .school("school")
            .slots(new ArrayList<VisitingSlot>(){{add(visitingSlot);}})
            .build();

    private static final PlacementVisitDto placementVisitDto = PlacementVisitDto
            .buildPlacementVisitDtoWith()
            .id("")
            .placementId(placementId)
            .placementVisitType("VIRTUAL")
            .startTime(new Date())
            .endTime(new Date())
            .providerContactName("providerContactName")
            .providerContactEmail("TEST@test.com")
            .school("school")
            .build();

    private static final PlacementVisitDto ExisitngPlacementVisitDto = PlacementVisitDto
            .buildPlacementVisitDtoWith()
            .id(placementVisitId)
            .placementId(placementId)
            .placementVisitType("VIRTUAL")
            .startTime(new Date())
            .endTime(new Date())
            .school("school")
            .providerContactName("providerContactName")
            .providerContactEmail("TEST@test.com")
            .build();

    private static final PlacementVisit placementVisit = PlacementVisit
            .buildPlacementVisitWith()
            .id(placementVisitId)
            .placementId(placementId)
            .placementVisitType(PlacementVisitType.VIRTUAL)
            .startTime(new Date())
            .endTime(new Date())
            .school("school")
            .providerContactName("providerContactName")
            .providerContactEmail("TEST@test.com")
            .build();

    private static final PlacementVisitSlotDto placementVisitSlotDto = PlacementVisitSlotDto
            .buildPlacementVisitSlotDtoWith()
            .school("school")
            .slots(new ArrayList<VisitingSlot>(){{add(visitingSlot);}})
            .build();

    private static final PlacementVisitSlotDto existingPlacementVisitSlotDto = PlacementVisitSlotDto
            .buildPlacementVisitSlotDtoWith()
            .id(placementVisitSlotId)
            .school("school")
            .slots(new ArrayList<VisitingSlot>(){{add(visitingSlot);}})
            .build();


    @Test
    void givenSchoolWithUnAvailableSlotsThenReturnException(){
        when(placementVisitSlotRepository.findPlacementVisitSlotBySchool(anyString())).thenReturn(unAvailablePlacementVisitSlot);
        assertThrows(ResourceNotFoundException.class,() -> placementVisitService.getAvailablePlacementVisitSlots(anyString()));
    }

    @Test
    void givenSchoolWithAvailableSlotsThenReturnSlots(){
        when(placementVisitSlotRepository.findPlacementVisitSlotBySchool(anyString())).thenReturn(placementVisitSlot);
        List<VisitingSlot> visitingSlotList = placementVisitService.getAvailablePlacementVisitSlots(anyString());
        assertEquals(1, visitingSlotList.size());
    }

    @Test
    void givenNewPlacementVisitDtoThenSchedulePlacementVisit(){
        when(placementVisitRepository.findPlacementVisitBySlotTime(any())).thenReturn(Collections.emptyList());
        when(placementVisitRepository.save(any())).thenReturn(placementVisit);
        when(placementVisitSlotRepository.findPlacementVisitSlotBySchool(anyString())).thenReturn(placementVisitSlot);
        when(placementVisitSlotRepository.save(any())).thenReturn(placementVisitSlot);
        PlacementVisit placementVisit1 = placementVisitService.schedulePlacementVisit(placementVisitDto);
        assertEquals(placementVisit.getPlacementId(), placementVisit1.getPlacementId());
    }

    @Test
    void givenExistingPlacementVisitDtoThenReSchedulePlacementVisit(){
        when(placementVisitRepository.findPlacementVisitBySlotTime(any())).thenReturn(Collections.emptyList());
        when(placementVisitRepository.save(any())).thenReturn(placementVisit);
        when(placementVisitRepository.findPlacementVisitById(anyString())).thenReturn(placementVisit);
        when(placementVisitSlotRepository.findPlacementVisitSlotBySchool(anyString())).thenReturn(placementVisitSlot);
        when(placementVisitSlotRepository.save(any())).thenReturn(placementVisitSlot);
        PlacementVisit placementVisit1 = placementVisitService.schedulePlacementVisit(ExisitngPlacementVisitDto);
        assertEquals(placementVisit.getPlacementId(), placementVisit1.getPlacementId());
    }

    @Test
    void givenNewPlacementVisitSlotsThenAddAvailableVisitingSlots(){
        when(placementVisitSlotRepository.findPlacementVisitSlotBySchool(anyString())).thenReturn(placementVisitSlot);
        when(placementVisitSlotRepository.save(any())).thenReturn(placementVisitSlot);
        PlacementVisitSlot placementVisitSlot1 = placementVisitService.updateAvailablePlacementVisitSlots(placementVisitSlotDto);
        assertEquals(placementVisitSlot.getSlots().size(), placementVisitSlot1.getSlots().size());
    }

    @Test
    void givenNewPlacementVisitSlotsThenUpdateAvailableVisitingSlots(){
        when(placementVisitSlotRepository.findPlacementVisitSlotBySchool(anyString())).thenReturn(placementVisitSlot);
        when(placementVisitSlotRepository.save(any())).thenReturn(placementVisitSlot);
        PlacementVisitSlot placementVisitSlot1 = placementVisitService.updateAvailablePlacementVisitSlots(existingPlacementVisitSlotDto);
        assertEquals(placementVisitSlot.getSlots().size(), placementVisitSlot1.getSlots().size());
    }

    @Test
    void givenSchoolNameWithNoPlacementVisitSlotThenReturnException(){
        when(placementVisitSlotRepository.findPlacementVisitSlotBySchool(any())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> placementVisitService.getPlacementVisitSlots("school"));
    }


    @Test
    void givenSchoolNameWithPlacementVisitSlotThenReturnVisitSlot(){
        when(placementVisitSlotRepository.findPlacementVisitSlotBySchool(any())).thenReturn(placementVisitSlot);
        PlacementVisitSlot placementVisitSlot1 = placementVisitService.getPlacementVisitSlots("school");
        assertEquals(placementVisitSlot.getSchool(), placementVisitSlot1.getSchool());
    }

    @Test
    void givenPlacementIdWithNoPlacementVisitsThenReturnException(){
        when(placementVisitRepository.findPlacementVisitByPlacementId(any())).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () -> placementVisitService.getPlacementVisitsByPlacementId(placementId));
    }

    @Test
    void givenPlacementIdWithPlacementVisitsThenReturnPlacementVisits(){
        when(placementVisitRepository.findPlacementVisitByPlacementId(any())).thenReturn(new ArrayList<PlacementVisit>(){{add(placementVisit);}});
        List<PlacementVisit> placementVisitList = placementVisitService.getPlacementVisitsByPlacementId(placementId);
        assertEquals(1, placementVisitList.size());
    }





}
