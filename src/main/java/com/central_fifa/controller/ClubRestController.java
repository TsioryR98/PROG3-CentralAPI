package com.central_fifa.controller;

import com.central_fifa.model.ClubRanking;
import com.central_fifa.service.championshipService.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClubRestController {
    private final ClubService clubService;

    @GetMapping("/bestClubs")
    public List<ClubRanking> getBestClubs(
            @RequestParam(required = false, defaultValue = "5") Integer top) {
        if (top <= 0) {
            throw new IllegalArgumentException("La valeur de 'top' doit être supérieure à 0.");
        }
        return clubService.getBestClubs(top);
    }
}
