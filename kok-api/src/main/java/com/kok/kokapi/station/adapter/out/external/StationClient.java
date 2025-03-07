package com.kok.kokapi.station.adapter.out.external;

import com.kok.kokapi.station.adapter.out.external.dto.StationResponses;
import com.kok.kokcore.station.application.port.out.LoadStationsPort;
import com.kok.kokcore.station.application.port.out.dto.StationRouteDtos;
import java.util.StringJoiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@EnableConfigurationProperties(StationClientProperties.class)
public class StationClient implements LoadStationsPort {

    private static final String DELIMITER = "/";

    private final RestClient restClient;
    private final StationClientProperties properties;
    private final StationErrorHandler stationErrorHandler;

    public StationClient(StationClientProperties properties, StationErrorHandler stationErrorHandler) {
        this.properties = properties;
        this.stationErrorHandler = stationErrorHandler;
        this.restClient = getRestClient();
    }

    public RestClient getRestClient() {
        return RestClient.builder()
            .requestFactory(getRequestFactory())
            .baseUrl(properties.baseUrl())
            .defaultStatusHandler(stationErrorHandler)
            .build();
    }

    private ClientHttpRequestFactory getRequestFactory() {
        return ClientHttpRequestFactoryBuilder.detect()
            .build(ClientHttpRequestFactorySettings.defaults());
    }

    @Override
    public StationRouteDtos loadAllStations() {
        StationResponses responses = restClient.get()
            .uri(getTargetUri())
            .retrieve()
            .body(StationResponses.class);
        log.debug("Seoul Data Open API Status Code: {}, Message: {}",
            responses.subwayStationMaster().result().code(),
            responses.subwayStationMaster().result().message()
        );
        return responses.toStationRouteDtos();
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
