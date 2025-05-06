package com.central_fifa.model;

import com.central_fifa.model.enums.Championship;
import com.central_fifa.model.enums.MatchStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Match {
    private String id;
    private Club clubPlayingHome;
    private Club clubPlayingAway;
    private String stadium;
    private LocalDateTime matchDatetime;
    private MatchStatus actualStatus;
    private Season season;
    private List<Goal> scorers;
    private MatchScore score;
    private Championship championship;
}