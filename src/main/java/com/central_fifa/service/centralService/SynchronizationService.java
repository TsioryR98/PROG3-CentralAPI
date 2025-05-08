package com.central_fifa.service.centralService;

import com.central_fifa.dao.championshipOperations.*;
import com.central_fifa.model.Club;
import com.central_fifa.model.Player;
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

    // API endpoints
    private static final Map<Integer, ApiEndpoint> API_ENDPOINTS = Map.of(
            8081, new ApiEndpoint("http://localhost:8081/", Championship.LA_LIGA),
            8082, new ApiEndpoint("http://localhost:8082/", Championship.LIGUE_1)
    );

    @Autowired
    public SynchronizationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> synchronizeData() {
        Map<String, Object> result = new LinkedHashMap<>(); // LinkedHashMap pour garder l'ordre

        for (Map.Entry<Integer, ApiEndpoint> entry : API_ENDPOINTS.entrySet()) {
            String baseUrl = entry.getValue().getUrl();
            Championship championship = entry.getValue().getChampionship();
            String apiKey = "api-" + entry.getKey();

            try {
                // Récupérer les clubs
                List<Club> clubs = fetchClubs(baseUrl);
                clubs.forEach(club -> club.setChampionship(championship));
                result.put(apiKey + "-clubs", clubs);

                // Récupérer les joueurs
                List<Player> players = fetchPlayers(baseUrl);
                players.forEach(player -> player.setChampionship(championship));
                result.put(apiKey + "-players", players);

                // Récupérer la médiane des goals
                Integer median = fetchDifferenceGoalMedian(baseUrl);
                if (median != null) {
                    Map<String, Object> medianData = new HashMap<>();
                    medianData.put("value", median);
                    medianData.put("championship", championship);
                    result.put(apiKey + "-differenceGoalsMedian", medianData);
                }

            } catch (Exception e) {
                logger.error("Error synchronizing data from {}", baseUrl, e);
                result.put(apiKey + "-error", "Failed to fetch data: " + e.getMessage());
            }
        }

        return result;
    }

    private List<Club> fetchClubs(String baseUrl) {
        String url = baseUrl + "/championship/clubs";
        try {
            ResponseEntity<List<Club>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Club>>() {
                    }
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (RestClientException e) {
            logger.error("Failed to fetch clubs from {}", url, e);
            throw new RuntimeException("Failed to fetch clubs from " + url, e);
        }
    }

    private List<Player> fetchPlayers(String baseUrl) {
        String url = baseUrl + "/championship/players"; // Correction du endpoint
        try {
            ResponseEntity<List<Player>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Player>>() {
                    }
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (RestClientException e) {
            logger.error("Failed to fetch players from {}", url, e);
            throw new RuntimeException("Failed to fetch players from " + url, e);
        }
    }

    private Integer fetchDifferenceGoalMedian(String baseUrl) {
        String url = baseUrl + "/championship/differenceGoalsMedian";
        try {
            ResponseEntity<Integer> response = restTemplate.getForEntity(url, Integer.class);
            return response.getBody();
        } catch (RestClientException e) {
            logger.error("Failed to fetch difference goal median from {}", url, e);
            throw new RuntimeException("Failed to fetch difference goal median from " + url, e);
        }
    }

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
