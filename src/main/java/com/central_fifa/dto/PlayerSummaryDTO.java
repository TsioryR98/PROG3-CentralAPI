package com.central_fifa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerSummaryDTO {
    private String id;
    private String name;
    private Integer number;
}