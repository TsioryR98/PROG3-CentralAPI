package com.central_fifa.dao.championshipOperations;

import com.central_fifa.model.MatchScore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@RequiredArgsConstructor
@Repository
public class MatchScoreDAO implements GenericDAO<MatchScore> {
    private final MatchDAO matchDAO;

    @Override
    public Optional<MatchScore> findById(String id) {
        return Optional.empty();
    }
}