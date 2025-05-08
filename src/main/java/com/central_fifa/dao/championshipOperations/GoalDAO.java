package com.central_fifa.dao.championshipOperations;

import com.central_fifa.model.Goal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@RequiredArgsConstructor
@Repository
public class GoalDAO implements GenericDAO<Goal> {
    private final PlayerDAO playerDAO;
    private final ClubDAO clubDAO;
    private final MatchDAO matchDAO;

    @Override
    public Optional<Goal> findById(String id) {
        return Optional.empty();
    }
}