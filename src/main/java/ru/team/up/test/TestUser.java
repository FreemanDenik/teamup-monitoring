package ru.team.up.test;

import org.springframework.stereotype.Controller;
import ru.team.up.teamup.entity.Admin;
import ru.team.up.teamup.entity.Role;
import ru.team.up.teamup.repositories.AdminRepository;

import javax.annotation.PostConstruct;

@Controller
public class TestUser {

    private final AdminRepository adminRepository;

    public TestUser(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @PostConstruct
    public void test() {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setRole(Role.ROLE_ADMIN);
        adminRepository.save(admin);
    }
}
