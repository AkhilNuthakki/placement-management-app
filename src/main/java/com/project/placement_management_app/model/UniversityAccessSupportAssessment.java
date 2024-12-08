package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildUniversityAccessSupportAssessmentWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UniversityAccessSupportAssessment {

    private String hasProviderHasAnyObligationsToVisits;
    private String hasAnyIssuesRelatedToConfidentiality;

}
