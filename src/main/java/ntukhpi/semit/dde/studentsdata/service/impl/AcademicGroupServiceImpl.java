package ntukhpi.semit.dde.studentsdata.service.impl;

import ntukhpi.semit.dde.studentsdata.entity.AcademicGroup;
import ntukhpi.semit.dde.studentsdata.entity.Student;
import ntukhpi.semit.dde.studentsdata.repository.AcademicGroupRepository;
import ntukhpi.semit.dde.studentsdata.service.interf.AcademicGroupService;
import ntukhpi.semit.dde.studentsdata.service.interf.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcademicGroupServiceImpl implements AcademicGroupService {

    private final AcademicGroupRepository academicGroupRepository;
    private final StudentService studentService;

    @Autowired
    public AcademicGroupServiceImpl(AcademicGroupRepository academicGroupRepository, StudentService studentService) {
        this.academicGroupRepository = academicGroupRepository;
        this.studentService = studentService;
    }
    @Override
    public List<AcademicGroup> getAllAcademicGroups() {
        return academicGroupRepository.findAll();
    }

    @Override
    public boolean saveAcademicGroup(AcademicGroup academicGroup) {
        try {
            if(getAcademicGroupByName(academicGroup.getGroupName())==null){
                academicGroupRepository.save(academicGroup);
                for (Student stud : academicGroup.getStudentsList()) {
                    studentService.saveStudent(stud);
                }
            }
            else {
                academicGroup = getAcademicGroupByName(academicGroup.getGroupName());
                for (Student stud : academicGroup.getStudentsList()) {
                    stud.setAcademicGroup(academicGroup);
                    studentService.saveStudent(stud);
                }
            }

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public AcademicGroup getAcademicGroupById(Long id) {
        return academicGroupRepository.findById(id).orElse(null);
    }

    @Override
    public boolean updateAcademicGroup(AcademicGroup academicGroup) {
        try {
            for (Student stud : academicGroup.getStudentsList()) {
                //if(student exists
                studentService.saveStudent(stud);
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public void deleteAcademicGroupById(Long id) {
        academicGroupRepository.deleteById(id);
    }

    @Override
    public AcademicGroup getAcademicGroupByName(String groupName) {
        return academicGroupRepository.findByGroupName(groupName);
    }

    // You can add more specific methods as needed for your application
}
