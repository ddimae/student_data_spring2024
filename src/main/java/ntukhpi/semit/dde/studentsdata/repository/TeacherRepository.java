package ntukhpi.semit.dde.studentsdata.repository;

import ntukhpi.semit.dde.studentsdata.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
