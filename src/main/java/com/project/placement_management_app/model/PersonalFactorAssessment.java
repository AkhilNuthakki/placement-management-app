package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildPersonalFactorAssessmentWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonalFactorAssessment {
    private String hasStudentPersonalFactorsImplicates;
}
