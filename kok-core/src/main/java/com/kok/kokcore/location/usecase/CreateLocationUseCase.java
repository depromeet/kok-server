package com.kok.kokcore.location.usecase;

import com.kok.kokcore.location.domain.Location;
import java.math.BigDecimal;


public interface CreateLocationUseCase {

    Location createLocation(String roomId, String memberId, BigDecimal latitude,
        BigDecimal longitude);

    Location updateLocation(String roomId, String memberId, BigDecimal latitude,
        BigDecimal longitude);
}
