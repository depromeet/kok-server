package com.kok.kokapi.station.adapter.out.persistence;

import com.kok.kokcore.station.domain.entity.Route;
import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RouteRepository extends JpaRepository<Route, Long> {

    List<Route> findAllByStationOrderByName(Station station);

    @Modifying
    @Query("UPDATE Route r SET r.name = '1호선' WHERE r.name IN :names")
    int updateRouteNameToRouteOne(List<String> names);
}
