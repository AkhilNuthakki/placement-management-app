package com.project.placement_management_app.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "buildRegisterUserRequestDtoWith")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterUserRequestDto {

    @NotBlank(message = "Email is required")
    private String emailId;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password should be minimum of 8 characters")
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Password should be alphanumeric")
    private String password;

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotBlank(message = "School Information is required")
    private String school;

    @NotBlank(message = "User role is required")
    private String role;
}
