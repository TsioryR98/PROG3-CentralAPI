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
        List<PlayerDTO> allPlayers = new ArrayList<>();
        List<ClubDTO> allClubs = new ArrayList<>();
        List<SeasonDTO> allSeasons = new ArrayList<>();
        List<MatchDTO> allMatches = new ArrayList<>();
        List<ClubStatisticsDTO> allClubStatistics = new ArrayList<>();

        // Process each API endpoint
        for (ApiEndpoint endpoint : API_ENDPOINTS.values()) {
            try {
                // Test fetching seasons
                List<SeasonDTO> seasons = fetchSeasons(endpoint.getUrl(), endpoint.getChampionship());
                if (seasons != null) {
                    allSeasons.addAll(seasons);
                    logger.info("Fetched {} seasons from {}", seasons.size(), endpoint.getUrl());
                }

                // Test fetching clubs
                List<ClubDTO> clubs = fetchClubs(endpoint.getUrl(), endpoint.getChampionship());
                if (clubs != null) {
                    allClubs.addAll(clubs);
                    logger.info("Fetched {} clubs from {}", clubs.size(), endpoint.getUrl());
                }

                // Test fetching players
                List<PlayerDTO> players = fetchPlayers(endpoint.getUrl(), endpoint.getChampionship());
                if (players != null) {
                    allPlayers.addAll(players);
                    logger.info("Fetched {} players from {}", players.size(), endpoint.getUrl());
                }

                // Test fetching matches
                List<MatchDTO> matches = fetchMatches(endpoint.getUrl(), CURRENT_SEASON_YEAR, endpoint.getChampionship());
                if (matches != null) {
                    allMatches.addAll(matches);
                    logger.info("Fetched {} matches from {}", matches.size(), endpoint.getUrl());
                }

                // Test fetching club statistics
                List<ClubStatisticsDTO> clubStatistics = fetchClubStatistics(endpoint.getUrl(), CURRENT_SEASON_YEAR, endpoint.getChampionship());
                if (clubStatistics != null) {
                    allClubStatistics.addAll(clubStatistics);
                    logger.info("Fetched {} club statistics from {}", clubStatistics.size(), endpoint.getUrl());
                }

            } catch (RestClientException e) {
                logger.error("Error testing endpoint {}: {}", endpoint.getUrl(), e.getMessage());
            }
        }

        // Add all collected data to the result
        result.put("players", allPlayers);
        result.put("clubs", allClubs);
        result.put("seasons", allSeasons);
        result.put("matches", allMatches);
        result.put("clubStatistics", allClubStatistics);

        return result;
    }

    private void saveGoal(GoalScorerDTO scorer, String clubId, String matchId, Championship championship) {
        if (scorer.getPlayer() != null) {
            Goal goal = new Goal();

            // Set player
            Player player = new Player();
            player.setId(scorer.getPlayer().getId());
            goal.setPlayer(player);

            // Set club
            Club club = new Club();
            club.setId(clubId);
            goal.setClub(club);

            // Set match
            Match match = new Match();
            match.setId(matchId);
            goal.setMatch(match);

            goal.setMinute(scorer.getMinuteOfGoal());
            goal.setOwnGoal(scorer.getOwnGoal());
            goal.setChampionship(championship);

            goalDAO.save(goal);
        }
    }

    private List<PlayerDTO> fetchPlayers(String baseUrl, Championship championship) {
        try {
            ResponseEntity<List<PlayerDTO>> response = restTemplate.exchange(
                    baseUrl + "/players",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<PlayerDTO>>() {
                    }
            );

            List<PlayerDTO> players = response.getBody();
            if (players != null) {
                players.forEach(player -> {
                    // Set championship for each player
                    player.setChampionship(championship);
                });
            }

            return players;
        } catch (RestClientException e) {
            logger.error("Error fetching players from {}: {}", baseUrl, e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<SeasonDTO> fetchSeasons(String baseUrl, Championship championship) {
        try {
            ResponseEntity<List<SeasonDTO>> response = restTemplate.exchange(
                    baseUrl + "/seasons",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SeasonDTO>>() {
                    }
            );

            return response.getBody();
        } catch (RestClientException e) {
            logger.error("Error fetching seasons from {}: {}", baseUrl, e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<ClubDTO> fetchClubs(String baseUrl, Championship championship) {
        try {
            ResponseEntity<List<ClubDTO>> response = restTemplate.exchange(
                    baseUrl + "/clubs",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ClubDTO>>() {
                    }
            );

            List<ClubDTO> clubs = response.getBody();
            if (clubs != null) {
                clubs.forEach(club -> {
                    // Set championship for each club
                    club.setChampionship(championship);
                });
            }

            return clubs;
        } catch (RestClientException e) {
            logger.error("Error fetching clubs from {}: {}", baseUrl, e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<PlayerDTO> fetchClubPlayers(String baseUrl, String clubId, Championship championship) {
        try {
            ResponseEntity<List<PlayerDTO>> response = restTemplate.exchange(
                    baseUrl + "/clubs/" + clubId + "/players",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<PlayerDTO>>() {
                    }
            );

            List<PlayerDTO> players = response.getBody();
            if (players != null) {
                players.forEach(player -> {
                    // Set championship for each player
                    player.setChampionship(championship);
                });
            }

            return players;
        } catch (RestClientException e) {
            logger.error("Error fetching players for club {} from {}: {}", clubId, baseUrl, e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<MatchDTO> fetchMatches(String baseUrl, int seasonYear, Championship championship) {
        try {
            ResponseEntity<List<MatchDTO>> response = restTemplate.exchange(
                    baseUrl + "/matches/" + seasonYear,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<MatchDTO>>() {
                    }
            );

            return response.getBody();
        } catch (RestClientException e) {
            logger.error("Error fetching matches for season {} from {}: {}", seasonYear, baseUrl, e.getMessage());
            return Collections.emptyList();
        }
    }

    private PlayerStatisticsDTO fetchPlayerStatistics(String baseUrl, String playerId, int seasonYear) {
        try {
            ResponseEntity<PlayerStatisticsDTO> response = restTemplate.exchange(
                    baseUrl + "/players/" + playerId + "/statistics/" + seasonYear,
                    HttpMethod.GET,
                    null,
                    PlayerStatisticsDTO.class
            );

            return response.getBody();
        } catch (RestClientException e) {
            logger.error("Error fetching statistics for player {} for season {} from {}: {}",
                    playerId, seasonYear, baseUrl, e.getMessage());
            return null;
        }
    }

    private List<ClubStatisticsDTO> fetchClubStatistics(String baseUrl, int seasonYear, Championship championship) {
        try {
            ResponseEntity<List<ClubStatisticsDTO>> response = restTemplate.exchange(
                    baseUrl + "/clubs/statistics/" + seasonYear,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ClubStatisticsDTO>>() {
                    }
            );

            List<ClubStatisticsDTO> clubStatistics = response.getBody();
            if (clubStatistics != null) {
                clubStatistics.forEach(clubStat -> {
                    // Set championship for each club statistic
                    clubStat.setChampionship(championship);
                });
            }

            return clubStatistics;
        } catch (RestClientException e) {
            logger.error("Error fetching club statistics for season {} from {}: {}",
                    seasonYear, baseUrl, e.getMessage());
            return Collections.emptyList();
        }
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
