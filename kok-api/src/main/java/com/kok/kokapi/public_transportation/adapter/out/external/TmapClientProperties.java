package com.kok.kokapi.public_transportation.adapter.out.external;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tmap-sub")
public record TmapClientProperties(
        String key,
        String url,
        String keyname
){
}
