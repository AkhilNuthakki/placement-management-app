package com.project.placement_management_app.repository;

import com.project.placement_management_app.model.Placement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PlacementRepository extends MongoRepository<Placement,String> {
    @Query(value = "{_id:'?0'}")
    Placement findPlacementByID(String id);

    @Query(value = "{'student.emailId':'?0'}")
    List<Placement> findPlacementsByStudentEmailID(String studentEmailId);

    @Query(value = "{'student.school':'?0'}")
    List<Placement> findPlacementsBySchool(String studentEmailId);

}
