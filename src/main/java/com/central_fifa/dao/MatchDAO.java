package com.central_fifa.dao;

import com.central_fifa.model.Match;
import com.central_fifa.model.enums.MatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public class MatchDAO implements GenericDAO<Match> {
    
    private final JdbcTemplate jdbcTemplate;
    private final ClubDAO clubDAO;
    private final SeasonDAO seasonDAO;
    
    @Autowired
    public MatchDAO(JdbcTemplate jdbcTemplate, ClubDAO clubDAO, SeasonDAO seasonDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.clubDAO = clubDAO;
        this.seasonDAO = seasonDAO;
    }
    
    @Override
    public void save(Match match) {
        // First save the clubs and season if they exist
        if (match.getClubPlayingHome() != null) {
            clubDAO.save(match.getClubPlayingHome());
        }
        
        if (match.getClubPlayingAway() != null) {
            clubDAO.save(match.getClubPlayingAway());
        }
        
        if (match.getSeason() != null) {
            seasonDAO.save(match.getSeason());
        }
        
        if (match.getId() == null || match.getId().isEmpty()) {
            // Insert new match
            String sql = "INSERT INTO match (home_club_id, away_club_id, stadium, match_datetime, status, season_id, championship) " +
                    "VALUES (?::uuid, ?::uuid, ?, ?, ?::match_status_enum, ?::uuid, ?::championship_enum)";
            jdbcTemplate.update(sql, 
                    match.getClubPlayingHome() != null ? match.getClubPlayingHome().getId() : null, 
                    match.getClubPlayingAway() != null ? match.getClubPlayingAway().getId() : null, 
                    match.getStadium(), 
                    match.getMatchDatetime() != null ? Timestamp.valueOf(match.getMatchDatetime()) : null, 
                    match.getActualStatus().name(), 
                    match.getSeason() != null ? match.getSeason().getId() : null, 
                    match.getChampionship().name());
        } else {
            // Update existing match
            String sql = "UPDATE match SET home_club_id = ?::uuid, away_club_id = ?::uuid, stadium = ?, " +
                    "match_datetime = ?, status = ?::match_status_enum, season_id = ?::uuid, championship = ?::championship_enum " +
                    "WHERE match_id = ?::uuid";
            jdbcTemplate.update(sql, 
                    match.getClubPlayingHome() != null ? match.getClubPlayingHome().getId() : null, 
                    match.getClubPlayingAway() != null ? match.getClubPlayingAway().getId() : null, 
                    match.getStadium(), 
                    match.getMatchDatetime() != null ? Timestamp.valueOf(match.getMatchDatetime()) : null, 
                    match.getActualStatus().name(), 
                    match.getSeason() != null ? match.getSeason().getId() : null, 
                    match.getChampionship().name(), 
                    match.getId());
        }
    }

    @Override
    public Optional<Match> findById(String id) {
        String sql = "SELECT match_id, home_club_id, away_club_id, stadium, match_datetime, status, season_id, championship " +
                "FROM match WHERE match_id = ?::uuid";
        try {
            Match match = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Match m = new Match();
                m.setId(rs.getString("match_id"));
                m.setStadium(rs.getString("stadium"));
                
                Timestamp matchDatetime = rs.getTimestamp("match_datetime");
                if (matchDatetime != null) {
                    m.setMatchDatetime(matchDatetime.toLocalDateTime());
                }
                
                m.setActualStatus(MatchStatus.valueOf(rs.getString("status")));
                m.setChampionship(com.central_fifa.model.enums.Championship.valueOf(rs.getString("championship")));
                
                // Get home club if exists
                String homeClubId = rs.getString("home_club_id");
                if (homeClubId != null) {
                    clubDAO.findById(homeClubId).ifPresent(m::setClubPlayingHome);
                }
                
                // Get away club if exists
                String awayClubId = rs.getString("away_club_id");
                if (awayClubId != null) {
                    clubDAO.findById(awayClubId).ifPresent(m::setClubPlayingAway);
                }
                
                // Get season if exists
                String seasonId = rs.getString("season_id");
                if (seasonId != null) {
                    seasonDAO.findById(seasonId).ifPresent(m::setSeason);
                }
                
                return m;
            }, id);
            return Optional.ofNullable(match);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public void delete(Match match) {
        String sql = "DELETE FROM match WHERE match_id = ?::uuid";
        jdbcTemplate.update(sql, match.getId());
    }
}