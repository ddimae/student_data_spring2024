package ntukhpi.semit.dde.studentsdata.service.impl;

import ntukhpi.semit.dde.studentsdata.entity.AcademicGroup;
import ntukhpi.semit.dde.studentsdata.repository.AcademicGroupRepository;
import ntukhpi.semit.dde.studentsdata.service.interf.AcademicGroupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcademicGroupServiceImpl implements AcademicGroupService {

    private final AcademicGroupRepository academicGroupRepository;

    public AcademicGroupServiceImpl(AcademicGroupRepository academicGroupRepository) {
        super();
        this.academicGroupRepository = academicGroupRepository;
    }

    @Override
    public List<AcademicGroup> getAllAcademicGroups() {
        return academicGroupRepository.findAll();
    }

    @Override
    public AcademicGroup saveAcademicGroup(AcademicGroup academicGroup) {
        return academicGroupRepository.save(academicGroup);
    }

    @Override
    public AcademicGroup getAcademicGroupById(Long id) {
        return academicGroupRepository.findById(id).orElse(null);
    }

    @Override
    public AcademicGroup updateAcademicGroup(AcademicGroup academicGroup) {
        // Implement update logic here
        return null;
    }

    @Override
    public void deleteAcademicGroupById(Long id) {
        academicGroupRepository.deleteById(id);
    }

    // You can add more specific methods as needed for your application
}
