package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildVisitingSlotWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VisitingSlot {
    private Date startTime;
    private Date endTime;
    private List<String> placementVisitIds;
}
