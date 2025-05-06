package com.central_fifa.dao;

import com.central_fifa.model.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CoachDAO implements GenericDAO<Coach> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CoachDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Coach coach) {
        if (coach.getId() == null || coach.getId().isEmpty()) {
            // Insert new coach
            String sql = "INSERT INTO coach (coach_name, nationality, championship) VALUES (?, ?, ?::championship_enum)";
            jdbcTemplate.update(sql, coach.getName(), coach.getNationality(), coach.getChampionship().name());
        } else {
            // Update existing coach
            String sql = "UPDATE coach SET coach_name = ?, nationality = ?, championship = ?::championship_enum WHERE coach_id = ?::uuid";
            jdbcTemplate.update(sql, coach.getName(), coach.getNationality(), coach.getChampionship().name(), coach.getId());
        }
    }

    @Override
    public Optional<Coach> findById(String id) {
        String sql = "SELECT coach_id, coach_name, nationality, championship FROM coach WHERE coach_id = ?::uuid";
        try {
            Coach coach = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Coach c = new Coach();
                c.setId(rs.getString("coach_id"));
                c.setName(rs.getString("coach_name"));
                c.setNationality(rs.getString("nationality"));
                c.setChampionship(com.central_fifa.model.enums.Championship.valueOf(rs.getString("championship")));
                return c;
            }, id);
            return Optional.ofNullable(coach);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Coach coach) {
        String sql = "DELETE FROM coach WHERE coach_id = ?::uuid";
        jdbcTemplate.update(sql, coach.getId());
    }
}
