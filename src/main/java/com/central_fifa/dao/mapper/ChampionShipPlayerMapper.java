package com.central_fifa.dao.mapper;

import com.central_fifa.model.PlayingTime;
import com.central_fifa.model.centralModel.ChampionshipPlayer;
import com.central_fifa.model.enums.Championship;
import com.central_fifa.model.enums.DurationUnit;
import com.central_fifa.model.enums.PlayerPosition;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ChampionShipPlayerMapper implements Function<ResultSet, ChampionshipPlayer> {
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public ChampionshipPlayer apply(ResultSet resultSet) {
        ChampionshipPlayer championshipPlayer = new ChampionshipPlayer();
        championshipPlayer.setId(resultSet.getString("id"));
        championshipPlayer.setName(resultSet.getString("name"));
        championshipPlayer.setNumber(resultSet.getInt("number"));
        championshipPlayer.setPosition(PlayerPosition.valueOf(resultSet.getString("position")));
        championshipPlayer.setNationality(resultSet.getString("nationality"));
        championshipPlayer.setAge(resultSet.getInt("age"));
        championshipPlayer.setChampionship(Championship.valueOf(resultSet.getString("championship")));
        championshipPlayer.setAge(resultSet.getInt("scored_goals"));

        //playing time field for resultset
        PlayingTime playingTime = new PlayingTime();
        playingTime.setValue(resultSet.getDouble("playing_time_value"));
        playingTime.setDurationUnit(DurationUnit.valueOf(resultSet.getString("playing_time_duration_unit")));

        championshipPlayer.setPlayingTime(playingTime);

        return championshipPlayer;
    }
}
