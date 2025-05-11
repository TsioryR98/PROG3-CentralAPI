package com.central_fifa.service.centralService;

import com.central_fifa.model.enums.Championship;

public class ApiEndpoint {
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
