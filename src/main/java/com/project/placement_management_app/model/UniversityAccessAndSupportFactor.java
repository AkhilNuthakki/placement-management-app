package com.project.placement_management_app.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildUniversityAccessAndSupportFactorWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UniversityAccessAndSupportFactor {

    private String canUndertakePlacementVisits;
    private String reason;
    private String confidentialityToBeTakenToAccount;
    private String moreDetailsToAccount;
}

