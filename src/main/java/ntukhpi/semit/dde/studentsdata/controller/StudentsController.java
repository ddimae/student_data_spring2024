package ntukhpi.semit.dde.studentsdata.controller;

import ntukhpi.semit.dde.studentsdata.entity.AcademicGroup;
import ntukhpi.semit.dde.studentsdata.entity.Student;
import ntukhpi.semit.dde.studentsdata.service.interf.AcademicGroupService;
import ntukhpi.semit.dde.studentsdata.service.interf.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentsController {

    @Autowired
    private final StudentService studentService;  // Assuming you have a service to handle business logic
    @Autowired
    private final AcademicGroupService academicGroupService;  // Assuming you have a service to handle business logic

    @Autowired
    public StudentsController(AcademicGroupService academicGroupService, StudentService studentService) {
        super();
        this.academicGroupService = academicGroupService;
        this.studentService = studentService;
    }

    @GetMapping
    public String getAllStudents(@RequestParam("id_group") Long groupId, Model model) {
        System.out.println("StudentController#getAllStudents");
        System.out.println("Group id = " + groupId);
        model.addAttribute("group", groupId);
        AcademicGroup groupInDB = academicGroupService.getAcademicGroupById(groupId);
        model.addAttribute("groupInDb", groupInDB);
        List<Student> students = studentService.getAllStudentsByGroupId(groupId);
        System.out.println(students);
        model.addAttribute("students", students);
        return "students";  // Assuming your JSP file is in the "students" folder
    }

    // If you have a separate method for handling the POST request, you can add it here
    // @PostMapping
    // public String handlePostRequest(...) { ... }
}