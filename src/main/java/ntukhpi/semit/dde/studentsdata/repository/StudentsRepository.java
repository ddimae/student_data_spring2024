package ntukhpi.semit.dde.studentsdata.repository;

import jakarta.validation.constraints.NotNull;
import ntukhpi.semit.dde.studentsdata.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentsRepository extends JpaRepository<Student, Long> {

    Optional<Student> findById(Long iD);

    List<Student> findAllByAcademicGroup_Id(Long id);

    Optional<Student> findByFirstNameAndLastName(@NotNull String firstName, @NotNull String lastName);

}
