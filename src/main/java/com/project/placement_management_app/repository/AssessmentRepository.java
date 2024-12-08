package com.project.placement_management_app.repository;

import com.project.placement_management_app.model.PlacementAssessment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface AssessmentRepository extends MongoRepository<PlacementAssessment, String> {

    @Query(value = "{_id : '?0'}")
    PlacementAssessment findPlacementAssessmentById(String id);
}
