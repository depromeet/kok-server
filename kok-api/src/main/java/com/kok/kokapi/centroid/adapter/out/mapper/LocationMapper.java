package com.kok.kokapi.centroid.adapter.out.mapper;

import com.kok.kokapi.centroid.adapter.out.dto.LocationResponse;
import com.kok.kokcore.location.domain.Location;
import com.kok.kokapi.config.geometry.PointConverter;
import org.springframework.stereotype.Component;
import org.springframework.data.util.Pair;
import java.math.BigDecimal;
import java.util.List;

@Component
public class LocationMapper {

    private final PointConverter pointConverter;

    public LocationMapper(PointConverter pointConverter) {
        this.pointConverter = pointConverter;
    }

    public LocationResponse toResponse(Location location) {
        Pair<BigDecimal, BigDecimal> coordinates = pointConverter.toCoordinates(location.getPoint());
        return LocationResponse.of(
                location.getUuid(),
                location.getMemberId(),
                coordinates.getFirst(),
                coordinates.getSecond()
        );
    }

    public List<LocationResponse> toResponseList(List<Location> locations) {
        return locations.stream().map(this::toResponse).toList();
    }
}
