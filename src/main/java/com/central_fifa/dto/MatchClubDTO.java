package com.central_fifa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchClubDTO {
    private String id;
    private String name;
    private String acronym;
    private Integer score;
    private List<GoalScorerDTO> scorers;
}