package ru.team.up.core.entity;

import com.querydsl.core.annotations.QueryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.team.up.dto.*;

import java.util.Date;
import java.util.Map;

/**
 * Сущность событие для Кафки
 */
@Builder
@QueryEntity
@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    /**
     * ID
     */
    @Id
    String id;

    /**
     * Название события
     */
    String reportName;

    /**
     * Тип события (Ручное, Автоматическое)
     */
    ControlDto control;

    /**
     * Модуль из которого отправлено событие
     */
    AppModuleNameDto appModuleName;

    /**
     * Инициатор события (Система, Пользователь, Менеджер, Администратор)
     */
    InitiatorTypeDto initiatorType;

    /**
     * Имя инициатора события
     */
    String initiatorName;

    /**
     * ID инициатора события
     */
    Long initiatorId;

    /**
     * Время создания события
     */
    Date time;

    /**
     * Статус события (Успешно, Неуспешно)
     */
    ReportStatusDto reportStatus;

    /**
     * Список дополнительных параметров
     */
    Map<String, ParametersDto> parameters;


}
