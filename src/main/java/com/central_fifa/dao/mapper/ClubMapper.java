package com.central_fifa.dao.mapper;

import com.central_fifa.model.Club;
import com.central_fifa.model.enums.Championship;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class ClubMapper implements Function<ResultSet , Club> {
    @Override
    @SneakyThrows
    public Club apply(ResultSet resultSet) {
        Club club = new Club();
                club.setId(resultSet.getString("id"));
                club.setName(resultSet.getString("name"));
                club.setAcronym(resultSet.getString("acronym"));
                club.setYearCreation(resultSet.getInt("yearCreation"));
                club.setStadium(resultSet.getString("stadium"));
                club.setCoachName(resultSet.getString("coachName"));
                club.setCoachNationality(resultSet.getString("coachNationality"));
                club.setScoredGoals(resultSet.getInt("scoredGoals"));
                club.setConcededGoals(resultSet.getInt("concededGoals"));
                club.setDifferenceGoals(resultSet.getInt("differenceGoals"));
                club.setCleanSheetNumber(resultSet.getInt("cleanSheetNumber"));
                club.setChampionship(Championship.valueOf(resultSet.getString("championship")));
        return club;
    }



}
