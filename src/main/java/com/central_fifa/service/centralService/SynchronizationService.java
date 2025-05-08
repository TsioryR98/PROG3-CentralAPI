package com.central_fifa.service.centralService;

import com.central_fifa.dao.championshipOperations.*;
import com.central_fifa.model.enums.Championship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final ClubDAO clubDAO;
    private final PlayerDAO playerDAO;
    private final DifferenceGoalMedianDao differenceGoalMedianDao;

    @Autowired
    public SynchronizationService(RestTemplate restTemplate, ClubDAO clubDAO, PlayerDAO playerDAO, DifferenceGoalMedianDao differenceGoalMedianDao) {
        this.restTemplate = restTemplate;
        this.clubDAO = clubDAO;
        this.playerDAO = playerDAO;
        this.differenceGoalMedianDao = differenceGoalMedianDao;
    }

    public Map<String, Object> synchronizeData() {
        Map<String, Object> result = new LinkedHashMap<>();

        for (Map.Entry<Integer, ApiEndpoint> entry : API_ENDPOINTS.entrySet()) {
            String baseUrl = entry.getValue().getUrl();
            Championship championship = entry.getValue().getChampionship();
            String apiKey = "api-" + entry.getKey();

            try {
                // Récupérer et insérer les clubs
                List<Club> clubs = fetchClubs(baseUrl);
                clubs.forEach(club -> {
                    club.setChampionship(championship);
                    clubDAO.save(club);
                });
                result.put(apiKey + "-clubs", clubs);

                // Récupérer et insérer les joueurs
                List<Player> players = fetchPlayers(baseUrl);
                players.forEach(player -> {
                    player.setChampionship(championship);
                    playerDAO.save(player);
                });
                result.put(apiKey + "-players", players);

                // Récupérer et insérer la médiane des goals
                Integer median = fetchDifferenceGoalMedian(baseUrl);
                if (median != null) {
                    DifferenceGoalMedian medianModel = new DifferenceGoalMedian(median, championship);
                    differenceGoalMedianDao.save(medianModel);
                    result.put(apiKey + "-differenceGoalsMedian", medianModel);
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
