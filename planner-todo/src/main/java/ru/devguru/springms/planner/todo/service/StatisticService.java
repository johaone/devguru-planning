package ru.devguru.springms.planner.todo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.devguru.springms.planner.todo.repository.StatisticRepository;
import ru.devguru.springms.planner.entity.Statistic;

@Service
@Transactional
@RequiredArgsConstructor
public class StatisticService {
    StatisticRepository statisticRepository;

    public Statistic findStatistic (Long userId) {
        return statisticRepository.findByUserId(userId);
    }
}
