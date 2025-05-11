package com.central_fifa.service.centralService;

import com.central_fifa.dao.championshipOperations.DifferenceGoalMedianDao;
import com.central_fifa.dao.championshipOperations.PlayerDAO;
import com.central_fifa.dao.championshipOperations.*;
import com.central_fifa.model.Club;
import com.central_fifa.model.DifferenceGoalMedian;
import com.central_fifa.model.Player;
import com.central_fifa.model.enums.Championship;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.central_fifa.service.centralService.ApiEndpointConfig.API_ENDPOINTS;

@Service
@RequiredArgsConstructor
public class SynchronizationService {
    private static final Logger logger = LoggerFactory.getLogger(SynchronizationService.class);

    private final ClubDAO clubDAO;
    private final PlayerDAO playerDAO;
    private final DifferenceGoalMedianDao differenceGoalMedianDao;
    private final DataValidator dataValidator;
    private final ApiClient apiClient;

    public Map<String, Object> synchronizeData() {
        Map<String, Object> result = new LinkedHashMap<>();

        for (Map.Entry<Integer, ApiEndpoint> entry : API_ENDPOINTS.entrySet()) {
            String baseUrl = entry.getValue().getUrl();
            Championship championship = entry.getValue().getChampionship();
            String apiKey = "api-" + entry.getKey();

            try {
                // get and validate the clubs
                List<Club> clubs = apiClient.fetchClubs(baseUrl);
                clubs.stream()
                        .map(club -> {
                            club.setChampionship(championship);
                            return club;
                        })
                        .filter(dataValidator::isValidClub)
                        .forEach(clubDAO::saveFetchedClubIntoCentral);
                result.put(apiKey + "-clubs", clubs);

                // get and validate the players
                List<Player> players = apiClient.fetchPlayers(baseUrl);
                players.stream()
                        .map(player -> {
                            player.setChampionship(championship);
                            return player;
                        })
                        .filter(dataValidator::isValidPlayer)
                        .forEach(playerDAO::saveFetchedPlayerIntoClub);
                result.put(apiKey + "-players", players);

                // get and validate the goals mediana
                Integer median = apiClient.fetchDifferenceGoalMedian(baseUrl);
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
}
