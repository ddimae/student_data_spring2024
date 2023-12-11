package ntukhpi.semit.dde.studentsdata.service.interf;

import ntukhpi.semit.dde.studentsdata.entity.AcademicGroup;

import java.util.List;

public interface AcademicGroupService {
    List<AcademicGroup> getAllAcademicGroups();

    boolean saveAcademicGroup(AcademicGroup academicGroup);

    AcademicGroup getAcademicGroupById(Long id);

    boolean updateAcademicGroup(AcademicGroup academicGroup);

    void deleteAcademicGroupById(Long id);

     AcademicGroup getAcademicGroupByName(String groupName);

    // You can add more specific methods as needed for your application
}
