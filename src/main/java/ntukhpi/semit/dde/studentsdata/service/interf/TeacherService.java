package ntukhpi.semit.dde.studentsdata.service.interf;

import ntukhpi.semit.dde.studentsdata.entity.Teacher;

import java.util.List;

public interface TeacherService {
    List<Teacher> getAllTeachers();

    Teacher saveTeacher(Teacher teacher);

    Teacher getTeacherById(Long id);

    Teacher updateTeacher(Teacher teacher);

    void deleteTeacherById(Long id);

    // You can add more specific methods as needed for your application
}
