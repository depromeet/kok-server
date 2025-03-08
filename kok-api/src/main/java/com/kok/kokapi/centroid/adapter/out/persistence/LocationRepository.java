package com.kok.kokapi.centroid.adapter.out.persistence;

import com.kok.kokcore.location.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    /* 해당 쿼리는 특정 UUID를 기준으로 위치 데이터를 조회하고, 해당 위치들의 중심점을 계산합니다.
       먼저, SRID 4326 (위도/경도) 좌표계를 사용하는 데이터를 SRID 3857 (평면 좌표계)로 변환합니다.
       변환된 좌표들을 ST_Collect를 이용해 하나의 MULTIPOINT 형태로 병합합니다.
       병합된 MULTIPOINT에서 ST_Centroid를 사용해 중심점을 계산합니다.
       계산된 중심점을 다시 SRID 4326 (위도/경도)으로 변환합니다. (지도 API와 호환성 유지)
       최종적으로 ST_AsText를 사용하여 결과를 WKT(Well-Known Text) 포맷으로 반환합니다. */

    @Query(value = """
    SELECT ST_AsText(
        ST_Transform(
            ST_Centroid(
                ST_Collect(
                    ST_Transform(location_point, 3857))),4326)
               )
    FROM location
    WHERE uuid = :uuid
    """, nativeQuery = true)
    String findCentroidByUuid(@Param("uuid") String uuid);

    Optional<Location> findLocationByUuidAndMemberId(String uuid, Integer memberId);

    List<Location> findLocationsByUuid(String uuid);

    @Query(value = """
    WITH ConvexHull AS (
        SELECT ST_ConvexHull(ST_Collect(ST_GeomFromText(ST_AsText(location_point)))) AS hull
        FROM location
        WHERE uuid = :uuid
    )
    SELECT l.*
    FROM location l, ConvexHull ch
    WHERE l.uuid = :uuid
        AND ST_Contains(ch.hull, ST_GeomFromText(ST_AsText(l.location_point)))
    """, nativeQuery = true)
    List<Location> findInsideConvexHull(@Param("uuid") String uuid);

    @Query(value = """
    WITH ConvexHull AS (
        SELECT ST_ConvexHull(ST_Collect(ST_GeomFromText(ST_AsText(location_point)))) AS hull,
               ST_Centroid(ST_ConvexHull(ST_Collect(ST_GeomFromText(ST_AsText(location_point))))) AS center
        FROM location
        WHERE uuid = :uuid
    )
    SELECT l.*,
           ATAN2(
               ST_Y(ST_GeomFromText(ST_AsText(l.location_point))) - ST_Y(ch.center),
               ST_X(ST_GeomFromText(ST_AsText(l.location_point))) - ST_X(ch.center)
           ) AS angle
    FROM location l, ConvexHull ch
    WHERE l.uuid = :uuid
        AND NOT ST_Contains(ch.hull, ST_GeomFromText(ST_AsText(l.location_point)))
    ORDER BY angle
    """, nativeQuery = true)
    List<Location> findConvexHull(@Param("uuid") String uuid);

}
