package com.project.placement_management_app.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildAutoFillAssessmentFormResponseDtoWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AutoFillAssessmentFormResponseDto {
    private String hasPublicLiabilityInsurance;
    private String hasEmployerLiabilityInsurance;
    private String hasProfessionalIndemnityInsurance;
    private String hasHealthAndSafetyPolicy;
    private String hasProviderHasAnyObligationsToVisits;
    private String hasAnyIssuesRelatedToConfidentiality;

}
