package com.central_fifa.dao;

import com.central_fifa.model.Club;
import com.central_fifa.model.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClubDAO implements GenericDAO<Club> {

    private final JdbcTemplate jdbcTemplate;
    private final CoachDAO coachDAO;

    @Autowired
    public ClubDAO(JdbcTemplate jdbcTemplate, CoachDAO coachDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.coachDAO = coachDAO;
    }

    @Override
    public void save(Club club) {
        // First save the coach if it exists
        if (club.getCoach() != null) {
            coachDAO.save(club.getCoach());
        }

        if (club.getId() == null || club.getId().isEmpty()) {
            // Insert new club
            String sql = "INSERT INTO club (club_name, acronym, year_creation, stadium, coach_id, championship) " +
                    "VALUES (?, ?, ?, ?, ?::uuid, ?::championship_enum)";
            jdbcTemplate.update(sql, 
                    club.getName(), 
                    club.getAcronym(), 
                    club.getYearCreation(), 
                    club.getStadium(), 
                    club.getCoach() != null ? club.getCoach().getId() : null, 
                    club.getChampionship().name());
        } else {
            // Update existing club
            String sql = "UPDATE club SET club_name = ?, acronym = ?, year_creation = ?, stadium = ?, " +
                    "coach_id = ?::uuid, championship = ?::championship_enum WHERE club_id = ?::uuid";
            jdbcTemplate.update(sql, 
                    club.getName(), 
                    club.getAcronym(), 
                    club.getYearCreation(), 
                    club.getStadium(), 
                    club.getCoach() != null ? club.getCoach().getId() : null, 
                    club.getChampionship().name(), 
                    club.getId());
        }
    }

    @Override
    public Optional<Club> findById(String id) {
        String sql = "SELECT club_id, club_name, acronym, year_creation, stadium, coach_id, championship " +
                "FROM club WHERE club_id = ?::uuid";
        try {
            Club club = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Club c = new Club();
                c.setId(rs.getString("club_id"));
                c.setName(rs.getString("club_name"));
                c.setAcronym(rs.getString("acronym"));
                c.setYearCreation(rs.getInt("year_creation"));
                c.setStadium(rs.getString("stadium"));
                c.setChampionship(com.central_fifa.model.enums.Championship.valueOf(rs.getString("championship")));

                // Get coach if exists
                String coachId = rs.getString("coach_id");
                if (coachId != null) {
                    coachDAO.findById(coachId).ifPresent(c::setCoach);
                }

                return c;
            }, id);
            return Optional.ofNullable(club);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Club club) {
        String sql = "DELETE FROM club WHERE club_id = ?::uuid";
        jdbcTemplate.update(sql, club.getId());
    }
}
