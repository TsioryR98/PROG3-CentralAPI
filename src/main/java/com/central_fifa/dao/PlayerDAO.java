package com.central_fifa.dao;

import com.central_fifa.model.Player;
import com.central_fifa.model.enums.PlayerPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PlayerDAO implements GenericDAO<Player> {

    private final JdbcTemplate jdbcTemplate;
    private final ClubDAO clubDAO;

    @Autowired
    public PlayerDAO(JdbcTemplate jdbcTemplate, ClubDAO clubDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.clubDAO = clubDAO;
    }

    @Override
    public void save(Player player) {
        // First save the club if it exists
        if (player.getClub() != null) {
            clubDAO.save(player.getClub());
        }

        if (player.getId() == null || player.getId().isEmpty()) {
            // Insert new player
            String sql = "INSERT INTO player (player_name, number, position, nationality, age, club_id, championship) " +
                    "VALUES (?, ?, ?::position_enum, ?, ?, ?::uuid, ?::championship_enum)";
            jdbcTemplate.update(sql, 
                    player.getName(), 
                    player.getNumber(), 
                    player.getPosition().name(), 
                    player.getNationality(), 
                    player.getAge(), 
                    player.getClub() != null ? player.getClub().getId() : null, 
                    player.getChampionship().name());
        } else {
            // Update existing player
            String sql = "UPDATE player SET player_name = ?, number = ?, position = ?::position_enum, " +
                    "nationality = ?, age = ?, club_id = ?::uuid, championship = ?::championship_enum " +
                    "WHERE player_id = ?::uuid";
            jdbcTemplate.update(sql, 
                    player.getName(), 
                    player.getNumber(), 
                    player.getPosition().name(), 
                    player.getNationality(), 
                    player.getAge(), 
                    player.getClub() != null ? player.getClub().getId() : null, 
                    player.getChampionship().name(), 
                    player.getId());
        }
    }

    @Override
    public Optional<Player> findById(String id) {
        String sql = "SELECT player_id, player_name, number, position, nationality, age, club_id, championship " +
                "FROM player WHERE player_id = ?::uuid";
        try {
            Player player = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Player p = new Player();
                p.setId(rs.getString("player_id"));
                p.setName(rs.getString("player_name"));
                p.setNumber(rs.getInt("number"));
                p.setPosition(PlayerPosition.valueOf(rs.getString("position")));
                p.setNationality(rs.getString("nationality"));
                p.setAge(rs.getInt("age"));
                p.setChampionship(com.central_fifa.model.enums.Championship.valueOf(rs.getString("championship")));

                // Get club if exists
                String clubId = rs.getString("club_id");
                if (clubId != null) {
                    clubDAO.findById(clubId).ifPresent(p::setClub);
                }

                return p;
            }, id);
            return Optional.ofNullable(player);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Player player) {
        String sql = "DELETE FROM player WHERE player_id = ?::uuid";
        jdbcTemplate.update(sql, player.getId());
    }
}
