package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildPoliciesAndInsuranceAssessmentWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PoliciesAndInsuranceAssessment {

    private String hasPublicLiabilityInsurance;
    private String hasEmployerLiabilityInsurance;
    private String doesHoldIndemnityInsurance;
    private String hasHealthAndSafetyPolicy;

}
