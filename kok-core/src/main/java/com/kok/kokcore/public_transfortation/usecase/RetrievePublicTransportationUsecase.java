package com.kok.kokcore.public_transfortation.usecase;

import java.util.List;

public interface RetrievePublicTransportationUsecase {
    // List{소요시간, 환승횟수}
    List<Integer> retrievePublicTransportation(Long stationId, String UUID, Integer memberId);
}
