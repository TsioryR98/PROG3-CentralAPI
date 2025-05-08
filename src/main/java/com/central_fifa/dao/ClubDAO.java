package com.central_fifa.dao;

import com.central_fifa.model.Club;
import com.central_fifa.model.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClubDAO implements GenericDAO<Club> {
    @Override
    public void save(Club club) {

    }

    @Override
    public Optional<Club> findById(String id) {
        return Optional.empty();
    }

    @Override
    public void delete(Club club) {

    }
}
