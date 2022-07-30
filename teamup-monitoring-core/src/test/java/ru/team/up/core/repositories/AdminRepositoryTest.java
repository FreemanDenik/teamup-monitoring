package ru.team.up.core.repositories;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.team.up.core.entity.Admin;
import ru.team.up.core.entity.Role;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Тест репозитория")
class AdminRepositoryTest {

    @Autowired
    AdminRepository adminTestRepository;

    private Admin adminTest;

    @Test
    @Order(1)
    @DisplayName("Тест метода save репозитория.")
    void findByUsername() {

        //given
        adminTest = Admin.builder()
                .id(-1L)
                .username("testuser")
                .password("123")
                .role(Role.ROLE_ADMIN)
                .build();
        adminTestRepository.save(adminTest);

        //when
        Admin adminTest2 = adminTestRepository.findById(adminTest.getId()).get();
        Admin adminTest3 = adminTestRepository.findByUsername("testuser");

        //then
        assertThat(adminTest2).isNotNull();
        assertThat(adminTest3).isNotNull();
        assertThat(adminTest2).isEqualTo(adminTest3);
        assertThat(adminTest2).isEqualTo(adminTest);
    }

    @Test
    @Order(2)
    @DisplayName("Тест метода deleteById репозитория.")
    void deleteById() {
        //given
        Long adminTestId = adminTest.getId();
        // when
        adminTestRepository.deleteById(adminTestId);
        // then
        assertThat(adminTestRepository.findById(adminTestId)).isEqualTo(Optional.empty());
    }

}