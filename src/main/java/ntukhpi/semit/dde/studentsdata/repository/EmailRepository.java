package ntukhpi.semit.dde.studentsdata.repository;

import ntukhpi.semit.dde.studentsdata.entity.Email;
import ntukhpi.semit.dde.studentsdata.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    List<Email> findAllByOwner(Person owner);

    @Query("SELECT e FROM Email e WHERE e.owner = :owner AND e.prior = true")
    Email findPriorByOwner(@Param("owner") Person owner);

}
