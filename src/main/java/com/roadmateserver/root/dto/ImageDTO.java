package com.roadmateserver.root.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageDTO {

    @JsonProperty("image_id")
    private Integer imageId;

    @JsonProperty("vehicle_id")
    private Integer vehicleId;

    @NonNull
    @JsonProperty("image_url")
    @NotBlank(message = "Image URL must not be null")
    private String imageUrl;
}
