package com.central_fifa.model;

import com.central_fifa.model.enums.Championship;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MatchScore {
    private String id;
    private Match match;
    private Integer homeScore;
    private Integer awayScore;
    private Championship championship;
}