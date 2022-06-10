package ru.team.up.teamup.service;

import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.InitiatorTypeDto;
import ru.team.up.teamup.entity.QReport;
import ru.team.up.teamup.entity.Report;
import ru.team.up.teamup.repositories.DataRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Report> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Report> findByParam(@Nullable AppModuleNameDto moduleName,
                                    @Nullable InitiatorTypeDto initiatorType,
                                    @Nullable String timeAfter,
                                    @Nullable String timeBefore,
                                    @Nullable String paramKey,
                                    @Nullable String paramValue) {
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
        Optional.ofNullable(paramKey).map(right -> report.parameters.containsKey(paramKey)).map(predicate::and);

        if (!timeAfter.isEmpty() && !timeBefore.isEmpty()) {
            predicate.and(report.time.between(from, to));
        } else if (!timeAfter.isEmpty()) {
            predicate.and(report.time.after(from));
        } else if (!timeBefore.isEmpty()) {
            predicate.and(report.time.before(to));
        }

        log.debug("Сформированный в findByParam предикат {}", predicate);
        List<Report> all = (List<Report>) repository.findAll(predicate);

        if (!paramValue.isEmpty()) {
            return all.stream().filter(x -> x.getParameters().containsValue(paramValue)).collect(Collectors.toList());
        }

        return all;
    }

}
