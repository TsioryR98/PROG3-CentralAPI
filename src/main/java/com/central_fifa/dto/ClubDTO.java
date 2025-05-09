package com.central_fifa.dto;

import com.central_fifa.model.ClubMinimumInfo;
import com.central_fifa.model.enums.Championship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubDTO {
    private ClubMinimumInfo club;
    private int rankingPoints;
    private int scoredGoals;
    private int concededGoals;
    private int differenceGoals;
    private int cleanSheetNumber;
}
