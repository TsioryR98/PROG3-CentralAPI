package com.central_fifa.model.centralModel;

import com.central_fifa.model.PlayingTime;
import com.central_fifa.model.enums.Championship;
import com.central_fifa.model.enums.PlayerPosition;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode

public class ChampionshipPlayer {
    private String id;
    private String name;
    private Integer number;
    private PlayerPosition position;
    private String nationality;
    private Integer age;
    private Championship championship;
    private Integer scoredGoals;
    private PlayingTime playingTime;
}
