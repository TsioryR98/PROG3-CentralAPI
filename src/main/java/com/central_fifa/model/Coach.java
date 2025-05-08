package com.central_fifa.model;

import com.central_fifa.model.enums.Championship;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Coach {
    private String name;
    private String nationality;
}
