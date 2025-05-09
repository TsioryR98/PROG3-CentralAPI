package com.central_fifa.dao.mapper;

import com.central_fifa.model.Player;
import com.central_fifa.model.PlayingTime;
import com.central_fifa.model.centralModel.ChampionshipPlayer;
import com.central_fifa.model.enums.Championship;
import com.central_fifa.model.enums.DurationUnit;
import com.central_fifa.model.enums.PlayerPosition;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PlayerMapper implements Function<ResultSet, Player> {

    @SneakyThrows
    @Override
    public Player apply(ResultSet resultSet) {
        Player championshipPlayer = new Player();
        championshipPlayer.setId(resultSet.getString("id"));
        championshipPlayer.setName(resultSet.getString("name"));
        championshipPlayer.setNumber(resultSet.getInt("number"));
        championshipPlayer.setPosition(PlayerPosition.valueOf(resultSet.getString("position")));
        championshipPlayer.setNationality(resultSet.getString("nationality"));
        championshipPlayer.setAge(resultSet.getInt("age"));
        championshipPlayer.setChampionship(Championship.valueOf(resultSet.getString("championship")));
        championshipPlayer.setAge(resultSet.getInt("scored_goals"));
        //playing time field for resultset
        championshipPlayer.setPlayingTimeValue(resultSet.getDouble("value"));
        championshipPlayer.setPlayingTimeDurationUnit(DurationUnit.valueOf(resultSet.getString("durationUnit")));

        return championshipPlayer;

    }
}
