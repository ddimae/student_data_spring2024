package ntukhpi.semit.dde.studentsdata.controller;

import jakarta.servlet.http.HttpServletRequest;
import ntukhpi.semit.dde.studentsdata.entity.AcademicGroup;
import ntukhpi.semit.dde.studentsdata.entity.Student;
import ntukhpi.semit.dde.studentsdata.service.interf.AcademicGroupService;
import ntukhpi.semit.dde.studentsdata.service.interf.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;

@Controller
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

    @GetMapping("/students")
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
    @GetMapping("/edit_student")
    public String editStudent(@RequestParam Long id, Model model, HttpServletRequest request) {
        Student studentToEdit = studentService.getStudentById(id);

        if (studentToEdit != null) {
            model.addAttribute("error", null);
            model.addAttribute("student", studentToEdit);
            model.addAttribute("id", id);
            return "student";
        } else {
            String referer = request.getHeader("Referer");
            return "redirect:"+ referer;
        }
    }

    @PostMapping("/edit_student")
    public String updateStudent(@ModelAttribute("student") Student student, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        // Perform necessary validations or logic before updating
        String idStr = request.getParameter("id");
        String idGroupStr = request.getParameter("id_group");
        String birthdayStr = request.getParameter("dateOfBirth");
        boolean contract  = Boolean.parseBoolean(request.getParameter("contract"));
        boolean scholarship = Boolean.parseBoolean(request.getParameter("takeScholarship"));
        System.out.println(contract);
        System.out.println(scholarship);
        System.out.println(birthdayStr);

        //Transform data
        Long id = Long.parseLong(idStr);
        Long idGroup = Long.parseLong(idGroupStr);
        LocalDate birthday = LocalDate.parse(birthdayStr);

        // Create object
        Student studForUpdate = new Student(student.getLastName(),student.getLastName(),student.getMiddleName(),
                birthday,contract,scholarship);
        studForUpdate.setId(Long.parseLong(idStr));
        System.out.println("This is me trying to make sense of this bullshit");
        System.out.println("student passed here: "  + student);
        System.out.println("Student that was made: "+ studForUpdate);
        System.out.println("Id 1: " + idStr + " " + student.getLastName());
        model.addAttribute("id_group", request.getParameter("id_group"));
        System.out.println("Id group: " + request.getParameter("id_group"));
        boolean updateOK = studentService.updateStudent(studForUpdate) !=null;

        if (!updateOK) {
            model.addAttribute("error", "Trouble updating information about student!");
            model.addAttribute("student", student);
            model.addAttribute("id", 0);
            return "students";
        } else {
            System.out.println("Update successfulL! New student - " + studForUpdate);
            String referer = request.getHeader("Referer");
            return "redirect:/students?id_group=" + request.getParameter("id_group");
        }
    }

}