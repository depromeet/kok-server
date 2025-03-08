package com.kok.kokcore.public_transfortation.usecase;

public interface RetrievePublicTransportationUsecase {

    String retrievePublicTransportation(Long stationId, String UUID, Integer memberId);
    String retrieveComplexPublicTransportation(Long stationId, String UUID, Integer memberId);
}
