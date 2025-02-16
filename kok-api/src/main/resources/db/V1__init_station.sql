CREATE TABLE station (
                         latitude DECIMAL(16, 14) NOT NULL,
                         longitude DECIMAL(17, 14) NOT NULL,
                         id BIGINT NOT NULL,
                         name VARCHAR(255) NOT NULL,
                         route VARCHAR(255) NOT NULL,
                         PRIMARY KEY (id)
);
