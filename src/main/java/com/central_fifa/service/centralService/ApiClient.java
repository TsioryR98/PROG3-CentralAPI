package com.central_fifa.service.centralService;

import com.central_fifa.controller.dto.ClubDTO;
import com.central_fifa.controller.dto.ClubRankingRestMapper;
import com.central_fifa.controller.dto.PlayerDTO;
import com.central_fifa.controller.dto.PlayerRankingRestMapper;
import com.central_fifa.model.Club;
import com.central_fifa.model.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ApiClient {
    private final RestTemplate restTemplate;
    private final ClubRankingRestMapper clubRankingRestMapper;
    private final PlayerRankingRestMapper playerRankingRestMapper;


    public List<Club> fetchClubs(String baseUrl) {
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
            throw new RuntimeException("Failed to fetch clubs from " + url, e);
        }
    }

    public List<Player> fetchPlayers(String baseUrl) {
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
            throw new RuntimeException("Failed to fetch players from " + url, e);
        }
    }


    public Integer fetchDifferenceGoalMedian(String baseUrl) {
        String url = baseUrl + "/championship/differenceGoalsMedian";
        try {
            ResponseEntity<Integer> response = restTemplate.getForEntity(url, Integer.class);
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch difference goal median from " + url, e);
        }
    }
}
