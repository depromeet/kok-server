package com.kok.kokapi.centroid.adapter.out.mapper;

import com.kok.kokapi.centroid.adapter.in.dto.response.LocationResponse;
import com.kok.kokapi.config.geometry.PointConverter;
import com.kok.kokcore.location.domain.Location;
import com.kok.kokcore.room.domain.Member;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    private final PointConverter pointConverter;

    public LocationMapper(PointConverter pointConverter) {
        this.pointConverter = pointConverter;
    }

    public LocationResponse toResponse(Location location) {
        Pair<BigDecimal, BigDecimal> coordinates = pointConverter.toCoordinates(
            location.getLocation_point());
        return LocationResponse.of(
            location.getRoomId(),
            location.getMemberId(),
            coordinates.getFirst(),
            coordinates.getSecond(),
            location.getName()
        );
    }

    public LocationResponse toResponse(Location location, Member member) {
        Pair<BigDecimal, BigDecimal> coordinates = pointConverter.toCoordinates(
            location.getLocation_point());
        return LocationResponse.of(
            location.getRoomId(),
            member.getMemberId(),
            member.getProfile(),
            coordinates.getFirst(),
            coordinates.getSecond()
        );
    }

    public List<LocationResponse> toResponseList(List<Location> locations) {
        return locations.stream().map(this::toResponse).toList();
    }
}
