package com.kok.kokcore.station.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Map;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    private static final Map<String, String> NORMALIZED_NAME_MAP = Map.ofEntries(
        Map.entry("경부선", "1호선"),
        Map.entry("경인선", "1호선"),
        Map.entry("경원선", "1호선"),
        Map.entry("장항선", "1호선"),
        Map.entry("과천선", "4호선"),
        Map.entry("7호선(인천)", "7호선"),
        Map.entry("9호선(연장)", "9호선"),
        Map.entry("신분당선(연장)", "신분당선"),
        Map.entry("신분당선(연장2)", "신분당선"),
        Map.entry("수인선", "수인분당선"),
        Map.entry("분당선", "수인분당선"),
        Map.entry("안산선", "수인분당선"),
        Map.entry("수도권 광역급행철도", "GTX"),
        Map.entry("공항철도1호선", "공항철도")
    );

    public static Route create(String rawName, Station station) {
        String normalized = NORMALIZED_NAME_MAP.getOrDefault(rawName, rawName);
        return new Route(normalized, station);
    }

    private Route(String name, Station station) {
        this.name = name;
        this.station = station;
    }
}
