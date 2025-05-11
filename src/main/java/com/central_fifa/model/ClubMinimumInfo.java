package com.central_fifa.model;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ClubMinimumInfo {
    private String id;
    private String name;
    private String acronym;
    private Integer yearCreation;
    private String stadium;
    private Coach coach;
}
