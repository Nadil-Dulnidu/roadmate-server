package com.roadmateserver.root.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.roadmateserver.root.common.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Data Transfer Object representing a user in the system")
public class UserDTO {
    @JsonProperty("user_id")
    @Schema(description = "Unique identifier for the user", example = "1")
    private Integer userId;

    @NotBlank(message = "Clerk ID must not be null")
    @NonNull
    @Schema(description = "Unique identifier for the user assigned by the clerk", example = "Clerk123")
    @JsonProperty("clerk_id")
    private String clerkId;

    @NotBlank(message = "First name must not be null")
    @NonNull
    @Schema(description = "First name of the user", example = "John")
    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    @NonNull
    @Schema(description = "Last name of the user", example = "Doe")
    @NotBlank(message = "Last name must not be null")
    private String lastName;

    @NotBlank(message = "Address must not be null")
    @NonNull
    @Email(message = "Email must be valid")
    @Schema(description = "Email address of the user", example = "johnDoe@gmail.com")
    @JsonProperty("email")
    private String email;

    @NotNull(message = "User role must not be null")
    @NonNull
    @Schema(description = "Role of the user in the system", example = "USER")
    @JsonProperty("role")
    private Constants.UserRole role;

    @JsonProperty("created_at")
    @NonNull
    @Schema(description = "Timestamp when the user was created", example = "2023-10-01T12:00:00Z")
    @NotNull(message = "Created at date must not be null")
    private Instant createdAt;

}
