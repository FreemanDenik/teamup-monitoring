package ru.team.up.core.repositories;


import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.team.up.core.entity.Report;


import java.util.List;


/**
 * Интерфейс Data Repository
 * Для общих CRUD операций над типом Report (Сущность сообщений для Кафки)
 */

@Repository
public interface DataRepository extends MongoRepository<Report, String>, QuerydslPredicateExecutor<Report> {
    @Query(value = "{'parameters': {$exists:true}}")
    List<Report> getOneAnyRowWithParam(Pageable page);
}
