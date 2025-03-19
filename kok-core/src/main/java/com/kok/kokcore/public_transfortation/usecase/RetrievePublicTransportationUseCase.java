package com.kok.kokcore.public_transfortation.usecase;

public interface RetrievePublicTransportationUseCase {

    String retrievePublicTransportation(Long stationId, String UUID, String memberId);
    String retrieveComplexPublicTransportation(Long stationId, String UUID, String memberId);
}
