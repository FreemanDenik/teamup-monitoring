package ru.team.up.core.controller;

import org.springframework.stereotype.Component;
import ru.team.up.core.entity.Admin;
import ru.team.up.core.entity.Role;
import ru.team.up.core.service.UserDetailsImpl;

import javax.annotation.PostConstruct;

@Deprecated
@Component
public class TestUser {
    private final UserDetailsImpl userDetails;

    public TestUser(UserDetailsImpl userDetails) {
        this.userDetails = userDetails;
    }

    @PostConstruct
    public void test() {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setPassword("123");
        admin.setUsername("Admin");
        admin.setRole(Role.ROLE_ADMIN);
        userDetails.save(admin);
    }
}
