package ru.team.up.sup.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.team.up.sup.auth.service.UserServiceAuth;
import ru.team.up.sup.auth.service.impl.UserDetailsImpl;
import ru.team.up.sup.core.entity.User;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;


/**
 * Контроллер для регистрации, авторизации
 * и переадресации на страницы в зависимости от роли пользователя
 */
@Slf4j
@Controller
public class MainController {

    private final UserServiceAuth userServiceAuth;
    private final UserDetailsImpl userDetails;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    public MainController(UserServiceAuth userServiceAuth, UserDetailsImpl userDetails) {
        this.userServiceAuth = userServiceAuth;
        this.userDetails = userDetails;
    }

    private User getCurrentAccount() {
        String email;
        if (SecurityContextHolder.getContext().getAuthentication().toString().contains("given_name")) {
            email = ((DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
        } else {
            email = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
        }
        return (User) userDetails.loadUserByUsername(email);
    }

    private void autoLogin(String email, String password, HttpServletRequest request) {
        UserDetails user = userDetails.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password
                , user.getAuthorities());

        try {
            Authentication auth = authenticationManager.authenticate(token);
            if (auth.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(auth);
                request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY
                        , SecurityContextHolder.getContext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        User account = (User) userDetails.loadUserByUsername(email);
        log.debug("Успешная авторизация id:{},  email:{}", account.getId(), account.getEmail());
    }


    /**
     * Стартовая страница
     *
     * @return страница с предложением зарегистрироваться или авторизоваться.
     */
    @GetMapping(value = "")
    public String printWelcomePage() {
        return "welcome";
    }

    /**
     * @return переход на страницу для пользователя с ролью USER
     */
    @GetMapping(value = "/user")
    public String printUserPage(Model model) {
        model.addAttribute("loggedUser", getCurrentAccount());
        return "user";
    }

//    /**
//     * @return переход на страницу для пользователя с ролью ADMIN
//     */
//    @GetMapping(value = "/admin")
//    public String printAdminPage(Model model) {
//        model.addAttribute("loggedUser", getCurrentAccount());
//        return "admin";
//    }

    /**
     * метод для перехода на страницу регистрации
     *
     * @param model - сущность для передачи юзера в html
     * @return страница с формой регистрации
     */
    @GetMapping(value = "/registration")
    public String printRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    /**
     * Метод для сохранения регистрации пользователя
     *
     * @param model - сущность для обмена информацией между методом и html.
     * @param user  - юзер, желающий зарегистрироваться.
     * @return В случае успешной регистрации нового пользователя автоматический вход в защищеную область в соотвествии
     * с максимальной ролью пользователя,
     * в ином случае - registration.html
     */
    @PostMapping(value = "/registration")
    public String registrationNewUser(@ModelAttribute User user, Model model, HttpServletRequest request, BindingResult result) {
        String password = user.getPassword();

        if (userServiceAuth.checkLogin(user.getEmail())) {
            ObjectError error = new ObjectError("login", "Такой никнейм уже занят");
            result.addError(error);
        }
        if (result.hasErrors()) {
            return "/registration";
        }
        userServiceAuth.saveUser(user);
        autoLogin(user.getEmail(), password, request);
        return "redirect:/authority";
    }


    /**
     * Метод для определения защищеной области для входа зарегистрированного пользоватлея
     *
     * @return Переход на ссылку в зависимости от роли, по умолчанию переход на /user
     */
    @GetMapping(value = "/authority")
    public String chooseRole() {

        String role = getCurrentAccount().getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.joining(","));

        if (role.contains("ROLE_ADMIN")) {
            return "redirect:/admin";
        }
        return "redirect:/user";
    }

    @GetMapping(value = "/login")
    public String loginPage(Model model) {
        return "login";
    }

    /**
     * Метод для подстановки в поля формы регистрации данных полученных от сервера аутентификции (Google)
     *
     * @param model          - сущность для обмена информацией между методом и html.
     * @param authentication - объект, с данными пользователя прошедшего аутентификацию на сервере аутентификации.
     * @return возвращает страницу с формой регистрации, с предварительно заполеннными полями "Имя", "Фамимлия" и "email"
     */
    @GetMapping(value = "/oauth2reg")
    public String user(Model model, Authentication authentication) {

        DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
        User user = new User();
        user.setEmail(principal.getEmail());
        user.setName(principal.getGivenName());
        user.setLastName(principal.getFamilyName());


        model.addAttribute("user", user);
        return "registration";
    }
}

