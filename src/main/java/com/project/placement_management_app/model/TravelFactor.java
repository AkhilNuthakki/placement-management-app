package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildTravelFactorWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TravelFactor {
    private String TravelToPlacement;
    private String OtherTravelToPlacement;
    private String isWorkingInDifferentLocation;
    private String otherAddress;
    private String isRequiredOverseasTravel;
    private String hasReadOverseasTravelGuidance;
    private String hasConsideredHowToTravelOverseas;

}
