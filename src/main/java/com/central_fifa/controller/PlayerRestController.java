package com.central_fifa.controller;

import com.central_fifa.model.PlayerRanking;
import com.central_fifa.model.enums.DurationUnit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class PlayerRestController {

    @GetMapping("/bestPlayers")
    public List<PlayerRanking> getBestPlayers(
            @RequestParam(required = false, defaultValue = "5") Integer top,
            @RequestParam(required = true) DurationUnit playingTimeUnit) {
        return null;
    }
}
