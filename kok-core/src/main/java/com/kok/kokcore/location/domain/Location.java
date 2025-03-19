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
                @UniqueConstraint(columnNames = {"room_id", "member_id"})
        })

public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomId;

    @Column(nullable = false)
    private String memberId;

    @Column(nullable = false, columnDefinition = "POINT SRID 4326")
    private Point location_point;


    public Location(String roomId, String memberId, Point location_point) {
        this.roomId = roomId;
        this.memberId = memberId;
        this.location_point = location_point;
    }

    // 더티체킹
    public void changePoint(Point point) {
        this.location_point = point;
    }
}
