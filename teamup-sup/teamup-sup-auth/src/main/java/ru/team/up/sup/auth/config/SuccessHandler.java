package ru.team.up.sup.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.team.up.sup.auth.service.impl.UserDetailsImpl;
import ru.team.up.sup.core.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@Slf4j
@Component
public class SuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserDetailsImpl userService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException  {


        if (authentication.toString().contains("given_name")) {

            try {
                User account = (User) userService.loadUserByUsername(((DefaultOidcUser) authentication.getPrincipal()).getEmail());

                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                                SecurityContextHolder.getContext().getAuthentication().getCredentials(),
                                Collections.singleton(account.getRole())));
            } catch (UsernameNotFoundException e) {
                httpServletResponse.sendRedirect("/oauth2reg");
                return;
            }
        }
        User account = (User) authentication.getPrincipal();
        log.debug("Успешная авторизация id:{},  email:{}", account.getId(), account.getEmail());
        Set<String> roles = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        if (roles.contains("ROLE_ADMIN")) {
            httpServletResponse.sendRedirect("/admin");
        } else if (roles.contains("ROLE_USER")) {
            httpServletResponse.sendRedirect("/user");
        } else {
            httpServletResponse.sendRedirect("/welcome");
        }
    }
}