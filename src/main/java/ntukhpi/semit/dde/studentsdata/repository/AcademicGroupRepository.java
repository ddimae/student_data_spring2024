package ntukhpi.semit.dde.studentsdata.repository;

import ntukhpi.semit.dde.studentsdata.entity.AcademicGroup;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public interface AcademicGroupRepository extends JpaRepository<AcademicGroup, Long> {
    @Override
    List<AcademicGroup> findAll();

    @Override
    Optional<AcademicGroup> findById(Long aLong);

    AcademicGroup findByGroupName(String groupName);

}
