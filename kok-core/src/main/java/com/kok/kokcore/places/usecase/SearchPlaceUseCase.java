package com.kok.kokcore.places.usecase;

import com.kok.kokcore.places.port.in.PlaceInput;
import com.kok.kokcore.places.domain.model.PlacesResult;

/**
 * 주변 장소 검색 유스케이스
 * PlaceInput을 받아 검색 결과(PlacesResult)를 반환합니다.
 */
public interface SearchPlaceUseCase {
    PlacesResult getPlaces(PlaceInput input) throws Exception;
}