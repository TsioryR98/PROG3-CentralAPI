package com.central_fifa.controller;

import com.central_fifa.model.ChampionshipRanking;
import com.central_fifa.service.championshipService.DifferenceGoalMedianService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChampionshipRestController {
    private final DifferenceGoalMedianService differenceGoalMedianService;

    @GetMapping("/championshipRankings")
    public List<ChampionshipRanking> getChampionshipRankings() {
        return differenceGoalMedianService.getChampionshipRankings();
    }
}
