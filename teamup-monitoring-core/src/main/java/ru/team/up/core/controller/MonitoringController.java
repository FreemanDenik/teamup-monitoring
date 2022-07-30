package ru.team.up.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.team.up.core.entity.Report;
import ru.team.up.core.models.FindViewModel;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.InitiatorTypeDto;
import ru.team.up.core.models.PaginationViewModel;
import ru.team.up.core.service.DataService;

import java.text.ParseException;

@Controller
@RequestMapping("/admin")
public class MonitoringController {
    Logger logger = LoggerFactory.getLogger(MonitoringController.class);
    private final DataService dataService;
    // Количество записей на странице поиска по умолчанию
    private final String pageSize = "10";

    public MonitoringController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/")
    public String home(Model model) {
        Page<Report> reports = dataService.getAll(Integer.parseInt(pageSize));
        model.addAttribute("reports", reports);
        model.addAttribute("modules", AppModuleNameDto.values());
        model.addAttribute("initiator", InitiatorTypeDto.values());

        model.addAttribute("findViewModel", new FindViewModel());

        model.addAttribute("pagination", new PaginationViewModel(reports));
        return "reportPage";
    }

    @RequestMapping("/find")
    public String find(Model model,
                       FindViewModel findViewModel,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = pageSize) int size) throws ParseException {
        logger.debug("Запрос поиска Событий");

        Page<Report> reports = dataService.findByParam(
                findViewModel.getModuleName(),
                findViewModel.getInitiatorType(),
                findViewModel.getTimeAfter(),
                findViewModel.getTimeBefore(),
                findViewModel.getParamKey(),
                findViewModel.getParamValue(), page - 1, size);
        logger.debug("Результаты поиска : {}", reports);

        model.addAttribute("initiator", InitiatorTypeDto.values());
        model.addAttribute("modules", AppModuleNameDto.values());
        model.addAttribute("reports", reports);

        model.addAttribute("findViewModel", findViewModel);
        model.addAttribute("btn", "btn btn-default");
        PaginationViewModel ddd = new PaginationViewModel(reports);

        model.addAttribute("pagination", new PaginationViewModel(reports));
        return "reportPage";
    }


}
