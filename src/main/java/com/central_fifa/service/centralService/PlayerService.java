package com.central_fifa.service.centralService;

import com.central_fifa.dao.championshipOperations.PlayerDAO;
import com.central_fifa.model.Player;
import com.central_fifa.model.PlayerRanking;
import com.central_fifa.model.PlayingTime;
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
        List<Player> players = playerDAO.findBestPlayers(top);

        return players.stream()
                .peek(player -> {
                    double convertedTime = switch (playingTimeUnit) {
                        case SECOND -> player.getPlayingTimeValue() * 60 * 60;
                        case MINUTE -> player.getPlayingTimeValue() * 60;
                        case HOUR -> player.getPlayingTimeValue();
                    };
                    player.setPlayingTimeValue(convertedTime);
                    player.setPlayingTimeDurationUnit(playingTimeUnit);
                })
                .map(player -> new PlayerRanking(
                        players.indexOf(player) + 1,
                        player.getId(),
                        player.getName(),
                        player.getNumber(),
                        player.getPosition(),
                        player.getNationality(),
                        player.getAge(),
                        player.getChampionship(),
                        player.getScoredGoals(),
                        new PlayingTime(player.getPlayingTimeValue(), player.getPlayingTimeDurationUnit())
                ))
                .collect(Collectors.toList());
    }
}