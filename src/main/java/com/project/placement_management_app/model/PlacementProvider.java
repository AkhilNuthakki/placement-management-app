package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildPlacementProviderWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PlacementProvider {
    private String name;
    private String address;
    private String webAddress;
    private String postcode;
    private double latitude;
    private double longitude;
    private String contactName;
    private String contactEmail;
    private String contactJobTitle;
    private String telephone;
    private String hasUndertakenAnyActivities;
    private String organizationActivities;

}
