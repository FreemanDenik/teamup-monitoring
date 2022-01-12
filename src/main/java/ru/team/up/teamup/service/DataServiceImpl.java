package ru.team.up.teamup.service;


import org.springframework.stereotype.Service;
import ru.team.up.teamup.entity.Report;
import ru.team.up.teamup.repositories.DataRepository;

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
}
