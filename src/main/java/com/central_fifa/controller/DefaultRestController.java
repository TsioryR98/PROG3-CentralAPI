package com.central_fifa.controller;

import com.central_fifa.service.centralService.SynchronizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DefaultRestController {

    private final SynchronizationService synchronizationService;

    @Autowired
    public DefaultRestController(SynchronizationService synchronizationService) {
        this.synchronizationService = synchronizationService;
    }

    @PostMapping("/synchronization")
    public Map<String, Object> synchronize() {
        return synchronizationService.synchronizeData();
    }
}
