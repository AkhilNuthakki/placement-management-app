package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildLocationAndRegionFactorWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LocationAndRegionFactor {
    private String accommodationArrangements;
    private String otherAccommodationArrangements;
    private String hasCheckedWithFCDO;
    private String isAwareOfAnyRisksAtLocation;
    private String risksAtLocation;
    private String providerUKOrNonUK;

}
