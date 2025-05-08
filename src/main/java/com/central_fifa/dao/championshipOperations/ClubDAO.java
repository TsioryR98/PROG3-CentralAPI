package com.central_fifa.dao.championshipOperations;

import com.central_fifa.config.DbConnection;
import com.central_fifa.model.Club;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
public class ClubDAO {
    private final DbConnection dbConnection;

    public void save(Club club) {
        String sql = """
                INSERT INTO club (club_id, club_name, championship, acronym, year_creation, stadium, coach_name, nationality, scored_goals, conceded_goals, difference_goals, clean_sheets)
                                VALUES (?::uuid, ?, ?::championship_enum, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                                ON CONFLICT (club_id, championship) DO UPDATE SET
                                scored_goals = EXCLUDED.scored_goals, conceded_goals = EXCLUDED.conceded_goals, difference_goals = EXCLUDED.difference_goals, clean_sheets = EXCLUDED.clean_sheets;
                """;


        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, club.getClub().getId());
            preparedStatement.setString(2, club.getClub().getName());
            preparedStatement.setString(3, club.getChampionship().name());
            preparedStatement.setString(4, club.getClub().getAcronym());
            preparedStatement.setInt(5, club.getClub().getYearCreation());
            preparedStatement.setString(6, club.getClub().getStadium());
            preparedStatement.setString(7, club.getClub().getCoach().getName());
            preparedStatement.setString(8, club.getClub().getCoach().getNationality());
            preparedStatement.setInt(9, club.getScoredGoals());
            preparedStatement.setInt(10, club.getConcededGoals());
            preparedStatement.setInt(11, club.getDifferenceGoals());
            preparedStatement.setInt(12, club.getCleanSheetNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving club: " + e.getMessage(), e);
        }
    }
}
