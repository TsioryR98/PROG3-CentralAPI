package com.central_fifa.dao.championshipOperations;

import com.central_fifa.model.Match;
import com.central_fifa.model.enums.MatchStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;
@RequiredArgsConstructor
@Repository
public class MatchDAO implements GenericDAO<Match> {
    
    private final ClubDAO clubDAO;
    private final SeasonDAO seasonDAO;

    @Override
    public Optional<Match> findById(String id) {
        return Optional.empty();
    }
}