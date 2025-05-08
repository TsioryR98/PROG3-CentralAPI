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

public class Player {
    private String id;
    private String name;
    private Integer number;
    private PlayerPosition position;
    private String nationality;
    private Integer age;
    private int scoredGoals;
    private PlayingTime playingTime;
    private Championship championship;
}
