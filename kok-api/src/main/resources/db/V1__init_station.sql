CREATE TABLE station
(
    id         BIGINT          NOT NULL AUTO_INCREMENT,
    station_id BIGINT          NOT NULL,
    name       VARCHAR(255)    NOT NULL,
    route      VARCHAR(255)    NOT NULL,
    latitude   DECIMAL(16, 14) NOT NULL,
    longitude  DECIMAL(17, 14) NOT NULL,
    priority BIGINT NOT NULL,
    PRIMARY KEY (id)
);
