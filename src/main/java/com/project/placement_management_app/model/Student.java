package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildStudentWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Student {

    private String firstName;
    private String lastName;
    private String studentNumber;
    private String emailId;
    private String course;
    private String school;
    private String telephone;
    private String academicYear;
    private String isInternationalStudent;
    private String isVisaAvailableDuringPlacement;
}
