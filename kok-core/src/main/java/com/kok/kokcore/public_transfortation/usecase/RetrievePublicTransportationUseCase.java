package com.kok.kokcore.public_transfortation.usecase;

public interface RetrievePublicTransportationUseCase {

    String retrievePublicTransportation(Long stationId, String roomId, String memberId);
    String retrieveComplexPublicTransportation(Long stationId, String roomId, String memberId);
}
