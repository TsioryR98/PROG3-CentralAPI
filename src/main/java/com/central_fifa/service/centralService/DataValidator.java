package com.central_fifa.service.centralService;

import com.central_fifa.model.Club;
import com.central_fifa.model.DifferenceGoalMedian;
import com.central_fifa.model.Player;
import org.springframework.stereotype.Component;

@Component
public class DataValidator {

    public boolean isValidClub(Club club) {
        return club != null &&
                club.getClub() != null &&
                club.getClub().getId() != null &&
                club.getClub().getName() != null &&
                club.getChampionship() != null;
    }

    public boolean isValidPlayer(Player player) {
        return player != null &&
                player.getId() != null &&
                player.getName() != null &&
                player.getPosition() != null &&
                player.getChampionship() != null;
    }

    public boolean isValidDifferenceGoalMedian(DifferenceGoalMedian median) {
        return median != null &&
                median.getChampionship() != null;
    }
}
