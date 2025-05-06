package com.central_fifa.model;

import com.central_fifa.model.enums.Championship;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Goal {
    private String id;
    private Player player;
    private Club club;
    private Match match;
    private Integer minute;
    private Boolean ownGoal;
    private Championship championship;
}