package com.central_fifa.dao.championshipOperations;

import com.central_fifa.model.Player;
import com.central_fifa.model.enums.PlayerPosition;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@RequiredArgsConstructor
@Repository
public class PlayerDAO implements GenericDAO<Player> {
    private final ClubDAO clubDAO;

    @Override
    public Optional<Player> findById(String id) {
        return Optional.empty();
    }
}
