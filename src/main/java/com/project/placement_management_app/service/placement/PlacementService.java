package com.project.placement_management_app.service.placement;


import com.project.placement_management_app.dto.request.FilterPlacementsRequestDto;
import com.project.placement_management_app.dto.request.PlacementDto;
import com.project.placement_management_app.model.Placement;
import com.project.placement_management_app.model.PlacementAuthorizationRequest;
import com.project.placement_management_app.model.ProviderForm;

import java.util.List;

public interface PlacementService {

    Placement addPlacement(PlacementAuthorizationRequest placementAuthorizationRequest, ProviderForm providerForm);
    List<Placement> getPlacementsByUserId(String userId);
    Placement getPlacementById(String placementId);
    Placement updatePlacement(PlacementDto placementDto);
    List<Placement> getFilteredPlacements(FilterPlacementsRequestDto filterPlacementsRequestDto);
    List<Placement> getPlacementsBySchool(String schoolName);

}
