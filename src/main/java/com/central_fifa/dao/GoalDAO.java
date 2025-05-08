package com.central_fifa.dao;

import com.central_fifa.model.Goal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GoalDAO implements GenericDAO<Goal> {

    @Override
    public void save(Goal goal) {

    }

    @Override
    public Optional<Goal> findById(String id) {
        return Optional.empty();
    }

    @Override
    public void delete(Goal goal) {

    }
}