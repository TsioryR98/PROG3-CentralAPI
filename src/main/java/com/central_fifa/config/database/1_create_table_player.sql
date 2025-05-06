CREATE TYPE positions AS ENUM ('STRIKER', 'MIDFIELDER', 'DEFENSE', 'GOAL_KEEPER');

CREATE TABLE player
(
    player_id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_name Varchar(200)  NOT NULL,
    number      INT           NOT NULL,
    position    positions NOT NULL,
    nationality Varchar(100)  NOT NULL,
    age         INT           NOT NULL,
    scored_goals INT DEFAULT 0,
    playing_time_seconds DOUBLE PRECISION DEFAULT 0,
    club_id     UUID REFERENCES club (club_id),
    UNIQUE (number, club_id)
)

