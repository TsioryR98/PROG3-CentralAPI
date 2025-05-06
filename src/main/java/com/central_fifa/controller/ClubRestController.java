package com.central_fifa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClubRestController {

    @GetMapping("/bestClubs")
    public Object getBestClubs(
            @RequestParam(required = false, defaultValue = "5") Integer top) {
        return null;
    }
}
