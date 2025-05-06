package com.central_fifa.dao;

import com.central_fifa.model.PlayingTime;
import com.central_fifa.model.enums.DurationUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PlayingTimeDAO implements GenericDAO<PlayingTime> {
    
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    public PlayingTimeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public void save(PlayingTime playingTime) {
        if (playingTime.getId() == null || playingTime.getId().isEmpty()) {
            // Insert new playing time
            String sql = "INSERT INTO playing_time (value, duration_unit, championship) " +
                    "VALUES (?, ?::duration_unit_enum, ?::championship_enum)";
            jdbcTemplate.update(sql, 
                    playingTime.getValue(), 
                    playingTime.getDurationUnit().name(), 
                    playingTime.getChampionship().name());
        } else {
            // Update existing playing time
            String sql = "UPDATE playing_time SET value = ?, duration_unit = ?::duration_unit_enum, " +
                    "championship = ?::championship_enum WHERE id = ?::uuid";
            jdbcTemplate.update(sql, 
                    playingTime.getValue(), 
                    playingTime.getDurationUnit().name(), 
                    playingTime.getChampionship().name(), 
                    playingTime.getId());
        }
    }
    
    /**
     * Save playing time with player and season relationships
     */
    public void saveWithRelationships(PlayingTime playingTime, String playerId, String seasonId) {
        if (playingTime.getId() == null || playingTime.getId().isEmpty()) {
            // Insert new playing time
            String sql = "INSERT INTO playing_time (player_id, season_id, value, duration_unit, championship) " +
                    "VALUES (?::uuid, ?::uuid, ?, ?::duration_unit_enum, ?::championship_enum)";
            jdbcTemplate.update(sql, 
                    playerId, 
                    seasonId, 
                    playingTime.getValue(), 
                    playingTime.getDurationUnit().name(), 
                    playingTime.getChampionship().name());
        } else {
            // Update existing playing time
            String sql = "UPDATE playing_time SET player_id = ?::uuid, season_id = ?::uuid, value = ?, " +
                    "duration_unit = ?::duration_unit_enum, championship = ?::championship_enum WHERE id = ?::uuid";
            jdbcTemplate.update(sql, 
                    playerId, 
                    seasonId, 
                    playingTime.getValue(), 
                    playingTime.getDurationUnit().name(), 
                    playingTime.getChampionship().name(), 
                    playingTime.getId());
        }
    }

    @Override
    public Optional<PlayingTime> findById(String id) {
        String sql = "SELECT id, value, duration_unit, championship " +
                "FROM playing_time WHERE id = ?::uuid";
        try {
            PlayingTime playingTime = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                PlayingTime pt = new PlayingTime();
                pt.setId(rs.getString("id"));
                pt.setValue(rs.getDouble("value"));
                pt.setDurationUnit(DurationUnit.valueOf(rs.getString("duration_unit")));
                pt.setChampionship(com.central_fifa.model.enums.Championship.valueOf(rs.getString("championship")));
                return pt;
            }, id);
            return Optional.ofNullable(playingTime);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public void delete(PlayingTime playingTime) {
        String sql = "DELETE FROM playing_time WHERE id = ?::uuid";
        jdbcTemplate.update(sql, playingTime.getId());
    }
    
    public Optional<PlayingTime> findByPlayerAndSeason(String playerId, String seasonId) {
        String sql = "SELECT id, value, duration_unit, championship " +
                "FROM playing_time WHERE player_id = ?::uuid AND season_id = ?::uuid";
        try {
            PlayingTime playingTime = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                PlayingTime pt = new PlayingTime();
                pt.setId(rs.getString("id"));
                pt.setValue(rs.getDouble("value"));
                pt.setDurationUnit(DurationUnit.valueOf(rs.getString("duration_unit")));
                pt.setChampionship(com.central_fifa.model.enums.Championship.valueOf(rs.getString("championship")));
                return pt;
            }, playerId, seasonId);
            return Optional.ofNullable(playingTime);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}