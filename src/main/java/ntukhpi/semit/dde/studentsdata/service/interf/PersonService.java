package ntukhpi.semit.dde.studentsdata.service.interf;

import ntukhpi.semit.dde.studentsdata.entity.Person;
import ntukhpi.semit.dde.studentsdata.entity.PhoneNumber;

import java.util.List;

public interface PersonService {
    Person getPersonById(Long id);
    List<Person> getAllPersons();
    String linkToReturn(Person person);
    List<PhoneNumber> findAllPhonesNumberByOwner(Person owner);
    public boolean updatePerson(Person entityToUpdate);
    // Add other methods as needed
}
