package ru.team.up.teamup.entity;

import com.querydsl.core.annotations.QueryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

/**
 * Сущность событие для Кафки
 */
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
     * Тип события (Ручное, Автоматическое)
     */
    Control control;

    /**
     * Модуль из которого отправлено событие
     */
    AppModuleName appModuleName;

    /**
     * Инициатор события (Система, Пользователь, Менеджер, Администратор)
     */
    InitiatorType initiatorType;

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
    Status status;

    /**
     * Список дополнительных параметров
     */
    Map<String, Object> parameters;
}
