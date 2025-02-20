package com.kok.kokcore.station.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long stationId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String route;
    @Column(nullable = false, columnDefinition = "DECIMAL(16, 14)")
    private BigDecimal latitude;
    @Column(nullable = false, columnDefinition = "DECIMAL(17, 14)")
    private BigDecimal longitude;

    public Station(Long id, Long stationId, String name, String route, BigDecimal latitude,
        BigDecimal longitude) {
        this.id = id;
        this.stationId = stationId;
        this.name = name;
        this.route = route;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Station(Long stationId, String name, String route, String latitude, String longitude) {
        this.stationId = stationId;
        this.name = name;
        this.route = route;
        this.latitude = new BigDecimal(latitude);
        this.longitude = new BigDecimal(longitude);
    }
}
