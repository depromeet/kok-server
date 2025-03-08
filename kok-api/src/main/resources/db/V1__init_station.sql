CREATE TABLE station
(
    id        BIGINT          NOT NULL AUTO_INCREMENT,
    name      VARCHAR(20)     NOT NULL,
    latitude  DECIMAL(16, 14) NOT NULL,
    longitude DECIMAL(17, 14) NOT NULL,
    priority  BIGINT          NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE route
(
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    code       BIGINT      NOT NULL,
    station_id BIGINT      NOT NULL,
    name       VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (station_id) REFERENCES station (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

create table location
(
    id        bigint auto_increment primary key,
    member_id int          not null,
    point     point        not null,
    uuid      varchar(255) not null,
    constraint UKrgpajb4rsivb4gj9xn2qowgw6
        unique (uuid, member_id)
);
