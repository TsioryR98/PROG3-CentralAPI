CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE club
(
    club_id          UUID              NOT NULL,
    club_name        Varchar(200)      NOT NULL,
    championship     championship_enum NOT NULL,
    acronym          Varchar(10)       NOT NULL,
    year_creation    INTEGER           NOT NULL,
    stadium          Varchar(200)      NOT NULL,
    coach_name       Varchar(200)      NOT NULL,
    nationality      Varchar(100)      NOT NULL,
    scored_goals     Int DEFAULT 0,
    conceded_goals   Int DEFAULT 0,
    difference_goals Int DEFAULT 0,
    clean_sheets     Int DEFAULT 0,
    PRIMARY KEY (club_id, championship)
);