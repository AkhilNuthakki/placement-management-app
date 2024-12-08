package com.project.placement_management_app.repository;

import com.project.placement_management_app.model.PlacementVisit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface PlacementVisitRepository extends MongoRepository<PlacementVisit, String> {
    @Query(value = "{placementId : '?0'}")
    List<PlacementVisit> findPlacementVisitByPlacementId(String placementId);

    @Query(value = "{_id : '?0'}")
    PlacementVisit findPlacementVisitById(String id);

    @Query(value = "{startTime : '?0'}")
    List<PlacementVisit> findPlacementVisitBySlotTime(Date slotTime);
}
