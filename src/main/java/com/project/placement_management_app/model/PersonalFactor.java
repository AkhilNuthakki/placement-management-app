package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildPersonalFactorWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonalFactor {
    private String hasConsideredAnyAdjustments;
}
