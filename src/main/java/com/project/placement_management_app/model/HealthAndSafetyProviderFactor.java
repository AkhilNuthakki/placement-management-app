package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildHealthAndSafetyProviderFactorWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class HealthAndSafetyProviderFactor {

    private String hasProcedureForRecordingIncidents;
    private String hasHealthAndSafetyPolicy;
    private String doesProvidesHealthAndSafetyTraining;
}
