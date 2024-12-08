package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildPlacementRoleWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PlacementRole {
    private String title;
    private Date startDate;
    private Date endDate;
    private int workingHours;
    private String hasProbationPeriod;
    private int probationPeriodInWeeks;
    private Double salary;
    private String sourceOfRole;
    private String otherSourceOfRole;
    private String hasInformedProvider;
    private String roleDescription;
}
