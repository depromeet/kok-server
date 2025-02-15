package com.kok.kokapi.station.adapter.outbound.external;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "station")
public record StationClientProperties(
    String baseUrl,
    String secretKey,
    String format,
    String service,
    String startIdx,
    String endIdx
) {

}
