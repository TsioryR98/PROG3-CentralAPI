package com.central_fifa.dao;

import com.central_fifa.model.Season;
import com.central_fifa.model.enums.SeasonStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SeasonDAO implements GenericDAO<Season> {
    
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    public SeasonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public void save(Season season) {
        if (season.getId() == null || season.getId().isEmpty()) {
            // Insert new season
            String sql = "INSERT INTO season (alias, status, year, championship) " +
                    "VALUES (?, ?::season_status_enum, ?, ?::championship_enum)";
            jdbcTemplate.update(sql, 
                    season.getAlias(), 
                    season.getStatus().name(), 
                    season.getYear(), 
                    season.getChampionship().name());
        } else {
            // Update existing season
            String sql = "UPDATE season SET alias = ?, status = ?::season_status_enum, " +
                    "year = ?, championship = ?::championship_enum WHERE season_id = ?::uuid";
            jdbcTemplate.update(sql, 
                    season.getAlias(), 
                    season.getStatus().name(), 
                    season.getYear(), 
                    season.getChampionship().name(), 
                    season.getId());
        }
    }

    @Override
    public Optional<Season> findById(String id) {
        String sql = "SELECT season_id, alias, status, year, championship FROM season WHERE season_id = ?::uuid";
        try {
            Season season = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Season s = new Season();
                s.setId(rs.getString("season_id"));
                s.setAlias(rs.getString("alias"));
                s.setStatus(SeasonStatus.valueOf(rs.getString("status")));
                s.setYear(rs.getInt("year"));
                s.setChampionship(com.central_fifa.model.enums.Championship.valueOf(rs.getString("championship")));
                return s;
            }, id);
            return Optional.ofNullable(season);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public void delete(Season season) {
        String sql = "DELETE FROM season WHERE season_id = ?::uuid";
        jdbcTemplate.update(sql, season.getId());
    }
    
    public Optional<Season> findByYearAndChampionship(int year, com.central_fifa.model.enums.Championship championship) {
        String sql = "SELECT season_id, alias, status, year, championship FROM season " +
                "WHERE year = ? AND championship = ?::championship_enum";
        try {
            Season season = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Season s = new Season();
                s.setId(rs.getString("season_id"));
                s.setAlias(rs.getString("alias"));
                s.setStatus(SeasonStatus.valueOf(rs.getString("status")));
                s.setYear(rs.getInt("year"));
                s.setChampionship(com.central_fifa.model.enums.Championship.valueOf(rs.getString("championship")));
                return s;
            }, year, championship.name());
            return Optional.ofNullable(season);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}