package com.kok.kokapi.common.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = {"com.kok.kokcore"})
public class JpaConfig {

}
