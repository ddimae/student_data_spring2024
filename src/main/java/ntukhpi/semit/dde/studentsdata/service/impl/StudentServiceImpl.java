package ntukhpi.semit.dde.studentsdata.service.impl;

import ntukhpi.semit.dde.studentsdata.entity.Contact;
import ntukhpi.semit.dde.studentsdata.entity.Email;
import ntukhpi.semit.dde.studentsdata.entity.PhoneNumber;
import ntukhpi.semit.dde.studentsdata.entity.Student;
import ntukhpi.semit.dde.studentsdata.repository.StudentsRepository;
import ntukhpi.semit.dde.studentsdata.service.interf.EmailService;
import ntukhpi.semit.dde.studentsdata.service.interf.PhoneNumberService;
import ntukhpi.semit.dde.studentsdata.service.interf.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentsRepository studentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PhoneNumberService phoneNumberService;

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> getAllStudentsByGroupId(Long id) {
        return studentRepository.findAllByAcademicGroup_Id(id);
    }

    @Override
    public boolean saveStudent(Student student) {
        try {
            if(getStudentByName(student.getLastName(),student.getFirstName())==null){
                studentRepository.save(student);
                System.out.println(student.getContacts());
                for (Contact contact : student.getContacts()) {
                    contact.setOwner(student);
                    if(contact.getClass() == Email.class){
                        emailService.saveEmail((Email) contact);
                    }
                    if(contact.getClass() == PhoneNumber.class){
                        phoneNumberService.savePhoneNumber((PhoneNumber) contact);
                    }
                }
            }
            else {
                System.out.println(student.getContacts());
                student = getStudentByName(student.getLastName(),student.getFirstName());
                for (Contact contact : student.getContacts()) {
                    contact.setOwner(student);
                    if(contact.getClass() == Email.class && !emailService.getAllByOwner(student).contains(contact)){
                        emailService.saveEmail((Email) contact);
                    }
                    if(contact.getClass() == PhoneNumber.class && !phoneNumberService.getAllByOwner(student).contains(contact)){
                        phoneNumberService.savePhoneNumber((PhoneNumber) contact);
                    }
                }
            }

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public Student getStudentByName(String firstName, String secondName) {
        return studentRepository.findByFirstNameAndLastName(firstName,secondName).orElse(null);
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
                existingStudent.setContacts(student.getContacts());

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
