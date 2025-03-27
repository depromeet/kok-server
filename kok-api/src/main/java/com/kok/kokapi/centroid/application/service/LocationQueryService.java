package com.kok.kokapi.centroid.application.service;

import com.kok.kokcore.location.domain.Location;
import com.kok.kokcore.location.port.out.ReadLocationPort;
import com.kok.kokcore.location.usecase.ReadLocationUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationQueryService implements ReadLocationUseCase {

    private final ReadLocationPort readLocationPort;

    @Override
    public Location readLocation(String roomId, String memberId) {
        return readLocationPort.findLocationByRoomIdAndMemberId(roomId, memberId)
            .orElseThrow(() -> new IllegalArgumentException("해당 멤버의 위치를 찾을 수 없습니다."));
    }

    @Override
    public List<Location> readLocations(String roomId) {
        return readLocationPort.findLocationsByRoomId(roomId);
    }

    @Override
    public List<Location> readInsideConvexHull(String roomId) {
        return readLocationPort.findInsideConvexHull(roomId);
    }

    @Override
    public List<Location> readConvexHull(String roomId) {
        return readLocationPort.findConvexHull(roomId);
    }
}
