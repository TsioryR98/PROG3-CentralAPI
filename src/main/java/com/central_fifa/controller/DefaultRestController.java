package com.central_fifa.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultRestController {

    @PostMapping("/syncrhonization")
    public Object synchronize() {
        return null;
    }
}
