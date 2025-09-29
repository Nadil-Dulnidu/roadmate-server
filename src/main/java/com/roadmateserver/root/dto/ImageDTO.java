package com.roadmateserver.root.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageDTO {

    @JsonProperty("image_id")
    private Integer imageId;

    @JsonProperty("vehicle_id")
    private Integer vehicleId;

    @JsonProperty("image_url")
    @NotBlank(message = "Image URL must not be null")
    private String imageUrl;
}
