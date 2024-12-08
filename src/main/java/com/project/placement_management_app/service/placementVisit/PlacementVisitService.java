package com.project.placement_management_app.service.placementVisit;

import com.project.placement_management_app.dto.request.PlacementVisitDto;
import com.project.placement_management_app.dto.request.PlacementVisitSlotDto;
import com.project.placement_management_app.model.Placement;
import com.project.placement_management_app.model.PlacementVisit;
import com.project.placement_management_app.model.PlacementVisitSlot;
import com.project.placement_management_app.model.VisitingSlot;

import java.util.List;

public interface PlacementVisitService {

    List<VisitingSlot> getAvailablePlacementVisitSlots(String schoolName);

    PlacementVisit schedulePlacementVisit(PlacementVisitDto placementVisitDto);

    PlacementVisitSlot updateAvailablePlacementVisitSlots(PlacementVisitSlotDto placementVisitSlotDto);

    PlacementVisitSlot getPlacementVisitSlots(String schoolName);

    List<PlacementVisit> getPlacementVisitsByPlacementId(String placementId);

    PlacementVisit getPlacementVisitById(String placementVisitId);

    List<List<Placement>> getSmartVisitPlanSuggestions(String schoolName);

}
