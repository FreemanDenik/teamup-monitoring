package ru.team.up.core.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.team.up.core.entity.Admin;
import ru.team.up.core.repositories.AdminRepository;

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

        UserDetails userDetails = adminRepository.findByUsername(username);

        if (userDetails == null){
            throw new UsernameNotFoundException("Пользователь с логином <"+username+"> не найден.");
        }

        return userDetails;
    }

    public void save(Admin admin) {
        admin.setPassword(pass.encode(admin.getPassword()));
        adminRepository.save(admin);
    }
}
