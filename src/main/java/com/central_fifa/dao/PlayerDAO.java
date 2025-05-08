package com.central_fifa.dao;

import com.central_fifa.config.DbConnection;
import com.central_fifa.model.Player;
import com.central_fifa.model.enums.PlayerPosition;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlayerDAO implements GenericDAO<Player> {
    private final DbConnection dbConnection;

    @Override
    public void save(Player player) {
        String sql = "INSERT INTO player (player_id, player_name, number, position, nationality, age, championship, scored_goals, playing_time_value, playing_time_duration_unit) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (player_id, championship) DO UPDATE SET " +
                "scored_goals = EXCLUDED.scored_goals, playing_time_value = EXCLUDED.playing_time_value, playing_time_duration_unit = EXCLUDED.playing_time_duration_unit";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, player.getId());
            preparedStatement.setString(2, player.getName());
            preparedStatement.setInt(3, player.getNumber());
            preparedStatement.setString(4, player.getPosition().name());
            preparedStatement.setString(5, player.getNationality());
            preparedStatement.setInt(6, player.getAge());
            preparedStatement.setString(7, player.getChampionship().name());
            preparedStatement.setInt(8, player.getScoredGoals());
            preparedStatement.setDouble(9, player.getPlayingTime().getValue());
            preparedStatement.setString(10, player.getPlayingTime().getDurationUnit().name());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving player: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Player> findById(String id) {
        return Optional.empty();
    }

    @Override
    public void delete(Player player) {

    }
}
