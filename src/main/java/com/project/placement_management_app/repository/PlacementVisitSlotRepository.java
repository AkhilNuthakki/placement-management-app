package com.project.placement_management_app.repository;

import com.project.placement_management_app.model.PlacementVisitSlot;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlacementVisitSlotRepository extends MongoRepository<PlacementVisitSlot, String> {

    @Aggregation(pipeline = {
            "{'$match' : {'school' : '?0'}}" ,
            "{'$unwind': '$slots'}",
            "{'$sort': {'slots.startTime': 1} }",
            "{'$group': {_id: '$_id', school: {'$first': '$school'}, slots: {'$push': '$slots'}}}"
    })
    PlacementVisitSlot findPlacementVisitSlotBySchool(String schoolName);

}
