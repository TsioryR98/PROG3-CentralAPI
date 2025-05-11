package com.central_fifa.model;

import com.central_fifa.model.enums.Championship;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ChampionshipRanking {
    private int rank;
    private Championship championship;
    private double differenceGoalsMedian;
}
