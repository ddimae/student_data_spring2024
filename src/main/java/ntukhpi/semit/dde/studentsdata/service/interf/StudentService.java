package ntukhpi.semit.dde.studentsdata.service.interf;

import ntukhpi.semit.dde.studentsdata.entity.Student;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    List<Student> getAllStudentsByGroupId(Long id);
    boolean saveStudent(Student student);

    Student getStudentById(Long id);

    Student getStudentByName(String firstName, String secondName);

    Student updateStudent(Student student);

    void deleteStudentById(Long id);


    // You can add more specific methods as needed for your application
}
