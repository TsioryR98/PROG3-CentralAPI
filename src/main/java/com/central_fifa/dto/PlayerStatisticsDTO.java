package com.central_fifa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerStatisticsDTO {
    private Integer scoredGoals;
    private PlayingTimeDTO playingTime;
}