package com.project.placement_management_app.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "buildPostcodesIOApiResponseDtoWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostcodesIOApiResponseDto {
    private double longitude;
    private double latitude;
}
