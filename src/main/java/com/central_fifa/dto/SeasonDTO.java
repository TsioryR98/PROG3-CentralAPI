package com.central_fifa.dto;

import com.central_fifa.model.enums.SeasonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeasonDTO {
    private String id;
    private String alias;
    private Integer year;
    private SeasonStatus status;
}