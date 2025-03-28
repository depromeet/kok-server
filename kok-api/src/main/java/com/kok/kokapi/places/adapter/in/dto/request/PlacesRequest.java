package com.kok.kokapi.places.adapter.in.dto.request;

import com.kok.kokcore.places.application.port.in.PlaceInput;
import com.kok.kokcore.places.domain.model.vo.PlaceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;


public record PlacesRequest(
    @NotBlank(message = "장소 카테고리는 필수값입니다.")
    PlaceType placeType,

    @DecimalMin(value = "33.0", message = "위도는 33 이상이어야 합니다.")
    @DecimalMax(value = "43.0", message = "위도는 43 이하이어야 합니다.")
    @Schema(defaultValue = "37.5665", description = "위도")
    double latitude,

    @DecimalMin(value = "123.0", message = "경도는 123 이상이어야 합니다.")
    @DecimalMax(value = "132.0", message = "경도는 132 이하이어야 합니다.")
    @Schema(defaultValue = "126.9788", description = "경도")
    double longitude,

    @Schema(defaultValue = "20", description = "최대 개수 20")
    Integer maxCount
) {
    public PlaceInput toPlaceInput() {
        return new PlaceInput(placeType, latitude, longitude, maxCount);
    }
}
