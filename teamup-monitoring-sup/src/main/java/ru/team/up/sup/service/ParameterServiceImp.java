package ru.team.up.sup.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.ListSupParameterDto;
import ru.team.up.dto.SupParameterDto;
import ru.team.up.dto.SupParameterType;
import ru.team.up.sup.entity.SupParameter;
import ru.team.up.sup.repository.ParameterDao;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Data
@Slf4j
@Service
public class ParameterServiceImp implements ParameterService {

    private final ParameterDao parameterDao;
    private final ParameterSender parameterSender;
    private final KafkaSupService kafkaSupService;
    private Set<SupParameter<?>> parameterSet = Set.of(
            loginEnabled,
            loginByGoogleEnabled,
            registrationEnabled,
            printWelcomePageEnabled,
            printAdminPageEnabled,
            chooseRoleEnabled,
            printModeratorPageEnabled,
            oauth2regUserEnabled,
            printRegistrationPageEnabled,
            printUserPageEnabled,
            getEventByIdEnabled,
            getUserByIdEnabled,
            countReturnCity,
            getCityByNameEnabled,
            getCityByNameInSubjectEnabled,
            getAllCitiesEnabled,
            getSomeCitiesByNameEnabled,
            getIsAvailableUsernameEnabled,
            getIsAvailableEmailEnabled,
            getAllEventsPrivateEnabled,
            getAllEventByCityEnabled,
            getFindEventsByNameEnabled,
            getFindEventsByAuthorEnabled,
            getFindEventsByTypeEnabled,
            getCreateEventEnabled,
            getUpdateEventEnabled,
            getDeleteEventEnabled,
            getAddEventParticipantEnabled,
            getDeleteEventParticipantEnabled,
            getInterestsUserByIdEnabled,
            getEnabled,
            getUserByEmailEnabled,
            getUserByUsernameEnabled,
            getUsersListEnabled,
            getEventsByOwnerIdEnabled,
            getEventsBySubscriberIdEnabled,
            getUpdateUserEnabled,
            getDeleteUserByIdEnabled,
            getTopUsersListInCityEnabled,
            getSupDefaultParamURL);

    @PostConstruct
    private void init() {
        createDefaultParamFile();
        parameterSender.sendDefaultsToSup();
        kafkaSupService.getAllModuleParameters();
    }

    @Override
    public List<SupParameterDto<?>> getAll() {
        return parameterDao.findAll();
    }

    @Override
    public SupParameterDto<?> getParamByName(String name) {
        return parameterDao.findByName(name);
    }

    @Override
    public void addParam(SupParameterDto<?> parameter) {
        parameterDao.add(parameter);
        updateStaticField(parameter);
    }

    private void createDefaultParamFile() {
        ListSupParameterDto defaultList = new ListSupParameterDto();
        ObjectMapper mapper = new ObjectMapper();
        for (SupParameter<?> parameter : parameterSet) {
            SupParameterDto<?> dto = SupParameterDto.builder()
                    .parameterName(parameter.getName())
                    .systemName(AppModuleNameDto.TEAMUP_CORE)
                    .parameterValue(parameter.getValue())
                    .parameterType(SupParameterType.valueOf(parameter.getValue().getClass().getSimpleName().toUpperCase()))
                    .build();
            parameterDao.add(dto);
            defaultList.addParameter(dto);
        }
        try {
            mapper.writeValue(new File("./Parameters.json"), defaultList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateStaticField(SupParameterDto<?> newParam) {
        for (SupParameter oldParam : parameterSet) {
            if (newParam.getParameterName().equals(oldParam.getName())) {
                log.debug("Параметр {} со значением {}", oldParam.getName(), oldParam.getValue());
                oldParam.setValue(newParam.getParameterValue());
                log.debug("Теперь имеет значение {}", oldParam.getValue());
                break;
            }
        }
    }
}