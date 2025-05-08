package com.central_fifa.service.centralService;

import com.central_fifa.dao.championshipOperations.PlayerDAO;
import com.central_fifa.model.PlayerRanking;
import com.central_fifa.model.enums.DurationUnit;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private final PlayerDAO playerDAO;

    public PlayerService(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    public List<PlayerRanking> getBestPlayers(int top, DurationUnit playingTimeUnit) {
        List<PlayerRanking> players = playerDAO.findBestPlayers(top);

        return players.stream()
                .peek(player -> {
                    double convertedTime = switch (playingTimeUnit) {
                        case SECOND -> player.getPlayingTime().getValue() * 60 * 60;
                        case MINUTE -> player.getPlayingTime().getValue() * 60;
                        case HOUR -> player.getPlayingTime().getValue();
                    };
                    player.getPlayingTime().setValue(convertedTime);
                    player.getPlayingTime().setDurationUnit(playingTimeUnit);
                })
                .collect(Collectors.toList());
    }
}