package com.central_fifa.service.centralService;

import com.central_fifa.model.enums.Championship;

import java.util.Map;

public class ApiEndpointConfig {
    // API endpoints
    public static final Map<Integer, ApiEndpoint> API_ENDPOINTS = Map.of(
            8081, new ApiEndpoint("http://localhost:8081/", Championship.LA_LIGA),
            8082, new ApiEndpoint("http://localhost:8082/", Championship.LIGUE_1)
    );
}
