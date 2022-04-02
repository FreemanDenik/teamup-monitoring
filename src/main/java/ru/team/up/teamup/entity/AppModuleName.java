package ru.team.up.teamup.entity;

import java.util.Random;

/**
 * @author Степан Глущенко
 * Имена модулей приложения, которые будут принимать парамтеры от системы SUP.
 */

public enum AppModuleName {
    TEAMUP_APP,
    TEAMUP_AUTH,
    TEAMUP_CORE,
    TEAMUP_DTO,
    TEAMUP_EXTERNAL,
    TEAMUP_INPUT,
    TEAMUP_KAFKA,
    TEAMUP_MONITORING,
    TEAMUP_MODERATOR,
    TEAMUP_SUP


/*
    // Сделано для теста (demo/Producer) пусть пока останется
    private static Random random = new Random();

    public static <T extends Enum> T getAppModule(Class<T> clazz) {
        return clazz.getEnumConstants()[random.nextInt(clazz.getEnumConstants().length)];
    }
*/
}
