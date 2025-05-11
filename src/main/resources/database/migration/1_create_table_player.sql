CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE championship_enum AS ENUM ('PREMIER_LEAGUE', 'LA_LIGA', 'BUNDESLIGA', 'SERIA', 'LIGUE_1');
CREATE TYPE duration_unit_enum AS ENUM ('SECOND', 'MINUTE', 'HOUR');
CREATE TYPE position_enum AS ENUM ('STRIKER', 'MIDFIELDER', 'DEFENSE', 'GOAL_KEEPER');

CREATE TABLE player
(
    player_id UUID NOT NULL ,
    player_name Varchar(200)      NOT NULL,
    number      Int               NOT NULL,
    position    position_enum     NOT NULL,
    nationality Varchar(100)      NOT NULL,
    age         Int               NOT NULL,
    championship championship_enum NOT NULL,
    scored_goals Int DEFAULT 0,
    playing_time_value DOUBLE PRECISION DEFAULT 0,
    playing_time_duration_unit duration_unit_enum DEFAULT 'MINUTE',
    PRIMARY KEY (player_id, championship)
);