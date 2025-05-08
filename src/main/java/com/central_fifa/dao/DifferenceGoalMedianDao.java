package com.central_fifa.dao;

import com.central_fifa.config.DbConnection;
import com.central_fifa.model.DifferenceGoalMedian;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
public class DifferenceGoalMedianDao {
    private final DbConnection dbConnection;

    public void save(DifferenceGoalMedian median) {
        String sql = "INSERT INTO difference_goal_median (championship, difference_goal_median) " +
                "VALUES (?, ?) " +
                "ON CONFLICT (championship) DO UPDATE SET " +
                "difference_goal_median = EXCLUDED.difference_goal_median";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, median.getChampionship().name());
            preparedStatement.setInt(2, median.getMedian());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving difference goal median: " + e.getMessage(), e);
        }
    }
}
