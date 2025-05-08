package com.central_fifa.dao.championshipOperations;

import com.central_fifa.model.Club;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@RequiredArgsConstructor
@Repository
public class ClubDAO implements GenericDAO<Club> {
    private final CoachDAO coachDAO;

    @Override
    public Optional<Club> findById(String id) {
        return Optional.empty();
    }
}
