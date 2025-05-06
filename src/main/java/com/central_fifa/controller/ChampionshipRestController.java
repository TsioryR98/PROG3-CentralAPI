package com.central_fifa.controller;

import com.central_fifa.model.ChampionshipRanking;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChampionshipRestController {

    @GetMapping("/championshipRankings")
    public ChampionshipRanking getChampionshipRankings() {
        return null;
    }
}
