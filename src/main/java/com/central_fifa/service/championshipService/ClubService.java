package com.central_fifa.service.championshipService;

import com.central_fifa.controller.dto.ClubRankingRestMapper;
import com.central_fifa.dao.championshipOperations.ClubDAO;
import com.central_fifa.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubDAO clubDAO;
    private final ClubRankingRestMapper clubRankingRestMapper;

    public List<ClubRanking> getBestClubs(int top) {
        List<Club> clubs = clubDAO.findBestClubs(top);
        return clubs.stream()
                .map(club -> clubRankingRestMapper.mapToClubRanking(club,clubs))
                .collect(Collectors.toList());
    }
}