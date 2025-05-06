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

        int savedCoaches = 0;
        int savedClubs = 0;
        int savedPlayers = 0;
        int savedSeasons = 0;
        int savedMatches = 0;
        int savedGoals = 0;
        int savedPlayingTimes = 0;
        int savedMatchScores = 0;

        // Process each API endpoint
        for (ApiEndpoint endpoint : API_ENDPOINTS.values()) {
            try {
                // Fetch and process seasons
                List<SeasonDTO> seasons = fetchSeasons(endpoint.getUrl(), endpoint.getChampionship());
                if (seasons != null) {
                    allSeasons.addAll(seasons);

                    // Save seasons to database
                    for (SeasonDTO seasonDTO : seasons) {
                        Season season = new Season();
                        season.setId(seasonDTO.getId());
                        season.setAlias(seasonDTO.getAlias());
                        season.setYear(seasonDTO.getYear());
                        season.setStatus(seasonDTO.getStatus());
                        season.setChampionship(endpoint.getChampionship());

                        seasonDAO.save(season);
                        savedSeasons++;
                    }
                }

                // Fetch and process clubs
                List<ClubDTO> clubs = fetchClubs(endpoint.getUrl(), endpoint.getChampionship());
                if (clubs != null) {
                    allClubs.addAll(clubs);

                    // Save clubs and coaches to database
                    for (ClubDTO clubDTO : clubs) {
                        // Save coach first
                        Coach coach = null;
                        if (clubDTO.getCoach() != null) {
                            coach = new Coach();
                            coach.setName(clubDTO.getCoach().getName());
                            coach.setNationality(clubDTO.getCoach().getNationality());
                            coach.setChampionship(endpoint.getChampionship());

                            coachDAO.save(coach);
                            savedCoaches++;
                        }

                        // Save club
                        Club club = new Club();
                        club.setId(clubDTO.getId());
                        club.setName(clubDTO.getName());
                        club.setAcronym(clubDTO.getAcronym());
                        club.setYearCreation(clubDTO.getYearCreation());
                        club.setStadium(clubDTO.getStadium());
                        club.setCoach(coach);
                        club.setChampionship(endpoint.getChampionship());

                        clubDAO.save(club);
                        savedClubs++;
                    }

                    // For each club, fetch its players
                    for (ClubDTO clubDTO : clubs) {
                        try {
                            List<PlayerDTO> clubPlayers = fetchClubPlayers(endpoint.getUrl(), clubDTO.getId(), endpoint.getChampionship());
                            if (clubPlayers != null) {
                                // We don't add these to allPlayers to avoid duplicates
                                // but we could process them if needed
                            }
                        } catch (RestClientException e) {
                            logger.error("Error fetching players for club {}: {}", clubDTO.getId(), e.getMessage());
                        }
                    }
                }

                // Fetch and process players
                List<PlayerDTO> players = fetchPlayers(endpoint.getUrl(), endpoint.getChampionship());
                if (players != null) {
                    allPlayers.addAll(players);

                    // Save players to database
                    for (PlayerDTO playerDTO : players) {
                        Player player = new Player();
                        player.setId(playerDTO.getId());
                        player.setName(playerDTO.getName());
                        player.setNumber(playerDTO.getNumber());
                        player.setPosition(playerDTO.getPosition());
                        player.setNationality(playerDTO.getNationality());
                        player.setAge(playerDTO.getAge());
                        player.setChampionship(endpoint.getChampionship());

                        // Set club if exists
                        if (playerDTO.getClub() != null) {
                            Club club = new Club();
                            club.setId(playerDTO.getClub().getId());
                            player.setClub(club);
                        }

                        playerDAO.save(player);
                        savedPlayers++;

                        // Fetch and save player statistics
                        try {
                            PlayerStatisticsDTO playerStats = fetchPlayerStatistics(endpoint.getUrl(), playerDTO.getId(), CURRENT_SEASON_YEAR);
                            if (playerStats != null) {
                                // Find or create season
                                Season season = new Season();
                                season.setYear(CURRENT_SEASON_YEAR);
                                season.setChampionship(endpoint.getChampionship());

                                // Save playing time
                                if (playerStats.getPlayingTime() != null) {
                                    PlayingTime playingTime = new PlayingTime();
                                    playingTime.setValue(playerStats.getPlayingTime().getValue());
                                    playingTime.setDurationUnit(playerStats.getPlayingTime().getDurationUnit());
                                    playingTime.setChampionship(endpoint.getChampionship());

                                    // Find season by year and championship
                                    Optional<Season> existingSeason = seasonDAO.findByYearAndChampionship(CURRENT_SEASON_YEAR, endpoint.getChampionship());
                                    String seasonId = existingSeason.map(Season::getId).orElse(null);

                                    playingTimeDAO.saveWithRelationships(playingTime, player.getId(), seasonId);
                                    savedPlayingTimes++;
                                }
                            }
                        } catch (RestClientException e) {
                            logger.error("Error fetching statistics for player {}: {}", playerDTO.getId(), e.getMessage());
                        }
                    }
                }

                // Fetch and process matches
                List<MatchDTO> matches = fetchMatches(endpoint.getUrl(), CURRENT_SEASON_YEAR, endpoint.getChampionship());
                if (matches != null) {
                    allMatches.addAll(matches);

                    // Save matches to database
                    for (MatchDTO matchDTO : matches) {
                        Match match = new Match();
                        match.setId(matchDTO.getId());
                        match.setStadium(matchDTO.getStadium());
                        match.setMatchDatetime(matchDTO.getMatchDatetime());
                        match.setActualStatus(matchDTO.getActualStatus());
                        match.setChampionship(endpoint.getChampionship());

                        // Set home club if exists
                        if (matchDTO.getClubPlayingHome() != null) {
                            Club homeClub = new Club();
                            homeClub.setId(matchDTO.getClubPlayingHome().getId());
                            match.setClubPlayingHome(homeClub);
                        }

                        // Set away club if exists
                        if (matchDTO.getClubPlayingAway() != null) {
                            Club awayClub = new Club();
                            awayClub.setId(matchDTO.getClubPlayingAway().getId());
                            match.setClubPlayingAway(awayClub);
                        }

                        // Find or create season
                        Optional<Season> existingSeason = seasonDAO.findByYearAndChampionship(CURRENT_SEASON_YEAR, endpoint.getChampionship());
                        if (existingSeason.isPresent()) {
                            match.setSeason(existingSeason.get());
                        }

                        matchDAO.save(match);
                        savedMatches++;

                        // Save match score
                        if (matchDTO.getClubPlayingHome() != null && matchDTO.getClubPlayingAway() != null) {
                            MatchScore matchScore = new MatchScore();
                            matchScore.setMatch(match);
                            matchScore.setHomeScore(matchDTO.getClubPlayingHome().getScore());
                            matchScore.setAwayScore(matchDTO.getClubPlayingAway().getScore());
                            matchScore.setChampionship(endpoint.getChampionship());

                            matchScoreDAO.save(matchScore);
                            savedMatchScores++;
                        }

                        // Save goals
                        if (matchDTO.getClubPlayingHome() != null && matchDTO.getClubPlayingHome().getScorers() != null) {
                            for (GoalScorerDTO scorer : matchDTO.getClubPlayingHome().getScorers()) {
                                saveGoal(scorer, matchDTO.getClubPlayingHome().getId(), match.getId(), endpoint.getChampionship());
                                savedGoals++;
                            }
                        }

                        if (matchDTO.getClubPlayingAway() != null && matchDTO.getClubPlayingAway().getScorers() != null) {
                            for (GoalScorerDTO scorer : matchDTO.getClubPlayingAway().getScorers()) {
                                saveGoal(scorer, matchDTO.getClubPlayingAway().getId(), match.getId(), endpoint.getChampionship());
                                savedGoals++;
                            }
                        }
                    }
                }

                // Fetch and process club statistics
                List<ClubStatisticsDTO> clubStatistics = fetchClubStatistics(endpoint.getUrl(), CURRENT_SEASON_YEAR, endpoint.getChampionship());
                if (clubStatistics != null) {
                    allClubStatistics.addAll(clubStatistics);
                    // We don't need to save club statistics as they can be calculated from matches and goals
                }

            } catch (RestClientException e) {
                logger.error("Error synchronizing with endpoint {}: {}", endpoint.getUrl(), e.getMessage());
            }
        }

        // Add all collected data to the result
        result.put("players", allPlayers);
        result.put("clubs", allClubs);
        result.put("seasons", allSeasons);
        result.put("matches", allMatches);
        result.put("clubStatistics", allClubStatistics);

        // Add persistence statistics to the result
        Map<String, Integer> persistenceStats = new HashMap<>();
        persistenceStats.put("savedCoaches", savedCoaches);
        persistenceStats.put("savedClubs", savedClubs);
        persistenceStats.put("savedPlayers", savedPlayers);
        persistenceStats.put("savedSeasons", savedSeasons);
        persistenceStats.put("savedMatches", savedMatches);
        persistenceStats.put("savedGoals", savedGoals);
        persistenceStats.put("savedPlayingTimes", savedPlayingTimes);
        persistenceStats.put("savedMatchScores", savedMatchScores);
        result.put("persistenceStats", persistenceStats);

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
                    new ParameterizedTypeReference<List<PlayerDTO>>() {}
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
                    new ParameterizedTypeReference<List<SeasonDTO>>() {}
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
                    new ParameterizedTypeReference<List<ClubDTO>>() {}
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
                    new ParameterizedTypeReference<List<PlayerDTO>>() {}
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
                    new ParameterizedTypeReference<List<MatchDTO>>() {}
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
                    new ParameterizedTypeReference<List<ClubStatisticsDTO>>() {}
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
