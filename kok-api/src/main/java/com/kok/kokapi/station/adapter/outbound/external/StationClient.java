package com.kok.kokapi.station.adapter.outbound.external;

import com.kok.kokapi.station.adapter.outbound.external.dto.StationResponses;
import com.kok.kokcore.station.application.port.outbound.ReadStationsPort;
import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;
import java.util.StringJoiner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@EnableConfigurationProperties(StationClientProperties.class)
public class StationClient implements ReadStationsPort {

    private static final String DELIMITER = "/";

    private final RestClient restClient;
    private final StationClientProperties properties;

    public StationClient(StationClientProperties properties) {
        this.properties = properties;
        this.restClient = getRestClient();
    }

    public RestClient getRestClient() {
        return RestClient.builder()
            .requestFactory(getRequestFactory())
            .baseUrl(properties.baseUrl())
            .build();
    }

    private ClientHttpRequestFactory getRequestFactory() {
        return ClientHttpRequestFactoryBuilder.detect()
            .build(ClientHttpRequestFactorySettings.defaults());
    }

    @Override
    public List<Station> readAllStation() {
        StationResponses responses = restClient.get()
            .uri(getTargetUri())
            .retrieve()
            .body(StationResponses.class);
        return responses.toStations();
    }

    public String getTargetUri() {
        StringJoiner stringJoiner = new StringJoiner(DELIMITER);
        return stringJoiner.add(properties.secretKey())
            .add(properties.format())
            .add(properties.service())
            .add(properties.startIdx())
            .add(properties.endIdx())
            .toString();
    }
}
