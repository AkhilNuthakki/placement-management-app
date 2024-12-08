package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildRoleAssessmentWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoleAssessment {

    private String hasMetDurationHoursRequirements;
    private String hasMetMinimumAcademicRequirements;
    private String hasProviderCompliedWithResponsibilities;
    private String hasConfidenceOnProvider;
    private String isStudentVisaValid;
    private String hasRoleDateComplyWithVisa;

}
