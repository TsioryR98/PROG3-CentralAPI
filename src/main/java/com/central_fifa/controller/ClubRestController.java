package com.central_fifa.controller;

import com.central_fifa.controller.dto.ClubRankingRestMapper;
import com.central_fifa.model.Club;
import com.central_fifa.model.ClubRanking;
import com.central_fifa.service.championshipService.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ClubRestController {
    private final ClubService clubService;
    @Autowired
    private final ClubRankingRestMapper clubRankingRestMapper;

    @GetMapping("/bestClubs")
    public List<ClubRanking> getBestClubs(
            @RequestParam(required = false, defaultValue = "5") Integer top) {
        if (top <= 0) {
            throw new IllegalArgumentException("La valeur de 'top' doit être supérieure à 0.");
        }
        List<Club> clubs = clubService.getBestClubs(top);

                return clubs.stream()
                .map(club -> clubRankingRestMapper.mapToClubRanking(club,clubs))
                .collect(Collectors.toList());
    }
}
