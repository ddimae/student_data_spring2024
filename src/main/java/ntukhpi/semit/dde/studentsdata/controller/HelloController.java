package ntukhpi.semit.dde.studentsdata.controller;

import ntukhpi.semit.dde.studentsdata.entity.AcademicGroup;
import ntukhpi.semit.dde.studentsdata.service.interf.AcademicGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HelloController {
    private final AcademicGroupService academicGroupService;

    @Autowired
    public HelloController(AcademicGroupService academicGroupService) {
        this.academicGroupService = academicGroupService;
    }

    @GetMapping("/")
    public String hello(Model model) {
        model.addAttribute("name", "John");
        return "start";
    }
    @PostMapping("/groups")
    public String postAllGroups(Model model) {
        List<AcademicGroup> groups = academicGroupService.getAllAcademicGroups()
                .stream()
                .sorted(Comparator.comparing(AcademicGroup::getGroupName))
                .collect(Collectors.toList());
        System.out.println(groups);
        model.addAttribute("groups", groups);
        return "groups"; // Assuming the JSP file is in the "groups" folder inside "views"
    }
    @GetMapping("/groups")
    public String redirect(Model model) {
        return postAllGroups(model);
    }

}