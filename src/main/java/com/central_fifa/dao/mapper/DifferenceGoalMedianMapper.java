package com.central_fifa.dao.mapper;

import com.central_fifa.model.DifferenceGoalMedian;
import com.central_fifa.model.enums.Championship;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.function.Function;

@Repository
@RequiredArgsConstructor
public class DifferenceGoalMedianMapper implements Function<ResultSet, DifferenceGoalMedian> {

    @Override
    @SneakyThrows
    public DifferenceGoalMedian apply(ResultSet resultSet) {
        DifferenceGoalMedian median = new DifferenceGoalMedian();
                median.setDifferenceGoalsMedian(resultSet.getInt("difference_goal_median"));
                median.setChampionship(Championship.valueOf(resultSet.getString("championship")));
        return median;
    }
}
