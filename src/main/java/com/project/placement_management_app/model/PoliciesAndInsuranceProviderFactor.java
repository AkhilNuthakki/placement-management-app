package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildPoliciesAndInsuranceProviderFactorWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PoliciesAndInsuranceProviderFactor {

    private String hasPublicLiabilityInsurance;
    private String publicLiabilityInsuranceProviderName;
    private Date publicLiabilityInsuranceExpiryDate;
    private String happensWhenThereIsPublicClaim;

    private String hasEmployerLiabilityInsurance;
    private String employerInsuranceProviderName;
    private Date employerInsuranceExpiryDate;
    private String happensWhenThereIsEmployeeClaim;

    private String hasProfessionalIndemnityInsurance;
    private String indemnityInsuranceProviderName;
    private Date indemnityInsuranceExpiryDate;
}
