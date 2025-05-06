package com.central_fifa.dao;

import com.central_fifa.model.Goal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GoalDAO implements GenericDAO<Goal> {
    
    private final JdbcTemplate jdbcTemplate;
    private final PlayerDAO playerDAO;
    private final ClubDAO clubDAO;
    private final MatchDAO matchDAO;
    
    @Autowired
    public GoalDAO(JdbcTemplate jdbcTemplate, PlayerDAO playerDAO, ClubDAO clubDAO, MatchDAO matchDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.playerDAO = playerDAO;
        this.clubDAO = clubDAO;
        this.matchDAO = matchDAO;
    }
    
    @Override
    public void save(Goal goal) {
        // First save the player, club, and match if they exist
        if (goal.getPlayer() != null) {
            playerDAO.save(goal.getPlayer());
        }
        
        if (goal.getClub() != null) {
            clubDAO.save(goal.getClub());
        }
        
        if (goal.getMatch() != null) {
            matchDAO.save(goal.getMatch());
        }
        
        if (goal.getId() == null || goal.getId().isEmpty()) {
            // Insert new goal
            String sql = "INSERT INTO goal (player_id, club_id, match_id, minute, own_goal, championship) " +
                    "VALUES (?::uuid, ?::uuid, ?::uuid, ?, ?, ?::championship_enum)";
            jdbcTemplate.update(sql, 
                    goal.getPlayer() != null ? goal.getPlayer().getId() : null, 
                    goal.getClub() != null ? goal.getClub().getId() : null, 
                    goal.getMatch() != null ? goal.getMatch().getId() : null, 
                    goal.getMinute(), 
                    goal.getOwnGoal(), 
                    goal.getChampionship().name());
        } else {
            // Update existing goal
            String sql = "UPDATE goal SET player_id = ?::uuid, club_id = ?::uuid, match_id = ?::uuid, " +
                    "minute = ?, own_goal = ?, championship = ?::championship_enum WHERE goal_id = ?::uuid";
            jdbcTemplate.update(sql, 
                    goal.getPlayer() != null ? goal.getPlayer().getId() : null, 
                    goal.getClub() != null ? goal.getClub().getId() : null, 
                    goal.getMatch() != null ? goal.getMatch().getId() : null, 
                    goal.getMinute(), 
                    goal.getOwnGoal(), 
                    goal.getChampionship().name(), 
                    goal.getId());
        }
    }

    @Override
    public Optional<Goal> findById(String id) {
        String sql = "SELECT goal_id, player_id, club_id, match_id, minute, own_goal, championship " +
                "FROM goal WHERE goal_id = ?::uuid";
        try {
            Goal goal = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Goal g = new Goal();
                g.setId(rs.getString("goal_id"));
                g.setMinute(rs.getInt("minute"));
                g.setOwnGoal(rs.getBoolean("own_goal"));
                g.setChampionship(com.central_fifa.model.enums.Championship.valueOf(rs.getString("championship")));
                
                // Get player if exists
                String playerId = rs.getString("player_id");
                if (playerId != null) {
                    playerDAO.findById(playerId).ifPresent(g::setPlayer);
                }
                
                // Get club if exists
                String clubId = rs.getString("club_id");
                if (clubId != null) {
                    clubDAO.findById(clubId).ifPresent(g::setClub);
                }
                
                // Get match if exists
                String matchId = rs.getString("match_id");
                if (matchId != null) {
                    matchDAO.findById(matchId).ifPresent(g::setMatch);
                }
                
                return g;
            }, id);
            return Optional.ofNullable(goal);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public void delete(Goal goal) {
        String sql = "DELETE FROM goal WHERE goal_id = ?::uuid";
        jdbcTemplate.update(sql, goal.getId());
    }
}