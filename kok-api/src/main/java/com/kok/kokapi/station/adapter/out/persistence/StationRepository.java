package com.kok.kokapi.station.adapter.out.persistence;

import com.kok.kokcore.station.domain.entity.Station;
import io.lettuce.core.dynamic.annotation.Param;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StationRepository extends JpaRepository<Station, Long> {

    @Query("SELECT EXISTS (SELECT 1 FROM Station)")
    boolean existsAny();

    List<Station> findAllByNameIn(List<String> names);

    Optional<Station> findStationById(Long stationId);

    @Query(value = """
            SELECT * FROM station
            WHERE ST_Distance_Sphere(Point(longitude, latitude), Point(:lon, :lat)) < :distance
                AND priority > 0
        """, nativeQuery = true)
    List<Station> findInRangeStationsByCentroid(@Param("lon") BigDecimal lon,
        @Param("lat") BigDecimal lat, @Param("distance") Double distance);

    @Query(value = """
            SELECT * FROM station
            WHERE name LIKE CONCAT('%', :keyword, '%')
        """, nativeQuery = true)
    List<Station> findStationsByKeyword(@Param("keyword") String keyword);
}
