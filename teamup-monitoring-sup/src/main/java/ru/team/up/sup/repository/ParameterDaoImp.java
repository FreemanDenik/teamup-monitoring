package ru.team.up.sup.repository;

import org.springframework.stereotype.Repository;
import ru.team.up.dto.SupParameterDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ParameterDaoImp implements ParameterDao {

    private final Map<String, SupParameterDto<?>> supParameterDtoMap = new HashMap<>();

    @Override
    public void add(SupParameterDto<?> supParameterDto) {
        supParameterDtoMap.put(supParameterDto.getParameterName(), supParameterDto);
    }

    @Override
    public SupParameterDto<?> findByName(String name) {
        return supParameterDtoMap.get(name);
    }

    @Override
    public List<SupParameterDto<?>> findAll() {
        return new ArrayList<>(supParameterDtoMap.values());
    }
}