package com.central_fifa.dao.mapper;

import com.central_fifa.model.PlayingTime;
import com.central_fifa.model.centralModel.ChampionshipPlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PlayingTimeMapper implements Function<ResultSet, PlayingTime> {
    @Override
    public PlayingTime apply(ResultSet resultSet) {
        return null;
    }
}
