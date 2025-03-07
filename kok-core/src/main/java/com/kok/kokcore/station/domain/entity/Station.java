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
    @Column(nullable = false, length = 15)
    private String name;
    @Column(nullable = false, columnDefinition = "DECIMAL(16, 14)")
    private BigDecimal latitude;
    @Column(nullable = false, columnDefinition = "DECIMAL(17, 14)")
    private BigDecimal longitude;
    @Column(nullable = false)
    private Long priority;

    public Station(String name, BigDecimal latitude, BigDecimal longitude, long priority) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.priority = priority;
    }

    public Station(String name,String latitude, String longitude) {
        this(name, new BigDecimal(latitude),new BigDecimal(longitude), 0);
    }
}
