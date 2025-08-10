package com.roadmateserver.root.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data Transfer Object representing an image associated with a vehicle")
public class ImageDTO {

    @JsonProperty("image_id")
    @Schema(description = "Unique identifier for the image", example = "1")
    private Integer imageId;

    @JsonProperty("vehicle_id")
    @Schema(description = "Unique identifier for the vehicle associated with the image", example = "1")
    private Integer vehicleId;

    @JsonProperty("image_url")
    @NotBlank(message = "Image URL must not be null")
    @Schema(description = "URL of the image", example = "https://example.com/image.jpg")
    private String imageUrl;
}
