package com.central_fifa.dto;

import com.central_fifa.model.PlayingTime;
import com.central_fifa.model.enums.Championship;
import com.central_fifa.model.enums.PlayerPosition;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {
    private String id;
    private String name;
    private Integer number;
    private PlayerPosition position;
    private String nationality;
    private Integer age;
    private Championship championship;
    private int scoredGoals;
    private PlayingTime playingTime;
}
