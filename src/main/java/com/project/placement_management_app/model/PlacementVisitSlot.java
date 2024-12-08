package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildPlacementVisitSlotWith")
@Document(value = "placement_visit_slots")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PlacementVisitSlot {
    @MongoId
    private String id;
    private String school;
    private String academicYear;
    private List<VisitingSlot> slots;

}
