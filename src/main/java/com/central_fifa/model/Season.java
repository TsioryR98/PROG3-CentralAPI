package com.central_fifa.model;

import com.central_fifa.model.enums.Championship;
import com.central_fifa.model.enums.SeasonStatus;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Season {
    private String id;
    private String alias;
    private SeasonStatus status;
    private Integer year;
    private Championship championship;
}