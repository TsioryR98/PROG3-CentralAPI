package com.central_fifa.service.centralService;

import com.central_fifa.controller.dto.ClubRankingRestMapper;
import com.central_fifa.controller.dto.PlayerRankingRestMapper;
import com.central_fifa.dao.championshipOperations.DifferenceGoalMedianDao;
import com.central_fifa.dao.championshipOperations.PlayerDAO;
import com.central_fifa.dao.championshipOperations.*;
import com.central_fifa.controller.dto.ClubDTO;
import com.central_fifa.controller.dto.PlayerDTO;
import com.central_fifa.model.Club;
import com.central_fifa.model.DifferenceGoalMedian;
import com.central_fifa.model.Player;
import com.central_fifa.model.enums.Championship;
import lombok.RequiredArgsConstructor;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor //inject dep
public class SynchronizationService {
    private static final Logger logger = LoggerFactory.getLogger(SynchronizationService.class);

    private final RestTemplate restTemplate;
    private final ClubDAO clubDAO;
    private final PlayerDAO playerDAO;
    private final DifferenceGoalMedianDao differenceGoalMedianDao;
    private final DataValidator dataValidator;
    private final ClubRankingRestMapper clubRankingRestMapper;
    private final PlayerRankingRestMapper playerRankingRestMapper;

    // API endpoints
    private static final Map<Integer, ApiEndpoint> API_ENDPOINTS = Map.of(
            8081, new ApiEndpoint("http://localhost:8081/", Championship.LA_LIGA),
            8082, new ApiEndpoint("http://localhost:8082/", Championship.LIGUE_1)
    );

    public Map<String, Object> synchronizeData() {
        Map<String, Object> result = new LinkedHashMap<>();

        for (Map.Entry<Integer, ApiEndpoint> entry : API_ENDPOINTS.entrySet()) {
            String baseUrl = entry.getValue().getUrl();
            Championship championship = entry.getValue().getChampionship();
            String apiKey = "api-" + entry.getKey();

            try {
                // get and validate the clubs
                List<Club> clubs = fetchClubs(baseUrl);
                clubs.stream()
                        .peek(club -> club.setChampionship(championship)) // Définir le championship
                        .filter(dataValidator::isValidClub)
                        .forEach(clubDAO::saveFetchedClubIntoCentral);
                result.put(apiKey + "-clubs", clubs);

                // get and validate the players
                List<Player> players = fetchPlayers(baseUrl);
                players.stream()
                        .peek(player -> player.setChampionship(championship)) // Définir le championship
                        .filter(dataValidator::isValidPlayer)
                        .forEach(playerDAO::saveFetchedPlayerIntoClub);
                result.put(apiKey + "-players", players);

                // get and validate the goals mediana
                Integer median = fetchDifferenceGoalMedian(baseUrl);
                if (median != null) {
                    DifferenceGoalMedian medianModel = new DifferenceGoalMedian(median, championship);
                    if (dataValidator.isValidDifferenceGoalMedian(medianModel)) {
                        differenceGoalMedianDao.saveFetchedGoalMedian(medianModel);
                        result.put(apiKey + "-differenceGoalsMedian", medianModel);
                    }
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
            ResponseEntity<List<ClubDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
            return response.getBody() != null
                    ? response.getBody().stream()
                    .map(clubRankingRestMapper::mapToClubEntity)
                    .collect(Collectors.toList())
                    : Collections.emptyList();
        } catch (RestClientException e) {
            logger.error("Failed to fetch clubs from {}", url, e);
            throw new RuntimeException("Failed to fetch clubs from " + url, e);
        }
    }

    private List<Player> fetchPlayers(String baseUrl) {
        String url = baseUrl + "/championship/players";
        try {
            ResponseEntity<List<PlayerDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
            return response.getBody() != null
                    ? response.getBody().stream()
                    .map(playerRankingRestMapper::mapToPlayerEntity)
                    .collect(Collectors.toList())
                    : Collections.emptyList();
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
