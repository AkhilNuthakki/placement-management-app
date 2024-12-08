package com.project.placement_management_app.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildFilterPlacementAuthRequestDtoWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FilterPlacementAuthRequestDto {

    private String providerName;
    private String placementAuthRequestStatus;
    private String studentCourse;
}
