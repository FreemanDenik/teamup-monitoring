package ru.team.up.teamup;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.ControlDto;
import ru.team.up.dto.InitiatorTypeDto;
import ru.team.up.dto.ReportStatusDto;
import ru.team.up.teamup.entity.Admin;
import ru.team.up.teamup.entity.Report;
import ru.team.up.teamup.entity.Role;
import ru.team.up.teamup.repositories.AdminRepository;
import ru.team.up.teamup.repositories.DataRepository;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("classpath:application.properties")
class TeamupMonitoringApplicationTests {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    DataRepository dataRepository;

    private Admin adminTest;

    private Report reportTest;

    @BeforeAll
    public void setUpEntities() {

        adminTest = Admin.builder()
                .id(-1L)
                .username("testuser")
                .password("123")
                .role(Role.ROLE_ADMIN)
                .build();

        reportTest = Report.builder()
                .reportName("Событие №1")
                .reportStatus(ReportStatusDto.SUCCESS)
                .appModuleName(AppModuleNameDto.TEAMUP_MONITORING)
                .control(ControlDto.AUTO)
                .initiatorId(2L)
                .initiatorType(InitiatorTypeDto.USER)
                .initiatorName("user")
                .time(new Date())
                .parameters(null)
                .build();
    }

    @Test
    @Order(1)
    void testAdminRepository() {

        // Сохранили тестового админа в БД
        adminRepository.save(adminTest);
        Long adminTestId = adminTest.getId();

        // Проверка на наличие тестового админа с полученным ID
        assertNotNull(adminRepository.findById(adminTestId));
        // Проверка на наличие тестового админа с указанным username
        assertNotNull(adminRepository.findByUsername("testuser"));

        // Создали нового тестового админа и получили его из БД по ID
        Admin adminTest2 = adminRepository.findById(adminTestId).get();

        // Проверили данные на совпадение
        assertEquals(adminTest2.getUsername(), adminTest.getUsername());
        assertEquals(adminTest2.getPassword(), adminTest.getPassword());
        assertEquals(adminTest2.getRole(), adminTest.getRole());

        // Удалили тестового админа по ID
        adminRepository.deleteById(adminTestId);

        // Проверили что тестового админа больше нет в БД
        assertEquals(adminRepository.findById(adminTestId), Optional.empty());
    }

    @Test
    @Order(2)
    void testDataRepository() {

        // Сохранили тестовый отчёт в БД
        dataRepository.save(reportTest);
        String reportTestId = reportTest.getId();

        // Проверка на наличие тестового отчёта с полученным ID
        assertNotNull(dataRepository.findById(reportTestId));

        // Получили созданный тестовый отчёт из БД по ID
        Report reportTest2 = dataRepository.findById(reportTestId).get();

        // Проверили данные на совпадение
        assertEquals(reportTest2.getReportName(), reportTest.getReportName());
        assertEquals(reportTest2.getReportStatus(), reportTest.getReportStatus());
        assertEquals(reportTest2.getAppModuleName(), reportTest.getAppModuleName());
        assertEquals(reportTest2.getControl(), reportTest.getControl());
        assertEquals(reportTest2.getInitiatorId(), reportTest.getInitiatorId());
        assertEquals(reportTest2.getInitiatorType(), reportTest.getInitiatorType());
        assertEquals(reportTest2.getInitiatorName(), reportTest.getInitiatorName());
        assertEquals(reportTest2.getTime(), reportTest.getTime());
        assertEquals(reportTest2.getParameters(), reportTest.getParameters());

        // Удалили тестовый отчёт
        dataRepository.deleteById(reportTestId);

        // Проверили что тестового отчёта больше нет в БД
        assertEquals(dataRepository.findById(reportTestId), Optional.empty());
    }
}
