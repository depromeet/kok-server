package com.kok.kokcore.public_transfortation.usecase;

import org.springframework.data.util.Pair;


public interface RetrievePublicTransportationUsecase {
    // Pair<소요시간, 환승횟수>
    Pair<Integer, Integer> retrievePublicTransportation(Long stationId, String UUID, Integer memberId);
}
