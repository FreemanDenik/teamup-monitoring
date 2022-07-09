package ru.team.up.sup.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessHandler successHandler;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(SuccessHandler successHandler, @Qualifier("userDetailsImpl") UserDetailsService userDetailsService) {
        this.successHandler = successHandler;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/registration", "/login", "/oauth2reg", "/oauth2/authorization/google", "/authority").permitAll()
                .antMatchers("/public/api/**").permitAll()
                .antMatchers("/admin/**", "/private/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").successHandler(successHandler)
                .loginProcessingUrl("/login").usernameParameter("auth_email").passwordParameter("auth_password").permitAll()
                .and()
                .oauth2Login().loginPage("/oauth2/authorization/google")
                .successHandler(successHandler);

        http.logout()//URL выхода из системы безопасности Spring - только POST. Вы можете поддержать выход из системы без POST, изменив конфигурацию Java
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))//выход из системы гет запрос на /logout
                .logoutSuccessUrl("/")//успешный выход из системы
                .and().csrf().disable();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

}
