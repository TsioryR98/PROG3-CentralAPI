package com.central_fifa.dto;

import com.central_fifa.model.enums.DurationUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayingTimeDTO {
    private Double value;
    private DurationUnit durationUnit;
}