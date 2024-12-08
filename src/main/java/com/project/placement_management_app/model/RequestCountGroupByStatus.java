package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "buildRequestCountGroupByStatusWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestCountGroupByStatus {
    private PlacementAuthorizationRequestStatus status;
    private long count;

}
