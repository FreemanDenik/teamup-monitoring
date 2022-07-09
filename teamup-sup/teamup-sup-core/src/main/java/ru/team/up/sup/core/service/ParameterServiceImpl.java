package ru.team.up.sup.core.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.ListSupParameterDto;
import ru.team.up.dto.SupParameterDto;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.exception.NoContentException;
import ru.team.up.sup.core.repositories.ParameterRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ParameterServiceImpl implements ParameterService {

    private Long daysToSaveParam = 7L;
    private final ParameterRepository parameterRepository;
    private final KafkaSupService kafkaSupService;

    @Autowired
    public ParameterServiceImpl(ParameterRepository parameterRepository,
                                KafkaSupService kafkaSupService) {
        this.parameterRepository = parameterRepository;
        this.kafkaSupService = kafkaSupService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Parameter> getAllParameters() throws NoContentException {
        log.debug("Старт метода List<Parameter> getAllParameters()");
        List<Parameter> parameters = parameterRepository.findAll();
        if (parameters.isEmpty()) {
            throw new NoContentException();
        }
        log.debug("Получили список всех параметров из БД");
        return parameters;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Parameter> getParametersBySystemName(AppModuleNameDto systemName) throws NoContentException {
        log.debug("Старт метода List<Parameter> getParametersBySystemName(String systemName)");
        List<Parameter> parameters = parameterRepository.getParametersBySystemName(systemName);
        if (parameters.isEmpty()) {
            throw new NoContentException();
        }
        log.debug("Получили список всех параметров по systemName {} из БД", systemName);
        return parameters;
    }

    @Override
    @Transactional(readOnly = true)
    public Parameter getParameterById(Long id) throws NoContentException {
        log.debug("Старт метода Parameter getParameterById(Long id) с id = {}", id);
        Parameter parameter = parameterRepository.findById(id)
                .orElseThrow(() -> new NoContentException());
        log.debug("Получили параметр {} из БД", parameter);
        return parameter;
    }

    @Override
    @Transactional(readOnly = true)
    public Parameter getParameterByParameterName(String parameterName) throws NoContentException {
        log.debug("Старт метода Parameter getParameterByParameterName(String parameterName) с параметром {}", parameterName);
        Parameter parameter = parameterRepository.getParameterByParameterName(parameterName);
        if (parameter == null) {
            throw new NoContentException();
        }
        log.debug("Получили из БД параметр {}", parameter);
        return parameter;
    }

    @Override
    public Parameter saveParameter(Parameter parameter) {
        log.debug("Старт метода Parameter saveParameter(Parameter parameter) с параметром {}", parameter);
        parameter.setCreationDate(LocalDate.now());
        parameter.setUpdateDate(LocalDateTime.now());
        Parameter savedParam = parameterRepository.save(parameter);
        kafkaSupService.send(parameter);
        log.debug("Сохранили параметр {} в БД", savedParam);
        return savedParam;
    }

    @Override
    public void deleteParameter(Long id) throws NoContentException {
        log.debug("Старт метода void deleteParameter(Parameter parameter) с id = {}", id);
        log.debug("Проверка существования параметра в БД с id = {}", id);
        parameterRepository.findById(id).orElseThrow(() -> new NoContentException());
        parameterRepository.deleteById(id);
        log.debug("Удалили параметр с id = {} из БД", id);
    }

    @Override
    public Parameter editParameter(Parameter parameter) {
        log.debug("Старт метода Parameter editParameter(Parameter parameter) с параметром {}", parameter);
        parameter.setCreationDate(parameterRepository.findById(parameter.getId())
                .orElseThrow(() -> new NoContentException())
                .getCreationDate());
        parameter.setUpdateDate(LocalDateTime.now());
        Parameter editedParam = parameterRepository.save(parameter);
        log.debug("Изменили параметр в БД {}", editedParam);
        kafkaSupService.send(parameter);
        return editedParam;
    }

    @Override
    public void compareWithDefaultAndUpdate(ListSupParameterDto dtoList) {
        if (dtoList == null ||
                dtoList.getModuleName() == null ||
                dtoList.getList() == null ||
                dtoList.getList().isEmpty()) {
            throw new RuntimeException("Получен пустой лист параметров по умолчанию");
        }
        List<Parameter> bdParams = parameterRepository.getParametersBySystemName(dtoList.getModuleName());
        Set<String> defaultNames = dtoList.getList().stream().map(p -> p.getParameterName()).collect(Collectors.toSet());
        Set<String> bdNames = bdParams.stream().map(p -> p.getParameterName()).collect(Collectors.toSet());

        bdParams.stream()
                .filter(p -> defaultNames.contains(p.getParameterName()))
                .forEach(this::markAsInUseAndSave);

        bdParams.stream()
                .filter(p -> !defaultNames.contains(p.getParameterName()))
                .forEach(this::markAsNotInUseAndSave);

        dtoList.getList().stream()
                .filter(p -> !bdNames.contains(p.getParameterName()))
                .forEach(this::addNewDefaultToDb);
    }

    @Override
    public void purge() {
        parameterRepository.findAll().stream()
                .filter(p -> p.getInUse() == null || !p.getInUse())
                .filter(this::parameterLastUsedDateFilter)
                .forEach(p -> parameterRepository.deleteById(p.getId()));
    }

    private boolean parameterLastUsedDateFilter(Parameter parameter) {
        LocalDate date = parameter.getLastUsedDate();
        return date == null || LocalDate.now().isAfter(date.plusDays(daysToSaveParam));
    }

    private void markAsNotInUseAndSave(Parameter parameter) {
        log.debug("Параметр {} не используется в модуле {}",
                parameter.getParameterName(),
                parameter.getSystemName());
        parameter.setInUse(false);
        parameterRepository.save(parameter);
    }

    private void markAsInUseAndSave(Parameter parameter) {
        parameter.setInUse(true);
        parameter.setLastUsedDate(LocalDate.now());
        parameterRepository.save(parameter);
        log.debug("Параметр {} используется в модуле {}. Дата последнего использования изменена на {}.",
                parameter.getParameterName(),
                parameter.getSystemName(),
                parameter.getLastUsedDate());
    }

    private void addNewDefaultToDb(SupParameterDto defaultParam) {
        log.debug("Параметр {} используется в модуле {}, но его нет в БД.",
                defaultParam.getParameterName(),
                defaultParam.getSystemName());
        parameterRepository.save(Parameter.builder()
                .parameterName(defaultParam.getParameterName())
                .parameterType(defaultParam.getParameterType())
                .systemName(defaultParam.getSystemName())
                .parameterValue(defaultParam.getParameterValue().toString())
                .creationDate(LocalDate.now())
                .inUse(true)
                .lastUsedDate(LocalDate.now())
                .build());
        log.debug("Параметр {} добавлен в БД.", defaultParam.getParameterName());
    }

}
