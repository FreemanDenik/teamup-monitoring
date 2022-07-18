package ru.team.up.core.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.team.up.core.entity.QReport;
import ru.team.up.core.entity.Report;
import ru.team.up.core.repositories.DataRepository;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.InitiatorTypeDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class DataServiceImpl implements DataService {

    private final DataRepository repository;

    public DataServiceImpl(DataRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveMessage(Report data) {
        repository.save(data);
    }

    @Override
    public Page<Report> getAll(int pageSize) {
        return repository.findAll(PageRequest.of(0, pageSize));
    }

    @Override
    public Page<Report> findByParam(@Nullable AppModuleNameDto moduleName,
                                    @Nullable InitiatorTypeDto initiatorType,
                                    @Nullable String timeAfter,
                                    @Nullable String timeBefore,
                                    @Nullable String paramKey,
                                    @Nullable String paramValue,
                                    int page,
                                    int pageSize) {
        QReport report = QReport.report;
        BooleanBuilder predicate = new BooleanBuilder();
        Date from = null;
        Date to = null;
        try {
            from = new SimpleDateFormat("yyyy-MM-dd").parse(timeAfter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            to = new SimpleDateFormat("yyyy-MM-dd").parse(timeBefore);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Optional.ofNullable(moduleName).map(report.appModuleName::eq).map(predicate::and);
        Optional.ofNullable(initiatorType).map(report.initiatorType::eq).map(predicate::and);
        Optional.ofNullable(paramKey.isEmpty() ? null : paramKey).map(right -> report.parameters.containsKey(paramKey)).map(predicate::and);

        // Получаем одну строку с непустым значением parameters
        List<Report> oneReport = repository.getOneAnyRowWithParam(PageRequest.of(0, 1));
        // Получаем все ключи из значения parameters
        var list = oneReport.get(0).getParameters().keySet();
        List<Predicate> listFilter = new ArrayList<>();
        // Запихиваем в коллекцию условия поиска description по полученным ключам
        list.forEach(key -> listFilter.add(report.parameters.get(key).description.contains(paramValue)));
        // Запихиваем коллекцию в виде массива в predicate
        predicate.andAnyOf(listFilter.stream().toArray(Predicate[]::new));

        if (!timeAfter.isEmpty() && !timeBefore.isEmpty()) {
            predicate.and(report.time.between(from, to));
        } else if (!timeAfter.isEmpty()) {
            predicate.and(report.time.after(from));
        } else if (!timeBefore.isEmpty()) {
            predicate.and(report.time.before(to));
        }

        log.debug("Сформированный в findByParam предикат {}", predicate);
        Page<Report> all = repository.findAll(predicate, PageRequest.of(page, pageSize));

        return all;
    }

}
