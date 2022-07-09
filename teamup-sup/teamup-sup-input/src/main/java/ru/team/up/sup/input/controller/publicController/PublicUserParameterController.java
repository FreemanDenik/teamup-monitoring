package ru.team.up.sup.input.controller.publicController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.team.up.sup.core.service.ParameterService;

@Slf4j
@Controller
public class PublicUserParameterController {

    private final ParameterService parameterService;

    @Autowired
    public PublicUserParameterController(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @GetMapping("/user/parameters")
    public String userParametersPage(ModelMap model) {
        model.addAttribute("allParameters", parameterService.getAllParameters());
        return "newUser";
    }


}
