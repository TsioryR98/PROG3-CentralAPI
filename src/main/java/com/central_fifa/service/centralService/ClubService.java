package com.central_fifa.service.centralService;


import com.central_fifa.config.DbConnection;
import com.central_fifa.dao.championshipOperations.ClubDAO;
import com.central_fifa.dao.championshipOperations.PlayerDAO;
import com.central_fifa.model.*;
import com.central_fifa.model.enums.Championship;
import com.central_fifa.model.enums.DurationUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubDAO clubDAO;
    private final DbConnection dbConnection;

    public List<Club> findBestClubs(int top) {
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
                    c.clean_sheets AS cleanSheetNumber,
                    c.ranking_points AS rankingPoints
                FROM club c
                ORDER BY c.ranking_points DESC, c.difference_goals DESC, c.clean_sheets DESC
                LIMIT ?
                """;

        List<Club> clubs = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, top);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Club club = new Club(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            resultSet.getString("acronym"),
                            resultSet.getInt("yearCreation"),
                            resultSet.getString("stadium"),
                            resultSet.getString("coachName"),
                            resultSet.getString("coachNationality"),
                            resultSet.getInt("scoredGoals"),
                            resultSet.getInt("concededGoals"),
                            resultSet.getInt("differenceGoals"),
                            resultSet.getInt("cleanSheetNumber"),
                            Championship.valueOf(resultSet.getString("championship"))
                    );
                    clubs.add(club);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching best clubs: " + e.getMessage(), e);
        }

        return clubs;
    }
}