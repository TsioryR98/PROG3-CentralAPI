package com.central_fifa.service;

import com.central_fifa.dao.*;
import com.central_fifa.dto.*;
import com.central_fifa.model.*;
import com.central_fifa.model.enums.Championship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class SynchronizationService {
    private static final Logger logger = LoggerFactory.getLogger(SynchronizationService.class);

    private final RestTemplate restTemplate;
    private final CoachDAO coachDAO;
    private final ClubDAO clubDAO;
    private final PlayerDAO playerDAO;
    private final SeasonDAO seasonDAO;
    private final MatchDAO matchDAO;
    private final GoalDAO goalDAO;
    private final PlayingTimeDAO playingTimeDAO;
    private final MatchScoreDAO matchScoreDAO;

    // API endpoints
    private static final Map<Integer, ApiEndpoint> API_ENDPOINTS = Map.of(
            8081, new ApiEndpoint("http://localhost:8081", Championship.LA_LIGA),
            8082, new ApiEndpoint("http://localhost:8082", Championship.LIGUE_1)
    );

    // Current season year - could be made configurable
    private static final int CURRENT_SEASON_YEAR = 2023;

    @Autowired
    public SynchronizationService(RestTemplate restTemplate,
                                  CoachDAO coachDAO,
                                  ClubDAO clubDAO,
                                  PlayerDAO playerDAO,
                                  SeasonDAO seasonDAO,
                                  MatchDAO matchDAO,
                                  GoalDAO goalDAO,
                                  PlayingTimeDAO playingTimeDAO,
                                  MatchScoreDAO matchScoreDAO) {
        this.restTemplate = restTemplate;
        this.coachDAO = coachDAO;
        this.clubDAO = clubDAO;
        this.playerDAO = playerDAO;
        this.seasonDAO = seasonDAO;
        this.matchDAO = matchDAO;
        this.goalDAO = goalDAO;
        this.playingTimeDAO = playingTimeDAO;
        this.matchScoreDAO = matchScoreDAO;
    }

    public Map<String, Object> synchronizeData() {
        Map<String, Object> result = new HashMap<>();

        // Liste des endpoints à tester
        List<String> endpoints = List.of(
                "/players",
                "/players/{id}/statistics/{seasonYear}",
                "/seasons",
                "/clubs",
                "/clubs/{id}/players",
                "/clubs/statistics/{seasonYear}",
                "/matches/{seasonYear}"
        );

        for (ApiEndpoint endpoint : API_ENDPOINTS.values()) {
            for (String path : endpoints) {
                try {
                    // Remplacement des paramètres dynamiques par des valeurs par défaut
                    String url = endpoint.getUrl() + path
                            .replace("{id}", "sample-id")
                            .replace("{seasonYear}", String.valueOf(CURRENT_SEASON_YEAR));

                    // Test de connexion
                    ResponseEntity<String> response = restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            null,
                            String.class
                    );
                    result.put(url, response.getStatusCode().is2xxSuccessful() ? "Connected" : "Failed");
                } catch (RestClientException e) {
                    result.put(endpoint.getUrl() + path, "Failed: " + e.getMessage());
                }
            }
        }

        return result;
    }

    // Helper class to store API endpoint information
    private static class ApiEndpoint {
        private final String url;
        private final Championship championship;

        public ApiEndpoint(String url, Championship championship) {
            this.url = url;
            this.championship = championship;
        }

        public String getUrl() {
            return url;
        }

        public Championship getChampionship() {
            return championship;
        }
    }
}
