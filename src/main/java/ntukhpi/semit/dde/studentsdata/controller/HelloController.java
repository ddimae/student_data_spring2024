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
        List<AcademicGroup> groups = academicGroupService.getAllAcademicGroups();
        System.out.println(groups);
        groups
                .stream()
                .sorted(Comparator
                        .comparing(AcademicGroup::getGroupName)
                        .thenComparing(group -> Integer.parseInt(group.getGroupName().replaceAll("[^0-9]", "")))
                        .thenComparing(group -> group.getGroupName().contains("і"))
        .thenComparing(group -> !group.getStudentsList().isEmpty()))
                .collect(Collectors.toList());

        //sorted(Comparator
        //                        // Sort first by whether the group has students
        //                        .comparing(group -> !group.getStudentsList().isEmpty())
        //                        // Then, sort by numeric value
        //                        .thenComparing(group -> Integer.parseInt(group.getGroupName().replaceAll("[^0-9]", "")))
        //                        // Finally, sort by the presence of 'і'
        //                        .thenComparing(group -> group.getGroupName().contains("і"))
        //                )
        System.out.println(groups);
        model.addAttribute("groups", groups);
        return "groups"; // Assuming the JSP file is in the "groups" folder inside "views"
    }
    @GetMapping("/groups")
    public String redirect(Model model) {
        return postAllGroups(model);
    }

}