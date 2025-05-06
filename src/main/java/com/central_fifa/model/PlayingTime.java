package com.central_fifa.model;

import com.central_fifa.model.enums.Championship;
import com.central_fifa.model.enums.DurationUnit;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PlayingTime {
    private String id;
    private Double value;
    private DurationUnit durationUnit;
    private Championship championship;
}
