package com.central_fifa.model;

import com.central_fifa.model.enums.Championship;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Club {
    private String id;
    private String name;
    private String acronym;
    private Integer yearCreation;
    private String stadium;
    private String coachName;
    private String coachNationality;
    private Integer scoredGoals;
    private Integer concededGoals;
    private Integer differenceGoals;
    private Integer cleanSheetNumber;
    private Championship championship;
}
