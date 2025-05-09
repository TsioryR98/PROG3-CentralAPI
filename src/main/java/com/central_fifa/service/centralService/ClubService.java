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

    public List<ClubRanking> getBestClubs(int top) {
        List<Club> clubs = clubDAO.findBestClubs(top);

        return clubs.stream()
                .map(club -> new ClubRanking(
                        clubs.indexOf(club) + 1,
                        new ClubMinimumInfo(
                                club.getId(),
                                club.getName(),
                                club.getAcronym(),
                                club.getYearCreation(),
                                club.getStadium(),
                                new Coach(club.getCoachName(), club.getCoachNationality())
                        ),
                        club.getScoredGoals() + club.getDifferenceGoals(),
                        club.getScoredGoals(),
                        club.getConcededGoals(),
                        club.getDifferenceGoals(),
                        club.getCleanSheetNumber()
                ))
                .collect(Collectors.toList());
    }
}