package ntukhpi.semit.dde.studentsdata.service.impl;

import ntukhpi.semit.dde.studentsdata.entity.Teacher;
import ntukhpi.semit.dde.studentsdata.repository.TeacherRepository;
import ntukhpi.semit.dde.studentsdata.service.interf.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id).orElse(null);
    }

    @Override
    public Teacher updateTeacher(Teacher teacher) {
        // Implement update logic here
        return null;
    }

    @Override
    public void deleteTeacherById(Long id) {
        teacherRepository.deleteById(id);
    }

    // You can add more specific methods as needed for your application
}
