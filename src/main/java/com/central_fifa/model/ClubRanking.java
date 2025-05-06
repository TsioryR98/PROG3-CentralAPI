package com.central_fifa.model;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@ToString
@EqualsAndHashCode
public class ClubRanking {
    private int rank;
    private Club club;
    private int rankingPoints;
    private int scoredGoals;
    private int concededGoals;
    private int differenceGoals;
    private int cleanSheetNumber;
}
