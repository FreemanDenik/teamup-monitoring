// todo - Семенов С.Н.- Убрано по мере разработки функционала
//package ru.team.up.sup.core.initialization;
//
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Component;
//import ru.team.up.dto.AppModuleNameDto;
//import ru.team.up.sup.core.entity.Parameter;
//import ru.team.up.sup.core.repositories.ParameterRepository;
//import ru.team.up.sup.core.service.KafkaSupService;
//
//import javax.annotation.PostConstruct;
//import javax.transaction.Transactional;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//
//import static ru.team.up.dto.SupParameterType.*;
//
//@Component
//@Transactional
//@AllArgsConstructor
//public class ParameterDefaultCreator {
//
//    private final ParameterRepository parameterRepository;
//    private final KafkaSupService kafkaSupService;
//
//    @PostConstruct
//    public void parameterDefaultCreator() {
//        parameterRepository.save(Parameter.builder()
//                .parameterName("testName")
//                .parameterType(STRING)
//                .parameterValue("testValue")
//                .systemName(AppModuleNameDto.TEAMUP_CORE)
//                .creationDate(LocalDate.now())
//                .updateDate(LocalDateTime.now())
//                .build()
//        );
//        parameterRepository.save(Parameter.builder()
//                .parameterName("testName2")
//                .parameterType(STRING)
//                .parameterValue("testValue2")
//                .systemName(AppModuleNameDto.TEAMUP_CORE)
//                .creationDate(LocalDate.now())
//                .updateDate(LocalDateTime.now()
//                ).build()
//        );
//        parameterRepository.save(Parameter.builder()
//                .parameterName("СIAMetingFlag")
//                .parameterType(STRING)
//                .parameterValue("AgentFreed0m")
//                .systemName(AppModuleNameDto.TEAMUP_CORE)
//                .creationDate(LocalDate.of(2020, 12, 12))
//                .updateDate(LocalDateTime.now()
//                ).build()
//        );
//        parameterRepository.save(Parameter.builder()
//                .parameterName("MonetizationLevel")
//                .parameterType(INTEGER)
//                .parameterValue("0")
//                .systemName(AppModuleNameDto.TEAMUP_CORE)
//                .creationDate(LocalDate.of(2019, 12, 12))
//                .updateDate(LocalDate.of(2019, 12, 12).atTime(13, 12)
//                ).build()
//        );
//        parameterRepository.save(Parameter.builder()
//                .parameterName("DestroySystem")
//                .parameterType(BOOLEAN)
//                .parameterValue("false")
//                .systemName(AppModuleNameDto.TEAMUP_KAFKA)
//                .creationDate(LocalDate.of(2019, 12, 12))
//                .updateDate(LocalDate.of(2019, 12, 12).atTime(13, 12)
//                ).build()
//        );
//        parameterRepository.save(Parameter.builder()
//                .parameterName("TEAMUP_CORE_GET_EVENT_BY_ID_ENABLED")
//                .parameterType(BOOLEAN)
//                .parameterValue("true")
//                .systemName(AppModuleNameDto.TEAMUP_CORE)
//                .creationDate(LocalDate.now())
//                .updateDate(LocalDate.now().atTime(LocalTime.now()))
//                .build()
//        );
//        parameterRepository.save(Parameter.builder()
//                .parameterName("TEAMUP_CORE_GET_USER_BY_ID_ENABLED")
//                .parameterType(BOOLEAN)
//                .parameterValue("true")
//                .systemName(AppModuleNameDto.TEAMUP_CORE)
//                .creationDate(LocalDate.now())
//                .updateDate(LocalDate.now().atTime(LocalTime.now()))
//                .build()
//        );
//        parameterRepository.save(Parameter.builder()
//                .parameterName("TEAMUP_CORE_COUNT_RETURN_CITY")
//                .parameterType(INTEGER)
//                .parameterValue("10")
//                .systemName(AppModuleNameDto.TEAMUP_CORE)
//                .creationDate(LocalDate.now())
//                .updateDate(LocalDate.now().atTime(LocalTime.now()))
//                .build()
//        );
//        kafkaSupService.send(parameterRepository.findAll());
//    }
//}
