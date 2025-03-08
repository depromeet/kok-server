package com.kok.kokcore.location.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "location" ,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"uuid", "member_id"})
        })

public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private Integer memberId;

    @Column(nullable = false, columnDefinition = "POINT SRID 4326")
    private Point location_point;


    public Location(String uuid, Integer memberId, Point location_point) {
        this.uuid = uuid;
        this.memberId = memberId;
        this.location_point = location_point;
    }

    // 더티체킹
    public void changePoint(Point point) {
        this.location_point = point;
    }
}
