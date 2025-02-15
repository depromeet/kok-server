package com.kok.kokapi.station.adapter.outbound.persistence;

import com.kok.kokcore.station.domain.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {

}
