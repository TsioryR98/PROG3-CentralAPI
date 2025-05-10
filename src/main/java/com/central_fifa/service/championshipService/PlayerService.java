package com.central_fifa.service.championshipService;

import com.central_fifa.controller.dto.PlayerRankingRestMapper;
import com.central_fifa.dao.championshipOperations.PlayerDAO;
import com.central_fifa.model.Player;
import com.central_fifa.model.PlayerRanking;
import com.central_fifa.model.PlayingTime;
import com.central_fifa.model.enums.DurationUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerDAO playerDAO;
    @Autowired
    private PlayerRankingRestMapper playerRankingRestMapper;

    private void convertPlayingTimeValue(Player player, DurationUnit targetPlayingTimeUnit){
        //Convert method
        double convertedTime = switch (targetPlayingTimeUnit) {
            case SECOND -> player.getPlayingTimeValue() * 3600; //sec
            case MINUTE -> player.getPlayingTimeValue() * 60;
            case HOUR -> player.getPlayingTimeValue();
        };
        player.setPlayingTimeValue(convertedTime);
        player.setPlayingTimeDurationUnit(targetPlayingTimeUnit);
    }

    public List<PlayerRanking> getBestPlayers(int top, DurationUnit playingTimeUnit) {
        List<Player> players = playerDAO.findBestPlayers(top);
        //convert
        players.forEach(player -> convertPlayingTimeValue(player,playingTimeUnit));
        return players.stream()
                .map(player -> playerRankingRestMapper.mapToPlayerRanking(player, players))
                .collect(Collectors.toList());
    }
}