CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE championship_enum AS ENUM ('PREMIER_LEAGUE', 'LA_LIGA', 'BUNDESLIGA', 'SERIA', 'LIGUE_1');

CREATE TABLE coach
(
    coach_id     UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    coach_name   Varchar(200)      NOT NULL,
    nationality  Varchar(100)      NOT NULL,
    championship championship_enum NOT NULL
)
