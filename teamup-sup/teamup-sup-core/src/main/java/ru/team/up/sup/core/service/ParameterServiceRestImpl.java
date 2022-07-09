package ru.team.up.sup.core.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.exception.NoContentException;
import ru.team.up.sup.core.repositories.ParameterRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ParameterServiceRestImpl implements ParameterServiceRest {
    private ParameterRepository parameterRepository;


    @Override
    @Transactional(readOnly = true)
    public Optional<Parameter> getParameterByParameterName(String parameterName) {
        Parameter parameter = parameterRepository.getParameterByParameterName(parameterName);
        return Optional.of(parameter);
    }

    @Override
    public Optional<Parameter> getParameterById(Long id) throws NoContentException {
        return parameterRepository.findById(id);
    }

    public List<Parameter> getAllParameters() {
        List<Parameter> parameters = parameterRepository.findAll();
        return parameters;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Parameter> findByParameterNameContains(String strName) {
        List<Parameter> list = parameterRepository.findByParameterNameContains(strName);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Parameter> findByCreationDateBetween(String date1, String date2) {
        LocalDate localDate1 = LocalDate.parse(date1);
        LocalDate localDate2 = LocalDate.parse(date2);
        List<Parameter> list = parameterRepository.findByCreationDateBetween(localDate1, localDate2);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Parameter> findByUpdateDateBetween(String updateDate1, String updateDate2) {
        LocalDateTime localDate1 = LocalDateTime.parse(updateDate1);
        LocalDateTime localDate2 = LocalDateTime.parse(updateDate2);
        List<Parameter> list = parameterRepository.findByUpdateDateBetween(localDate1, localDate2);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Parameter> getParametersBySystemName(AppModuleNameDto systemName) {
        List<Parameter> parameters = parameterRepository.getParametersBySystemName(systemName);
        return parameters;
    }
}
