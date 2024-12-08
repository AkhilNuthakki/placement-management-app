package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildHealthFactorWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HealthFactor {
    private String isAwareOfPreCautionaryMeasures;
    private String preCautionaryMeasures;
    private String hasDownloadedSafeZoneApp;
    private String hasAppliedGlobalHealthInsuranceCard;
}
