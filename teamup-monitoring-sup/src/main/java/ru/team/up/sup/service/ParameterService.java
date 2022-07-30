package ru.team.up.sup.service;

import ru.team.up.dto.SupParameterDto;
import ru.team.up.sup.entity.SupParameter;

import java.util.List;

/**
 * Интерфейс сервиса для получения локальных параметров и установки значений по умолчанию
 */
public interface ParameterService {

    /**
     * Параметры модуля по умолчанию
     */
    /*--------------AUTH module--------------*/
    SupParameter<Boolean> loginEnabled = new SupParameter<>(
            "TEAMUP_CORE_LOGIN_ENABLED",
            true);
    SupParameter<Boolean> loginByGoogleEnabled = new SupParameter<>(
            "TEAMUP_CORE_LOGIN_BY_GOOGLE_ENABLED",
            true);
    SupParameter<Boolean> registrationEnabled = new SupParameter<>(
            "TEAMUP_CORE_REGISTRATION_ENABLED",
            true);
    SupParameter<Boolean> printWelcomePageEnabled = new SupParameter<>(
            "TEAMUP_CORE_PRINT_WELCOME_PAGE_ENABLED",
            true);
    SupParameter<Boolean> printAdminPageEnabled = new SupParameter<>(
            "TEAMUP_CORE_PRINT_ADMIN_PAGE_ENABLED",
            true);
    SupParameter<Boolean> chooseRoleEnabled = new SupParameter<>(
            "TEAMUP_CORE_CHOOSE_ROLE_ENABLED",
            true);
    SupParameter<Boolean> printModeratorPageEnabled = new SupParameter<>(
            "TEAMUP_CORE_PRINT_MODERATOR_PAGE_ENABLED",
            true);
    SupParameter<Boolean> oauth2regUserEnabled = new SupParameter<>(
            "TEAMUP_CORE_PRINT_OAUTH_2_REG_USER_ENABLED",
            true);
    SupParameter<Boolean> printRegistrationPageEnabled = new SupParameter<>(
            "TEAMUP_CORE_PRINT_REGISTRATION_PAGE_ENABLED",
            true);
    SupParameter<Boolean> printUserPageEnabled = new SupParameter<>(
            "TEAMUP_CORE_PRINT_USER_PAGE_ENABLED",
            true);
    /*--------------INPUT module--------------*/
    SupParameter<Boolean> getEventByIdEnabled = new SupParameter<>(
            "TEAMUP_CORE_GET_EVENT_BY_ID_ENABLED",
            true);
    SupParameter<Boolean> getUserByIdEnabled = new SupParameter<>(
            "TEAMUP_CORE_GET_USER_BY_ID_ENABLED",
            true);
    SupParameter<Boolean> getAllEventsPrivateEnabled = new SupParameter<>(
            "TEAMUP_CORE_GET_ALL_EVENTS_ENABLED",
            true);
    SupParameter<Boolean> createEventEnabled = new SupParameter<>(
            "TEAMUP_CORE_CREATE_EVENT_ENABLED",
            true);
    SupParameter<Boolean> updateNumberOfParticipantsEnabled = new SupParameter<>(
            "TEAMUP_CORE_UPDATE_NUMBER_OF_PARTICIPANTS_ENABLED",
            true);
    SupParameter<Boolean> getOneEventEnabled = new SupParameter<>(
            "TEAMUP_CORE_GET_ONE_EVENT_ENABLED",
            true);
    SupParameter<Boolean> updateEventEnabled = new SupParameter<>(
            "TEAMUP_CORE_UPDATE_EVENT_ENABLED",
            true);
    SupParameter<Boolean> deleteAdminEnabled = new SupParameter<>(
            "TEAMUP_CORE_DELETE_ADMIN_ENABLED",
            true);
    SupParameter<Boolean> sendApplicationEnabled = new SupParameter<>(
            "TEAMUP_CORE_SEND_APPLICATION_ENABLED",
            true);
    SupParameter<Boolean> getAllApplicationsByEventIdEnabled = new SupParameter<>(
            "TEAMUP_CORE_GET_ALL_APPLICATION_BY_EVENT_ENABLED",
            true);
    SupParameter<Boolean> getAllApplicationsByUserIdEnabled = new SupParameter<>(
            "TEAMUP_CORE_GET_ALL_APPLICATION_BY_USER_ID_ENABLED",
            true);
    SupParameter<Boolean> getAllUsersEnabled = new SupParameter<>(
            "TEAMUP_CORE_GET_ALL_USERS_ENABLED",
            true);
    SupParameter<Boolean> createUserEnabled = new SupParameter<>(
            "TEAMUP_CORE_CREATE_USER_ENABLED",
            true);
    SupParameter<Boolean> getUserByIdPrivateEnabled = new SupParameter<>(
            "TEAMUP_CORE_GET_USER_BY_ID_PRIVATE_ENABLED",
            true);
    SupParameter<Boolean> updateUserEnabled = new SupParameter<>(
            "TEAMUP_CORE_UPDATE_USER_ENABLED",
            true);
    SupParameter<Boolean> deleteUserEnabled = new SupParameter<>(
            "TEAMUP_CORE_DELETE_USER_ENABLED",
            true);
    SupParameter<Boolean> getAllModeratorsEnabled = new SupParameter<>(
            "TEAMUP_CORE_GET_ALL_MODERATORS_ENABLED",
            true);
    SupParameter<Boolean> createModeratorEnabled = new SupParameter<>(
            "TEAMUP_CORE_CREATE_MODERATOR_ENABLED",
            true);
    SupParameter<Boolean> getOneModeratorEnabled = new SupParameter<>(
            "TEAMUP_CORE_GET_ONE_MODERATOR_ENABLED",
            true);
    SupParameter<Boolean> updateModeratorEnabled = new SupParameter<>(
            "TEAMUP_CORE_UPDATE_MODERATOR_ENABLED",
            true);
    SupParameter<Boolean> deleteModeratorEnabled = new SupParameter<>(
            "TEAMUP_CORE_DELETE_MODERATOR_ENABLED",
            true);
    SupParameter<Boolean> getAssignedEventsOfModeratorEnabled = new SupParameter<>(
            "TEAMUP_CORE_GET_ASSIGNED_EVENTS_OF_MODERATOR_ENABLED",
            true);
    SupParameter<Boolean> sendEmailUserMessageEnabled = new SupParameter<>(
            "TEAMUP_CORE_SEND_EMAIL_USER_MESSAGE_ENABLED",
            true);
    SupParameter<Boolean> getAllAdminsEnabled = new SupParameter<>(
            "TEAMUP_CORE_GET_ALL_ADMINS_ENABLED",
            true);
    SupParameter<Boolean> createAdminEnabled = new SupParameter<>(
            "TEAMUP_CORE_CREATE_ADMIN_ENABLED",
            true);
    SupParameter<Boolean> getOneAdminEnabled = new SupParameter<>(
            "TEAMUP_CORE_GET_ONE_ADMIN_ENABLED",
            true);
    SupParameter<Boolean> updateAdminEnabled = new SupParameter<>(
            "TEAMUP_CORE_UPDATE_ADMIN_ENABLED",
            true);
    SupParameter<Boolean> deleteAdminFromAdminControllerEnabled = new SupParameter<>(
            "TEAMUP_CORE_DELETE_ADMIN_FROM_ADMIN_CONTROLLER_ENABLED",
            true);
    /*--------------CORE module--------------*/
    SupParameter<Integer> countReturnCity = new SupParameter<>(
            "TEAMUP_CORE_COUNT_RETURN_CITY",
            10);
    SupParameter<Boolean> getCityByNameEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_RETURN_CITY_FOR_TITLE",
            true);
    SupParameter<Boolean> getCityByNameInSubjectEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_RETURN_CITY_FOR_TITLE_IN_SUBJECT",
            true);
    SupParameter<Boolean> getAllCitiesEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_RETURN_ALL_CITY",
            true);
    SupParameter<Boolean> getSomeCitiesByNameEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_RETURN_CITY_SUITABLE_FOR_TITLE",
            true);
    SupParameter<Boolean> getIsAvailableUsernameEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_RETURN_AVAILABILITY_CHECK_SURNAME",
            true);
    SupParameter<Boolean> getIsAvailableEmailEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_RETURN_AVAILABILITY_CHECK_EMAIL",
            true);
    SupParameter<Boolean> getAllEventsEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_RETURN_ALL_EVENTS",
            true);
    SupParameter<Boolean> getAllEventByCityEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_RETURN_ALL_EVENTS_BY_CITY",
            true);
    SupParameter<Boolean> getFindEventsByNameEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_RETURN_EVENTS_BY_NAME",
            true);
    SupParameter<Boolean> getFindEventsByAuthorEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_RETURN_EVENTS_BY_AUTHOR",
            true);
    SupParameter<Boolean> getFindEventsByTypeEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_RETURN_EVENTS_BY_TYPE",
            true);
    SupParameter<Boolean> getCreateEventEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_RETURN_CREATE_EVENT",
            true);
    SupParameter<Boolean> getUpdateEventEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_RETURN_UPDATE_EVENT",
            true);
    SupParameter<Boolean> getDeleteEventEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_DELETE_EVENT",
            true);
    SupParameter<Boolean> getAddEventParticipantEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_ADD_EVENT_PARTICIPANT",
            true);
    SupParameter<Boolean> getDeleteEventParticipantEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_DELETE_EVENT_PARTICIPANT",
            true);
    SupParameter<Boolean> getInterestsUserByIdEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_RETUTN_INTEREST_USERS_BY_ID",
            true);
    SupParameter<Boolean> getEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_RETUTN_ALL_MODULE_PARAMETERS",
            true);
    SupParameter<Boolean> getUserByEmailEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_USER_BY_EMAIL",
            true);
    SupParameter<Boolean> getUserByUsernameEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_USER_BY_USERNAME",
            true);
    SupParameter<Boolean> getUsersListEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_ALL_USERS",
            true);
    SupParameter<Boolean> getEventsByOwnerIdEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_EVENTS_BY_ID_USERS",
            true);
    SupParameter<Boolean> getEventsBySubscriberIdEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_EVENTS_BY_SUBSCRIBER_ID_USER",
            true);
    SupParameter<Boolean> getUpdateUserEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_UPDATE_USER",
            true);
    SupParameter<Boolean> getDeleteUserByIdEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_DELETE_USER",
            true);
    SupParameter<Boolean> getTopUsersListInCityEnabled = new SupParameter<>(
            "TEAMUP_CORE_COUNT_TOP_USERS_IN_CITY",
            true);
    SupParameter<Integer> getModeratorDisconnectTimeout = new SupParameter<>(
            "TEAMUP_CORE_MODERATOR_DISCONNECT_TIMEOUT",
            30);
    SupParameter<Integer> getModeratorEventLimitation = new SupParameter<>(
            "TEAMUP_CORE_MODERATOR_EVENT_LIMITATION",
            3);
    SupParameter<String> getSupDefaultParamURL = new SupParameter<>(
            "TEAMUP_CORE_DEFAULT_PARAM_URL",
            "http://localhost:8083/public/api/update/TEAMUP_CORE/");
    SupParameter<String> getNotificationUriHost = new SupParameter<>(
            "TEAMUP_CORE_NOTIFICATION_URI_HOST",
            "http://localhost:8085"
    );

    /**
     * Получение листа текущих параметров из кэша
     */
    List<SupParameterDto<?>> getAll();

    /**
     * Добавление или перезапись параметра
     */
    void addParam(SupParameterDto<?> parameter);

    /**
     * Поиск параметра по имени
     */
    SupParameterDto<?> getParamByName(String name);

}