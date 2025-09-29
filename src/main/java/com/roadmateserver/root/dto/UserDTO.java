package com.roadmateserver.root.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.roadmateserver.root.common.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    @JsonProperty("user_id")
    private Integer userId;

    @NotBlank(message = "Clerk ID must not be null")
    @NonNull
    @JsonProperty("clerk_id")
    private String clerkId;

    @NotBlank(message = "First name must not be null")
    @NonNull
    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    @NonNull
    @NotBlank(message = "Last name must not be null")
    private String lastName;

    @NotBlank(message = "Address must not be null")
    @NonNull
    @Email(message = "Email must be valid")
    @JsonProperty("email")
    private String email;

    @NotNull(message = "User role must not be null")
    @NonNull
    @JsonProperty("role")
    private Constants.UserRole role;

    @JsonProperty("created_at")
    @NonNull
    @NotNull(message = "Created at date must not be null")
    private Instant createdAt;
}
