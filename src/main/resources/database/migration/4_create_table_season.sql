CREATE TYPE season_status_enum AS ENUM ('NOT_STARTED', 'STARTED', 'FINISHED');

CREATE TABLE season
(
    season_id    UUID PRIMARY KEY            DEFAULT uuid_generate_v4(),
    alias        VARCHAR(200)       NOT NULL,
    status       season_status_enum NOT NULL DEFAULT 'NOT_STARTED',
    year         INTEGER UNIQUE     NOT NULL,
    championship championship_enum  NOT NULL,
    UNIQUE (year, championship),
    CHECK (year > 0)
);