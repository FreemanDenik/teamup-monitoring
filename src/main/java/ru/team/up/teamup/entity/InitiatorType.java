package ru.team.up.teamup.entity;


/**
 * Кто именно создал событие для Кафки:
 * SYSTEM - система
 * USER - пользователь
 * MANAGER - менеджер
 * ADMIN - администратор
 */
public enum InitiatorType {
    SYSTEM, USER, MANAGER, ADMIN
}
