//package com.central_fifa.dao.centralOperation;
//
//import com.central_fifa.config.DbConfig;
//import com.central_fifa.dao.mapper.ChampionShipPlayerMapper;
//import com.central_fifa.model.centralModel.ChampionshipPlayer;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@Repository
//public class ChampionshipRepository implements GenericDAO<ChampionshipPlayer> {
//    private final DbConfig dataSource;
//    private final ChampionShipPlayerMapper championShipPlayerMapper;
//
//    @Override
//    public Optional<ChampionshipPlayer> findById(String id) {
//        return Optional.empty();
//    }
//
//    //just save all fetched data from each championship to central's database inside player table from JSON external API
//    public List<ChampionshipPlayer> saveAllChampionshipPlayerToCentral (List<ChampionshipPlayer> championshipPlayerList){
//        List<ChampionshipPlayer> savedChampionshipPlayer = new ArrayList<>();
//
//        String insertQuery = "INSERT INTO player " +
//                "(player_id, player_name, number, position, nationality, age, championship, scored_goals,playing_time_value,playing_time_duration_unit) \n" +
//                "VALUES (?, ?, ?, ?, ?, ?, ?::championship, ?, ?, ?::playing_time_duration_unit)";
//
//        try(Connection connection = dataSource.getConnection()) {
//            connection.setAutoCommit(false);
//            try(PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
//                for (ChampionshipPlayer championshipPlayer : championshipPlayerList) {
//                    preparedStatement.setString(1, championshipPlayer.getId());
//                    preparedStatement.setString(2, championshipPlayer.getName());
//                    preparedStatement.setInt(3, championshipPlayer.getNumber());
//                    preparedStatement.setString(4, championshipPlayer.getPosition().name());
//                    preparedStatement.setString(5, championshipPlayer.getNationality());
//                    preparedStatement.setInt(6, championshipPlayer.getAge());
//                    preparedStatement.setString(7, championshipPlayer.getChampionship().name());
//                    preparedStatement.setInt(8, championshipPlayer.getScoredGoals());
//                    preparedStatement.setDouble(9, championshipPlayer.getPlayingTime().getValue());
//                    preparedStatement.setString(10, championshipPlayer.getPlayingTime().getDurationUnit().name());
//
//                    // Execute the insert statement
//                    try (ResultSet resultSet = preparedStatement.executeQuery()){
//                        if (resultSet.next()){
//                            ChampionshipPlayer savedPlayer = championShipPlayerMapper.apply(resultSet);
//                            savedChampionshipPlayer.add(savedPlayer);
//                        }
//                    } catch (SQLException e) {
//                        System.out.println("Player with ID " + championshipPlayer.getId() + " already exists. Skipping insert.");
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return savedChampionshipPlayer;
//    }
//
//    //add select to this central database for getting all players and giving ranks from the results
//}
//
