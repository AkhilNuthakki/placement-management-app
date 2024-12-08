package com.project.placement_management_app.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.placement_management_app.model.VisitingSlot;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildPlacementVisitSlotDtoWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PlacementVisitSlotDto {
    private String id;
    private String school;
    private String academicYear;
    private List<VisitingSlot> slots;
}
