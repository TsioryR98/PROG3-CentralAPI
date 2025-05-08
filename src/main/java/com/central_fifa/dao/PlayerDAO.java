package com.central_fifa.dao;

import com.central_fifa.model.Player;
import com.central_fifa.model.enums.PlayerPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PlayerDAO implements GenericDAO<Player> {

    @Override
    public void save(Player player) {

    }

    @Override
    public Optional<Player> findById(String id) {
        return Optional.empty();
    }

    @Override
    public void delete(Player player) {

    }
}
