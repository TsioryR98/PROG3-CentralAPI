package com.central_fifa.controller;

import com.central_fifa.model.ClubRanking;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class ClubRestController {

    @GetMapping("/bestClubs")
    public List<ClubRanking> getBestClubs(
            @RequestParam(required = false, defaultValue = "5") Integer top) {
        return null;
    }
}
