package ru.team.up.teamup.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.InitiatorTypeDto;
import ru.team.up.dto.ParametersDto;
import ru.team.up.teamup.entity.Report;
import ru.team.up.teamup.service.DataService;

import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class MonitoringController {
    Logger logger = LoggerFactory.getLogger(MonitoringController.class);

    private final DataService dataService;

    public MonitoringController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("reports", dataService.getAll());
        model.addAttribute("modules", AppModuleNameDto.values());
        model.addAttribute("initiator", InitiatorTypeDto.values());
        return "reportPage";
    }

    @RequestMapping( "/find")
    public String find(Model model,
                       AppModuleNameDto moduleName,
                       InitiatorTypeDto initiatorType,
                       String timeAfter,
                       String timeBefore) throws ParseException {
        logger.debug("Запрос поиска Событий");

        List<Report> reports = dataService.findByParam(moduleName, initiatorType, timeAfter, timeBefore);
        logger.debug("Результаты поиска : {}", reports);

        model.addAttribute("initiator", InitiatorTypeDto.values());
        model.addAttribute("modules", AppModuleNameDto.values());
        model.addAttribute("reports", reports);
        return "reportPage";
    }



}
