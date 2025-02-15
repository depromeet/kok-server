package com.kok.kokcore.station.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Station {

    @Id
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String route;
    @Column(nullable = false, columnDefinition = "DECIMAL(16, 14)")
    private BigDecimal latitude;
    @Column(nullable = false, columnDefinition = "DECIMAL(17, 14)")
    private BigDecimal longitude;

    public Station(long id, String name, String route, BigDecimal latitude, BigDecimal longitude) {
        this.id = id;
        this.name = name;
        this.route = route;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Station(long id, String name, String route, String latitude, String longitude) {
        this(id, name, route, new BigDecimal(latitude), new BigDecimal(longitude));
    }
}
