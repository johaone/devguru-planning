package ru.devguru.springms.planner.todo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.devguru.springms.planner.entity.Statistic;

@Repository
public interface StatisticRepository extends CrudRepository<Statistic, Long> { // Наследуемся не от JpaRepository, а от CRUDRepository - есть все нужные методы для OneToOne связи
    Statistic findByUserId(Long userId); // Получаем только один объект - связь один к одному
}
