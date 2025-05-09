package com.central_fifa.service.centralService;

import com.central_fifa.dao.championshipOperations.DifferenceGoalMedianDao;
import com.central_fifa.model.ChampionshipRanking;
import com.central_fifa.model.DifferenceGoalMedian;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DifferenceGoalMedianService {
    private final DifferenceGoalMedianDao differenceGoalMedianDAO;

    public List<ChampionshipRanking> getChampionshipRankings() {
        List<DifferenceGoalMedian> medians = differenceGoalMedianDAO.findAllOrderedByMedian();

        return IntStream.range(0, medians.size())
                .mapToObj(index -> {
                    DifferenceGoalMedian median = medians.get(index);
                    return new ChampionshipRanking(
                            index + 1, // Classement
                            median.getChampionship(),
                            median.getDifferenceGoalsMedian()
                    );
                })
                .toList();
    }
}
