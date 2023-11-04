package ntukhpi.semit.dde.studentsdata.service.impl;

import jakarta.transaction.Transactional;
import ntukhpi.semit.dde.studentsdata.entity.Student;
import ntukhpi.semit.dde.studentsdata.repository.StudentsRepository;
import ntukhpi.semit.dde.studentsdata.service.interf.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentsRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentsRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> getAllStudentsByGroupId(Long id) {
        return studentRepository.findAllByAcademicGroup_Id(id);
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public Student updateStudent(Student student) {
        try {
            // Find the existing student entity by ID
            Student existingStudent = studentRepository.findById(student.getId()).orElse(null);

            if (existingStudent != null) {
                // Update the fields with the values from the provided student
                existingStudent.setDateOfBirth(student.getDateOfBirth());
                existingStudent.setContract(student.isContract());
                existingStudent.setTakeScholarship(student.isTakeScholarship());

                // Save the updated student entity
                Student updatedStudent = studentRepository.save(existingStudent);

                // Return the updated student entity
                return updatedStudent;
            } else {
                // Handle the case where the student with the given ID is not found
                System.err.println("=== StudentsServiceImpl#updateStudent === Student not found!");
                return null;
            }
        } catch (Exception e) {
            System.err.println("=== StudentsServiceImpl#updateStudent === Something went wrong!");
            e.printStackTrace();  // Log or handle the exception as needed
            return null;
        }
    }

    @Override
    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    // You can add more specific methods as needed for your application
}
