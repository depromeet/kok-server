CREATE TABLE station (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         name VARCHAR(20) NOT NULL,
                         latitude DECIMAL(16, 14) NOT NULL,
                         longitude DECIMAL(17, 14) NOT NULL,
                         priority BIGINT NOT NULL,
                         PRIMARY KEY (id)
);

CREATE TABLE route (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       code BIGINT NOT NULL,
                       station_id BIGINT NOT NULL,
                       name VARCHAR(20) NOT NULL,
                       PRIMARY KEY (id),
                       FOREIGN KEY (station_id) REFERENCES station(id)
);