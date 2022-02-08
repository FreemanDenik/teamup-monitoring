package ru.team.up.teamup.service;

import org.springframework.stereotype.Service;
import ru.team.up.teamup.entity.Control;
import ru.team.up.teamup.entity.Report;
import ru.team.up.teamup.repositories.DataRepository;

import java.util.Date;
import java.util.List;

@Service
public class DataServiceImpl implements DataService {

    private final DataRepository repository;

    public DataServiceImpl(DataRepository repository) {
        this.repository = repository;
    }

    @Override
    public Report saveMessage(Report data) {
        return repository.save(data);
    }

    @Override
    public List<Report> getAll() {
        return (List<Report>) repository.findAll();
    }

    @Override
    public List<Report> findByParam(Control control, Date timeAfter, Date timeBefore) {
        return repository.findAllByControlOrTimeGreaterThanEqualOrTimeLessThanEqual(control, timeAfter, timeBefore);
    }

}
