package ntukhpi.semit.dde.studentsdata.repository;

import ntukhpi.semit.dde.studentsdata.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> { }
