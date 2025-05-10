package com.central_fifa.dao.championshipOperations;

import com.central_fifa.config.DbConnection;
import com.central_fifa.dao.mapper.PlayerMapper;
import com.central_fifa.model.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlayerDAO {
    private final DbConnection dbConnection;
    @Autowired
    private PlayerMapper playerMapper;

    public void saveFetchedPlayerIntoClub(Player player) {
        String sql = """
                INSERT INTO player (player_id, player_name, number, position, nationality, age, championship, scored_goals, playing_time_value, playing_time_duration_unit)
                                VALUES (?::uuid, ?, ?, ?::position_enum, ?, ?, ?::championship_enum, ?, ?, ?::duration_unit_enum)
                                ON CONFLICT (player_id, championship) DO UPDATE SET
                                scored_goals = EXCLUDED.scored_goals, playing_time_value = EXCLUDED.playing_time_value, playing_time_duration_unit = EXCLUDED.playing_time_duration_unit
                """;

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
            preparedStatement.setDouble(9, player.getPlayingTimeValue());
            preparedStatement.setString(10, player.getPlayingTimeDurationUnit().name());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving player: " + e.getMessage(), e);
        }
    }

    public List<Player> findBestPlayers(int top) {
        List<Player> players = new ArrayList<>();
        String sql = """
                SELECT
                    p.player_id AS id,
                    p.player_name AS name,
                    p.number,
                    p.position,
                    p.nationality,
                    p.age,
                    p.championship,
                    p.scored_goals,
                    p.playing_time_value,
                    p.playing_time_duration_unit
                FROM player p
                ORDER BY p.scored_goals DESC, p.playing_time_value DESC
                LIMIT ?
                """;
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, top);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Player player = playerMapper.apply(resultSet);
                    players.add(player);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching best players: " + e.getMessage(), e);
        }
        return players;
    }
}
