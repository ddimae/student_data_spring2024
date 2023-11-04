package ntukhpi.semit.dde.studentsdata.repository;

import ntukhpi.semit.dde.studentsdata.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
