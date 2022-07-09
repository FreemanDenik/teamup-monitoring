package ru.team.up.sup.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.team.up.sup.core.entity.User;
import ru.team.up.sup.core.repositories.UserRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserDetailsImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userDetailsAccount = userRepository.findByEmail(email);
//        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        log.info("Account authorization:{}", email);
        if (userDetailsAccount == null) {
            throw new UsernameNotFoundException("userDetailsAccount is null");
        } else {
            userDetailsAccount.setLastAccountActivity(LocalDateTime.now());
            return userDetailsAccount;
        }
    }
}
