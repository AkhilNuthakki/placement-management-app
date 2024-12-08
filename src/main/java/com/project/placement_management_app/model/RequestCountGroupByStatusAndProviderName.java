package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "buildRequestCountGroupByStatusAndProviderNameWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestCountGroupByStatusAndProviderName {
    private String providerName;
    private List<RequestCountGroupByStatus> statuses;
}
