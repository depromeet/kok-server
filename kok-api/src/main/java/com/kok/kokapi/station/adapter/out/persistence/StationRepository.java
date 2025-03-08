package com.kok.kokapi.station.adapter.out.persistence;

import com.kok.kokcore.station.domain.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {

    @Query("SELECT EXISTS (SELECT 1 FROM Station)")
    boolean existsAny();

    Optional<Station> findStationById(Long stationId);
}
