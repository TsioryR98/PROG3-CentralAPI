package com.central_fifa.controller;

import com.central_fifa.controller.dto.PlayerRankingRestMapper;
import com.central_fifa.model.Player;
import com.central_fifa.model.PlayerRanking;
import com.central_fifa.model.enums.DurationUnit;
import com.central_fifa.service.championshipService.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PlayerRestController {
    private final PlayerService playerService;
    @Autowired
    private PlayerRankingRestMapper playerRankingRestMapper;

    @GetMapping("/bestPlayers")
    public List<PlayerRanking> getBestPlayers(
            @RequestParam(required = false, defaultValue = "5") Integer top,
            @RequestParam(required = true) DurationUnit playingTimeUnit) {
        if (top <= 0) {
            throw new IllegalArgumentException("top should be greater than 0");
        }

        List<Player> players = playerService.getBestPlayers(top, playingTimeUnit);
        return players.stream()
                .map(player -> playerRankingRestMapper.mapToPlayerRanking(player, players))
                .collect(Collectors.toList());
    }
}
