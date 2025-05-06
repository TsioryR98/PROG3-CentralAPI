package com.central_fifa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoalScorerDTO {
    private PlayerSummaryDTO player;
    private Integer minuteOfGoal;
    private Boolean ownGoal;
}
