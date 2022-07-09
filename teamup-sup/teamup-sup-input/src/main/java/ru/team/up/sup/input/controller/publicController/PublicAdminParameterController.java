package ru.team.up.sup.input.controller.publicController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.SupParameterType;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.exception.NoContentException;
import ru.team.up.sup.core.service.ParameterService;
import ru.team.up.sup.core.service.UserService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
public class PublicAdminParameterController {

    private final ParameterService parameterService;
    private final UserService userService;

    @Autowired
    public PublicAdminParameterController(ParameterService parameterService, UserService userService) {
        this.parameterService = parameterService;
        this.userService = userService;
    }

    @GetMapping()
    public String adminParametersPage(ModelMap model) {
        List<Parameter> allParams = null;
        try {
            allParams = parameterService.getAllParameters();
        } catch (NoContentException e) {
            log.debug("Получен пустой лист с параметрами");
        }
        model.addAttribute("newParameter", new Parameter());
        model.addAttribute("allParams", allParams);
        return "admin/Admin";
    }

    @PostMapping("/edit")
    public String editParameter(@ModelAttribute("parameter") Parameter parameter, Principal principal) {
        parameter.setUserWhoLastChangeParameters(userService.getUserByName(principal.getName()));
        parameterService.editParameter(parameter);
        return "redirect:/admin";
    }

    @PostMapping("/add")
    public String createParameter(@ModelAttribute("newParameter") Parameter parameter, Principal principal) {
        parameter.setUserWhoLastChangeParameters(userService.getUserByName(principal.getName()));
        parameterService.saveParameter(parameter);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable long id) {
        parameterService.deleteParameter(id);
        return "redirect:/admin";
    }

    @ModelAttribute
    public void addAttributes(Principal principal, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("allSystems", AppModuleNameDto.values());
        model.addAttribute("systemsCount", Arrays.stream(AppModuleNameDto.values()).count());
        model.addAttribute("allTypes", SupParameterType.values());
        model.addAttribute("typesCount", Arrays.stream(SupParameterType.values()).count());
    }
}
