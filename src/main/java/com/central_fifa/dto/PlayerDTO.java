package com.central_fifa.dto;

import com.central_fifa.model.enums.Championship;
import com.central_fifa.model.enums.PlayerPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO {
    private String id;
    private String name;
    private Integer number;
    private PlayerPosition position;
    private String nationality;
    private Integer age;
    private ClubDTO club;
    private Championship championship;
}
