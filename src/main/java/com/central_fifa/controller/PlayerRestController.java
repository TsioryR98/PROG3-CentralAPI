package com.central_fifa.controller;

import com.central_fifa.model.PlayerRanking;
import com.central_fifa.model.enums.DurationUnit;
import com.central_fifa.service.centralService.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlayerRestController {
    private final PlayerService playerService;

    @GetMapping("/bestPlayers")
    public List<PlayerRanking> getBestPlayers(
            @RequestParam(required = false, defaultValue = "5") Integer top,
            @RequestParam(required = true) DurationUnit playingTimeUnit) {
        if (top <= 0) {
            throw new IllegalArgumentException("top should be greater than 0");
        }
        return playerService.getBestPlayers(top, playingTimeUnit);
    }
}
