package com.central_fifa.dao.championshipOperations;

import com.central_fifa.model.Season;
import com.central_fifa.model.enums.SeasonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@RequiredArgsConstructor
@Repository
public class SeasonDAO implements GenericDAO<Season> {

    @Override
    public Optional<Season> findById(String id) {
        return Optional.empty();
    }
}