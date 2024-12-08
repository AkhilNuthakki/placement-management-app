package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildPlacementProviderFormWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Document(value = "placement_provider_forms")

public class ProviderForm {

    @MongoId
    private String id;
    private String studentName;
    private PlacementProvider placementProvider;
    private PlacementRole placementRole;
    private ProviderWorkFactor workFactor;
    private TravelFactor travelFactor;
    private LocationAndRegionFactor locationAndRegionFactor;
    private HealthFactor healthFactor;
    private PersonalFactor personalFactor;
    private PoliciesAndInsuranceProviderFactor policiesAndInsuranceProviderFactor;
    private PoliciesAndInsuranceNonUKProviderFactor policiesAndInsuranceNonUKProviderFactor;
    private HealthAndSafetyProviderFactor healthAndSafetyProviderFactor;
    private UniversityAccessAndSupportFactor universityAccessAndSupportFactor;
    private PlacementAuthorizationRequestStatus status;
    private Date statusDate;

}
