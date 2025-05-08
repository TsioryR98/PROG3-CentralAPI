package com.central_fifa.dao.championshipOperations;

import com.central_fifa.model.PlayingTime;
import com.central_fifa.model.enums.DurationUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@RequiredArgsConstructor
@Repository
public class PlayingTimeDAO implements GenericDAO<PlayingTime> {

    @Override
    public Optional<PlayingTime> findById(String id) {
        return Optional.empty();
    }
}