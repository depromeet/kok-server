package com.kok.kokapi.centroid.application.service;

import com.kok.kokapi.config.geometry.PointConverter;
import com.kok.kokcore.location.domain.Location;
import com.kok.kokcore.location.port.out.ReadLocationPort;
import com.kok.kokcore.location.port.out.SaveLocationPort;
import com.kok.kokcore.location.usecase.CreateLocationUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LocationCommandService implements CreateLocationUseCase {

    private final SaveLocationPort saveLocationPort;
    private final ReadLocationPort readLocationPort;
    private final PointConverter pointConverter;

    @Override
    public Location createLocation(String roomId, String memberId, BigDecimal latitude,
        BigDecimal longitude) {
        Point point = pointConverter.fromCoordinates(latitude, longitude);
        return saveLocationPort.saveLocation(roomId, memberId, point);
    }

    @Override
    @Transactional
    public Location updateLocation(String roomId, String memberId, BigDecimal latitude,
        BigDecimal longitude) {
        Location location = readLocationPort.findLocationByRoomIdAndMemberId(roomId, memberId)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 정보가 없습니다."));
        Point newPoint = pointConverter.fromCoordinates(latitude, longitude);
        location.changePoint(newPoint);
        return location;
    }
}
