package com.central_fifa.dao.championshipOperations;

import com.central_fifa.model.Coach;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@RequiredArgsConstructor
@Repository
public class CoachDAO implements GenericDAO<Coach> {

    @Override
    public Optional<Coach> findById(String id) {
        return Optional.empty();
    }
}
