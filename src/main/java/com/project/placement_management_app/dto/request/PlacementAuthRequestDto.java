package com.project.placement_management_app.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.placement_management_app.model.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildSubmitPlacementAuthRequestDtoWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PlacementAuthRequestDto {
    private String id;
    private String userId;
    @NotNull(message = "Student details are required")
    private Student student;
    @NotNull(message = "Placement Provider details are required")
    private PlacementProvider placementProvider;
    @NotNull(message = "Placement Role details are required")
    private PlacementRole placementRole;
    @NotNull(message = "Work Factors are required")
    private WorkFactor workFactor;
    @NotNull(message = "Travel Factors are required")
    private TravelFactor travelFactor;
    @NotNull(message = "Location and Region Factors are required")
    private LocationAndRegionFactor locationAndRegionFactor;
    @NotNull(message = "Health Factors are required")
    private HealthFactor healthFactor;
    @NotNull(message = "Personal Factors are required")
    private PersonalFactor personalFactor;
    @NotNull(message = "Policies And Insurance Factors are required")
    private PoliciesAndInsuranceFactor policiesAndInsuranceFactor;
}
