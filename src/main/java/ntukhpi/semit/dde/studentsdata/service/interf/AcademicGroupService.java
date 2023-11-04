package ntukhpi.semit.dde.studentsdata.service.interf;

import ntukhpi.semit.dde.studentsdata.entity.AcademicGroup;

import java.util.List;

public interface AcademicGroupService {
    List<AcademicGroup> getAllAcademicGroups();

    AcademicGroup saveAcademicGroup(AcademicGroup academicGroup);

    AcademicGroup getAcademicGroupById(Long id);

    AcademicGroup updateAcademicGroup(AcademicGroup academicGroup);

    void deleteAcademicGroupById(Long id);

    // You can add more specific methods as needed for your application
}
