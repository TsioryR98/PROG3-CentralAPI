CREATE TYPE championship AS ENUM ('PREMIER_LEAGUE', 'LA_LIGA', 'BUNDESLIGA', 'SERIA','LIGUE_1');

CREATE TABLE club
(
    club_id       UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    club_name     VARCHAR(200) UNIQUE NOT NULL,
    acronym       VARCHAR(5)          NOT NULL,
    year_creation INTEGER             NOT NULL,
    stadium       VARCHAR(200)        NOT NULL,
    championship championship NOT NULL,
    ranking_points INT DEFAULT 0,
    scored_goals INT DEFAULT 0,
    conceded_goals INT DEFAULT 0,
    difference_goals INT GENERATED ALWAYS AS (scored_goals - conceded_goals) STORED,
    clean_sheet_number INT DEFAULT 0
    coach_id      UUID REFERENCES coach (coach_id),
    UNIQUE (club_id, coach_id)
);
