package com.central_fifa.controller.dto;

import com.central_fifa.model.Club;
import com.central_fifa.model.ClubMinimumInfo;
import com.central_fifa.model.ClubRanking;
import com.central_fifa.model.Coach;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClubRankingRestMapper {
    public ClubRanking mapToClubRanking(Club club, List<Club> clubs){
        return new ClubRanking(
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
        );
    }
    public Club mapToClubEntity( ClubDTO clubDTO){
        return new Club(
                clubDTO.getClub().getId(),
                clubDTO.getClub().getName(),
                clubDTO.getClub().getAcronym(),
                clubDTO.getClub().getYearCreation(),
                clubDTO.getClub().getStadium(),
                clubDTO.getClub().getCoach().getName(),
                clubDTO.getClub().getCoach().getNationality(),
                clubDTO.getScoredGoals(),
                clubDTO.getConcededGoals(),
                clubDTO.getDifferenceGoals(),
                clubDTO.getCleanSheetNumber(),
                null
        );
    }
}
