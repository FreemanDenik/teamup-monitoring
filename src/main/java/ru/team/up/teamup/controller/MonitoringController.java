package ru.team.up.teamup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.team.up.teamup.entity.Control;
import ru.team.up.teamup.entity.Report;
import ru.team.up.teamup.service.DataService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class MonitoringController {

    private final DataService dataService;

    public MonitoringController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("reports", dataService.getAll());
        return "reportPage";
    }

    @RequestMapping( "/find")
    public String find(Model model,
                       Control control,
                       String timeAfter,
                       String timeBefore) {

        List<Report> reports = null;

        if (control != null ||  !timeAfter.isEmpty() || !timeBefore.isEmpty()) {
            Date dateA = null;
            Date dateB = null;
            try {
                dateA = new SimpleDateFormat("yyyy-MM-dd").parse(timeAfter);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                dateB = new SimpleDateFormat("yyyy-MM-dd").parse(timeBefore);
            } catch (Exception e) {
                e.printStackTrace();
            }
            reports = dataService.findByParam(control, dateA, dateB);
        }

        model.addAttribute("reports", reports);
        return "reportPage";
    }

}
