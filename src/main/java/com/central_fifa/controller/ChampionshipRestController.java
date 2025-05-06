package com.central_fifa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChampionshipRestController {

    @GetMapping("/championshipRankings")
    public Object getChampionshipRankings() {
        return null;
    }
}
