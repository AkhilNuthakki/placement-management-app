package com.project.placement_management_app.controller;
;
import com.project.placement_management_app.dto.request.FilterPlacementsRequestDto;
import com.project.placement_management_app.dto.request.PlacementDto;
import com.project.placement_management_app.model.Placement;
import com.project.placement_management_app.service.placement.PlacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/placement-management")
public class PlacementController {

    @Autowired
    private PlacementService placementService;

    @GetMapping("/placements/{placementId}")
    public ResponseEntity<Placement> getPlacementById(@PathVariable String placementId){
        return new ResponseEntity<>(placementService.getPlacementById(placementId), HttpStatus.OK);
    }

    @GetMapping("/placements/user-id/{userId}")
    public ResponseEntity<List<Placement>> getPlacementsByUserId(@PathVariable String userId){
        return new ResponseEntity<>(placementService.getPlacementsByUserId(userId), HttpStatus.OK);
    }

    @PatchMapping("/placements/update-placement")
    public ResponseEntity<Placement> updatePlacementDetails(@RequestBody PlacementDto placementDto){
        return new ResponseEntity<>(placementService.updatePlacement(placementDto),HttpStatus.OK);
    }

    @PutMapping(value = "/placements/filter")
    public ResponseEntity<List<Placement>> getFilteredPlacements(@RequestBody FilterPlacementsRequestDto filterPlacementsRequestDto){
        return new ResponseEntity<>(placementService.getFilteredPlacements(filterPlacementsRequestDto), HttpStatus.OK);
    }

}
