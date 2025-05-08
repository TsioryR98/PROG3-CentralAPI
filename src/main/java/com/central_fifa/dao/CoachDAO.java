package com.central_fifa.dao;

import com.central_fifa.model.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CoachDAO implements GenericDAO<Coach> {
    @Override
    public void save(Coach coach) {

    }

    @Override
    public Optional<Coach> findById(String id) {
        return Optional.empty();
    }

    @Override
    public void delete(Coach coach) {

    }
}
