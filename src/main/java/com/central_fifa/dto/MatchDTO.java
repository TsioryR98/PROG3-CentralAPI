package com.central_fifa.dto;

import com.central_fifa.model.enums.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchDTO {
    private String id;
    private MatchClubDTO clubPlayingHome;
    private MatchClubDTO clubPlayingAway;
    private String stadium;
    private LocalDateTime matchDatetime;
    private MatchStatus actualStatus;
}