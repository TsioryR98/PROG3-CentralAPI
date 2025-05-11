package com.central_fifa.controller.dto;

import com.central_fifa.model.Player;
import com.central_fifa.model.PlayerRanking;
import com.central_fifa.model.PlayingTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
@RequiredArgsConstructor
@Component
public class PlayerRankingRestMapper {
        public PlayerRanking mapToPlayerRanking(Player player, List<Player> players) {
            return new PlayerRanking(
                    players.indexOf(player) + 1,
                    player.getId(),
                    player.getName(),
                    player.getNumber(),
                    player.getPosition(),
                    player.getNationality(),
                    player.getAge(),
                    player.getChampionship(),
                    player.getScoredGoals(),
                    new PlayingTime(
                            player.getPlayingTimeValue(),
                            player.getPlayingTimeDurationUnit()
                    )
            );
        }
        public Player mapToPlayerEntity (PlayerDTO playerDTO){
            Player player = new Player();
                    player.setId(playerDTO.getId());
                    player.setName(playerDTO.getName());
                    player.setNumber( playerDTO.getNumber());
                    player.setPosition(playerDTO.getPosition());
                    player.setNationality(playerDTO.getNationality());
                    player.setAge(playerDTO.getAge());
                    player.setChampionship(playerDTO.getChampionship());
                    player.setScoredGoals(playerDTO.getScoredGoals());
                    player.setPlayingTimeValue(playerDTO.getPlayingTime().getValue());
                    player.setPlayingTimeDurationUnit(playerDTO.getPlayingTime().getDurationUnit());
                            return player;
        }
}
