package com.central_fifa.dao.championshipOperations;

import com.central_fifa.config.DbConnection;
import com.central_fifa.dao.mapper.ClubMapper;
import com.central_fifa.model.Club;
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
public class ClubDAO {
    private final DbConnection dbConnection;
    private ClubMapper clubMapper;

    public void saveFetchedClubIntoCentral(Club club) {
        String sql = """
                INSERT INTO club (club_id, club_name, championship, acronym, year_creation, stadium, coach_name, nationality, scored_goals, conceded_goals, difference_goals, clean_sheets)
                                VALUES (?::uuid, ?, ?::championship_enum, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                                ON CONFLICT (club_id, championship) DO UPDATE SET
                                scored_goals = EXCLUDED.scored_goals, conceded_goals = EXCLUDED.conceded_goals, difference_goals = EXCLUDED.difference_goals, clean_sheets = EXCLUDED.clean_sheets;
                """;
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, club.getId());
            preparedStatement.setString(2, club.getName());
            preparedStatement.setString(3, club.getChampionship().name());
            preparedStatement.setString(4, club.getAcronym());
            preparedStatement.setInt(5, club.getYearCreation());
            preparedStatement.setString(6, club.getStadium());
            preparedStatement.setString(7, club.getCoachName());
            preparedStatement.setString(8, club.getCoachNationality());
            preparedStatement.setInt(9, club.getScoredGoals());
            preparedStatement.setInt(10, club.getConcededGoals());
            preparedStatement.setInt(11, club.getDifferenceGoals());
            preparedStatement.setInt(12, club.getCleanSheetNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving club: " + e.getMessage(), e);
        }
    }

    public List<Club> findBestClubs(int top) {
        List<Club> clubs = new ArrayList<>();
        String sql = """
                SELECT
                    c.club_id AS id,
                    c.club_name AS name,
                    c.acronym,
                    c.year_creation AS yearCreation,
                    c.stadium,
                    c.coach_name AS coachName,
                    c.nationality AS coachNationality,
                    c.championship,
                    c.scored_goals AS scoredGoals,
                    c.conceded_goals AS concededGoals,
                    c.difference_goals AS differenceGoals,
                    c.clean_sheets AS cleanSheetNumber
                FROM club c
                ORDER BY c.scored_goals DESC, c.difference_goals DESC, c.clean_sheets DESC
                LIMIT ?
                """;
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, top);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Club club = clubMapper.apply(resultSet);
                    clubs.add(club);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des meilleurs clubs : " + e.getMessage(), e);
        }

        return clubs;
    }
}
