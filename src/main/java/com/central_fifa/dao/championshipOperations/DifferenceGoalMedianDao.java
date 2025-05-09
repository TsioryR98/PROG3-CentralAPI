package com.central_fifa.dao.championshipOperations;

import com.central_fifa.config.DbConnection;
import com.central_fifa.dao.mapper.DifferenceGoalMedianMapper;
import com.central_fifa.model.DifferenceGoalMedian;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DifferenceGoalMedianDao {
    private final DbConnection dbConnection;
    private final DifferenceGoalMedianMapper goalMedianMapper;

    public void saveFetchedGoalMedian(DifferenceGoalMedian median) {
        String sql = "INSERT INTO difference_goal_median (championship, difference_goal_median) " +
                "VALUES (?::championship_enum, ?) " +
                "ON CONFLICT (championship) DO UPDATE SET " +
                "difference_goal_median = EXCLUDED.difference_goal_median";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, median.getChampionship().name());
            preparedStatement.setInt(2, median.getDifferenceGoalsMedian());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving difference goal median: " + e.getMessage(), e);
        }
    }

    public List<DifferenceGoalMedian> findAllOrderedByMedian() {
        List<DifferenceGoalMedian> medians = new ArrayList<>();
        String sql = """
                SELECT championship, difference_goal_median
                FROM difference_goal_median
                ORDER BY difference_goal_median ASC
                """;
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                DifferenceGoalMedian median = goalMedianMapper.apply(resultSet);
                medians.add(median);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des médianes : " + e.getMessage(), e);
        }

        return medians;
    }
}
