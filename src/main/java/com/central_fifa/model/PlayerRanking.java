package com.central_fifa.model;

import com.central_fifa.model.enums.Championship;
import com.central_fifa.model.enums.PlayerPosition;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PlayerRanking {
    private int rank;
    private String id;
    private String name;
    private int number;
    private PlayerPosition position;
    private String nationality;
    private int age;
    private Club club;
    private Championship championship;
    private int scoredGoals;
    private PlayingTime playingTime;
}
