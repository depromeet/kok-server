package com.kok.kokapi.public_transportation.adapter.out.external;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
@EnableConfigurationProperties(TmapClientProperties.class)
public class PublicTransportationClient {

    private final RestClient restClient;
    private final TmapClientProperties properties;

    public PublicTransportationClient(TmapClientProperties properties) {
        this.properties = properties;
        this.restClient = getRestClient();
    }

    public RestClient getRestClient() {
        return RestClient.builder()
                .requestFactory(getRequestFactory())
                .defaultHeader(properties.keyname(), properties.key())  // API Key 추가
                .baseUrl(properties.url()) // Base URL 설정
                .build();
    }

    public RestClient getClient() {
        return this.restClient;
    }

    private ClientHttpRequestFactory getRequestFactory() {
        return ClientHttpRequestFactoryBuilder.detect()
                .build(ClientHttpRequestFactorySettings.defaults());
    }
}
