package com.project.placement_management_app.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.placement_management_app.model.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildProviderFormRequestDtoWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProviderFormRequestDto {

    private String id;
    private String requestId;
    private String studentName;
    @NotNull(message = "Placement Provider details are required")
    private PlacementProvider placementProvider;
    @NotNull(message = "Placement Role details are required")
    private PlacementRole placementRole;
    @NotNull(message = "Work factor details are required")
    private ProviderWorkFactor workFactor;
    @NotNull(message = "Travel factor details are required")
    private TravelFactor travelFactor;
    @NotNull(message = "Health factor details are required")
    private HealthFactor healthFactor;
    @NotNull(message = "Location and region factor details are required")
    private LocationAndRegionFactor locationAndRegionFactor;
    @NotNull(message = "Personal factor details are required")
    private PersonalFactor personalFactor;
    private PoliciesAndInsuranceProviderFactor policiesAndInsuranceProviderFactor;
    private PoliciesAndInsuranceNonUKProviderFactor policiesAndInsuranceNonUKProviderFactor;
    @NotNull(message = "Health and Safety details are required")
    private HealthAndSafetyProviderFactor healthAndSafetyProviderFactor;
    @NotNull(message = "University Access and Support Factor details are required")
    private UniversityAccessAndSupportFactor universityAccessAndSupportFactor;

}
