package com.central_fifa.dao;

import com.central_fifa.model.Player;

import java.util.Optional;

public class PlayerDAO  implements GenericDAO<Player>{
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
