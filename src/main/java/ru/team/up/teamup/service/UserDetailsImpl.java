package ru.team.up.teamup.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.team.up.teamup.entity.Admin;
import ru.team.up.teamup.repositories.AdminRepository;

@Service
public class UserDetailsImpl implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder pass;

    public UserDetailsImpl(AdminRepository adminRepository, PasswordEncoder pass) {
        this.adminRepository = adminRepository;
        this.pass = pass;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.findByUsername(username);
    }

    public void save(Admin admin) {
        admin.setPassword(pass.encode(admin.getPassword()));
        adminRepository.save(admin);
    }
}
