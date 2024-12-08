package com.project.placement_management_app.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.placement_management_app.model.PlacementProvider;
import com.project.placement_management_app.model.Student;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildPlacementDtoWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PlacementDto {

    private String id;
    private String requestId;
    private String roleTitle;
    private Date startDate;
    private Date endDate;
    private Student student;
    private PlacementProvider placementProvider;


}
