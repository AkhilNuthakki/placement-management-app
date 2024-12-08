package com.project.placement_management_app.controller;

import com.project.placement_management_app.dto.request.PlacementVisitDto;
import com.project.placement_management_app.dto.request.PlacementVisitSlotDto;
import com.project.placement_management_app.model.Placement;
import com.project.placement_management_app.model.PlacementVisit;
import com.project.placement_management_app.model.PlacementVisitSlot;
import com.project.placement_management_app.model.VisitingSlot;
import com.project.placement_management_app.service.placementVisit.PlacementVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/placement-management/visits")
public class PlacementVisitController {

    @Autowired
    private PlacementVisitService placementVisitService;


    @GetMapping(value = "/available-slots/{schoolName}")
    public ResponseEntity<List<VisitingSlot>> getAvailablePlacementVisitSlots(@PathVariable String schoolName){
        return new ResponseEntity<>(placementVisitService.getAvailablePlacementVisitSlots(schoolName), HttpStatus.OK);
    }

    @PostMapping(value = "/schedule")
    public ResponseEntity<PlacementVisit> schedulePlacementVisit(@RequestBody PlacementVisitDto placementVisitDto){
        return new ResponseEntity<>(placementVisitService.schedulePlacementVisit(placementVisitDto), HttpStatus.CREATED);
    }

    @PostMapping(value = "/update-slots")
    public ResponseEntity<PlacementVisitSlot> updateAvailablePlacementVisitSlots(@RequestBody PlacementVisitSlotDto placementVisitSlotDto) {
        return new ResponseEntity<>(placementVisitService.updateAvailablePlacementVisitSlots(placementVisitSlotDto), HttpStatus.CREATED);
    }

    @GetMapping(value = "/all-slots/school/{schoolName}")
    public ResponseEntity<PlacementVisitSlot> getPlacementVisitSlots(@PathVariable String schoolName) {
        return new ResponseEntity<>(placementVisitService.getPlacementVisitSlots(schoolName), HttpStatus.OK);
    }

    @GetMapping(value = "/placement-id/{placementId}")
    public ResponseEntity<List<PlacementVisit>> getPlacementVisitsByPlacementId(@PathVariable String placementId){
        return new ResponseEntity<>(placementVisitService.getPlacementVisitsByPlacementId(placementId), HttpStatus.OK);
    }

    @GetMapping(value = "/placement-visit-id/{placementVisitId}")
    public ResponseEntity<PlacementVisit> getPlacementVisitByPlacementVisitId(@PathVariable String placementVisitId){
        return new ResponseEntity<>(placementVisitService.getPlacementVisitById(placementVisitId), HttpStatus.OK);
    }

    @GetMapping(value = "/smart-visit-plan/{schoolName}")
    public ResponseEntity<List<List<Placement>>> getSmartVisitPlanSuggestions(@PathVariable String schoolName){
        return new ResponseEntity<>(placementVisitService.getSmartVisitPlanSuggestions(schoolName), HttpStatus.OK);
    }

}
