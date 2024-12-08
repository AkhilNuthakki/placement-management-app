package com.project.placement_management_app.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildPlacementVisitDtoWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PlacementVisitDto {

    private String id;
    private String placementId;
    private String placementVisitType;
    private Date startTime;
    private Date endTime;
    private String studentName;
    private String studentEmail;
    private String school;
    private String providerContactName;
    private String providerContactEmail;
}
