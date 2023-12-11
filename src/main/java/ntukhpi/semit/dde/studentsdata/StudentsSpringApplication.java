package ntukhpi.semit.dde.studentsdata;

import ntukhpi.semit.dde.studentsdata.repository.StudentsRepository;
import ntukhpi.semit.dde.studentsdata.service.impl.StudentServiceImpl;
import ntukhpi.semit.dde.studentsdata.service.interf.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StudentsSpringApplication {
    //    @Autowired
    //    StudentsRepository studentsRepository;
    //    @Autowired
    //    StudentService studentService;
    public static void main(String[] args) {
        SpringApplication.run(StudentsSpringApplication.class, args);
    }
    @Bean
    public CommandLineRunner CommandLineRunnerBean() {
        return (args) -> {
            testStudents();
        };
    }
    private void testStudents(){
//        System.out.println(studentService.getAllStudents());
    }
}
