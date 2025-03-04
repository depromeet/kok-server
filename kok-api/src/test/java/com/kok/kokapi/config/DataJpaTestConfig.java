package com.kok.kokapi.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
@EntityScan(basePackages = "com.kok.kokcore")
public class DataJpaTestConfig {

}
