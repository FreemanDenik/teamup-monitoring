package ru.team.up.teamup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.team.up.core.entity.Admin;
import ru.team.up.core.entity.Role;
import ru.team.up.core.repositories.AdminRepository;
import ru.team.up.core.service.UserDetailsImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест имплементации интерфейса UserDetails")
class UserDetailsImplTest {

    @Mock
    private AdminRepository adminRepository;

    private PasswordEncoder passwordEncoder;

    private UserDetailsImpl userDetailsService;

    @Captor
    private ArgumentCaptor<Admin> adminCaptor;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userDetailsService = new UserDetailsImpl(adminRepository, passwordEncoder);
    }

    @Test
    @DisplayName("Выбрасывание исключения UsernameNotFoundException")
    void loadUserByUsernameNotFoundException() {

        //given
        String notExistingUser = "";

        //when & then
        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> userDetailsService.loadUserByUsername(notExistingUser))
                .withMessage("Пользователь с логином <> не найден.");
        verify(adminRepository, times(1)).findByUsername(any());
    }

    @Test
    @DisplayName("Пользователь найден")
    void loadUserByUsernameFound() {

        //given
        Admin adminNikita = Admin.builder()
                .id(1L)
                .password("123")
                .username("Nikita")
                .role(Role.ROLE_ADMIN)
                .build();

        given(adminRepository.findByUsername("Nikita"))
                .willReturn(adminNikita);

        //when
        Admin actualFoundAdmin = (Admin) userDetailsService.loadUserByUsername("Nikita");

        //then
        assertThat(actualFoundAdmin).isEqualTo(adminNikita);
        verify(adminRepository, times(1)).findByUsername("Nikita");
    }

    @Test
    @DisplayName("Сохранение админа в репозитории")
    void save() {

        //given
        Admin adminTest = Admin.builder()
                .id(1L)
                .password("123")
                .username("Admin")
                .role(Role.ROLE_ADMIN)
                .build();

        //when
        userDetailsService.save(adminTest);

        adminTest.setPassword(passwordEncoder.encode(adminTest.getPassword()));

        //then
        verify(adminRepository, times(1)).save(adminCaptor.capture());
        assertThat(adminTest).isEqualTo(adminCaptor.getValue());
    }
}