package com.central_fifa.dao;

import com.central_fifa.model.MatchScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MatchScoreDAO implements GenericDAO<MatchScore> {
    
    private final JdbcTemplate jdbcTemplate;
    private final MatchDAO matchDAO;
    
    @Autowired
    public MatchScoreDAO(JdbcTemplate jdbcTemplate, MatchDAO matchDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.matchDAO = matchDAO;
    }
    
    @Override
    public void save(MatchScore matchScore) {
        // First save the match if it exists
        if (matchScore.getMatch() != null) {
            matchDAO.save(matchScore.getMatch());
        }
        
        // Check if a match score record already exists for this match
        String checkSql = "SELECT match_score_id FROM match_score WHERE match_id = ?::uuid";
        String existingId = null;
        try {
            existingId = jdbcTemplate.queryForObject(checkSql, String.class, 
                    matchScore.getMatch() != null ? matchScore.getMatch().getId() : null);
        } catch (Exception e) {
            // No existing record found
        }
        
        if (existingId != null) {
            // Update existing match score
            String sql = "UPDATE match_score SET home_score = ?, away_score = ?, " +
                    "championship = ?::championship_enum WHERE match_score_id = ?::uuid";
            jdbcTemplate.update(sql, 
                    matchScore.getHomeScore(), 
                    matchScore.getAwayScore(), 
                    matchScore.getChampionship().name(), 
                    existingId);
            matchScore.setId(existingId);
        } else if (matchScore.getId() == null || matchScore.getId().isEmpty()) {
            // Insert new match score
            String sql = "INSERT INTO match_score (match_id, home_score, away_score, championship) " +
                    "VALUES (?::uuid, ?, ?, ?::championship_enum)";
            jdbcTemplate.update(sql, 
                    matchScore.getMatch() != null ? matchScore.getMatch().getId() : null, 
                    matchScore.getHomeScore(), 
                    matchScore.getAwayScore(), 
                    matchScore.getChampionship().name());
        } else {
            // Update existing match score by ID
            String sql = "UPDATE match_score SET match_id = ?::uuid, home_score = ?, away_score = ?, " +
                    "championship = ?::championship_enum WHERE match_score_id = ?::uuid";
            jdbcTemplate.update(sql, 
                    matchScore.getMatch() != null ? matchScore.getMatch().getId() : null, 
                    matchScore.getHomeScore(), 
                    matchScore.getAwayScore(), 
                    matchScore.getChampionship().name(), 
                    matchScore.getId());
        }
    }

    @Override
    public Optional<MatchScore> findById(String id) {
        String sql = "SELECT match_score_id, match_id, home_score, away_score, championship " +
                "FROM match_score WHERE match_score_id = ?::uuid";
        try {
            MatchScore matchScore = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                MatchScore ms = new MatchScore();
                ms.setId(rs.getString("match_score_id"));
                ms.setHomeScore(rs.getInt("home_score"));
                ms.setAwayScore(rs.getInt("away_score"));
                ms.setChampionship(com.central_fifa.model.enums.Championship.valueOf(rs.getString("championship")));
                
                // Get match if exists
                String matchId = rs.getString("match_id");
                if (matchId != null) {
                    matchDAO.findById(matchId).ifPresent(ms::setMatch);
                }
                
                return ms;
            }, id);
            return Optional.ofNullable(matchScore);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public void delete(MatchScore matchScore) {
        String sql = "DELETE FROM match_score WHERE match_score_id = ?::uuid";
        jdbcTemplate.update(sql, matchScore.getId());
    }
    
    public Optional<MatchScore> findByMatchId(String matchId) {
        String sql = "SELECT match_score_id, match_id, home_score, away_score, championship " +
                "FROM match_score WHERE match_id = ?::uuid";
        try {
            MatchScore matchScore = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                MatchScore ms = new MatchScore();
                ms.setId(rs.getString("match_score_id"));
                ms.setHomeScore(rs.getInt("home_score"));
                ms.setAwayScore(rs.getInt("away_score"));
                ms.setChampionship(com.central_fifa.model.enums.Championship.valueOf(rs.getString("championship")));
                
                // Get match
                matchDAO.findById(matchId).ifPresent(ms::setMatch);
                
                return ms;
            }, matchId);
            return Optional.ofNullable(matchScore);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}