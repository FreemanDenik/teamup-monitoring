package ru.team.up.core.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.team.up.core.entity.Admin;
import ru.team.up.core.entity.Role;
import ru.team.up.core.repositories.AdminRepository;
import ru.team.up.core.service.UserDetailsImpl;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Тест страницы логина")
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("Вложенные тесты")
    class NestedTestClass {

        @Autowired
        AdminRepository adminTestRepository;

        @Autowired
        private UserDetailsImpl userDetails;

        private Admin admin;

        @BeforeEach
        void beforeAll() {
            admin = Admin.builder()
                    .id(-1L)
                    .password("123")
                    .username("testuser")
                    .role(Role.ROLE_ADMIN)
                    .build();

            userDetails.save(admin);
        }

        @AfterEach
        void afterAll() {
            adminTestRepository.deleteById(admin.getId());
        }

        @Test
        @DisplayName("Успешный логин и редирект на страницу админа.")
        void loginSuccessfull() throws Exception {

            mockMvc
                    .perform(formLogin().user("testuser").password("123"))
                    .andDo(print())
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/admin/"));
        }
    }

    @Test
    @DisplayName("Незарегистрированный пользователь перенаправляется на страницу логина.")
    void homeRedirection () throws Exception {
        mockMvc
                .perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("Страница логина доступна")
    void loginPage() throws Exception {
        mockMvc
                .perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Пользователь с неправильным логином и паролем не может войти в систему.")
    void loginBadCredentials() throws Exception {
        mockMvc
                .perform(post("/login")
                        .param("username","Vasya")
                        .param("password","Pupkin"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?error"));
    }

}