package com.central_fifa.dao;

import com.central_fifa.model.Club;

import java.util.Optional;

public class ClubDAO implements GenericDAO<Club> {
    @Override
    public void save(Club model) {
    }

    @Override
    public Optional<Club> findById(String id) {
        return Optional.empty();
    }
    @Override
    public void delete(Club model) {
    }
}
