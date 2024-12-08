package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildProviderWorkFactorWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProviderWorkFactor {

    private String hasAnyHazardsOrRisks;
    private String hazardsOrRisks;
    private String isAnyTrainingRequired;
    private String howTrainingIsProvided;
    private String isWorkFromHome;
    private String howFrequentlyWorkFromHome;
    private String howStudentIsMonitored;


}
