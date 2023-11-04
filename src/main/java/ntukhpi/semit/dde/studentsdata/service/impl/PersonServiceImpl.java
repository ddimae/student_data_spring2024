package ntukhpi.semit.dde.studentsdata.service.impl;

import ntukhpi.semit.dde.studentsdata.entity.*;
import ntukhpi.semit.dde.studentsdata.repository.PersonRepository;
import ntukhpi.semit.dde.studentsdata.repository.PhoneNumberRepository;
import ntukhpi.semit.dde.studentsdata.service.interf.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PhoneNumberRepository phoneNumberRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, PhoneNumberRepository phoneNumberRepository) {
        this.personRepository = personRepository;
        this.phoneNumberRepository = phoneNumberRepository;
    }

    @Override
    public Person getPersonById(Long id) {
        Optional<Person> personOptional = personRepository.findById(id);
        return personOptional.orElse(null);
    }

    @Override
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }
    @Override
    public List<PhoneNumber> findAllPhonesNumberByOwner(Person owner) {
        // Assuming you have a bidirectional relationship between Person and PhoneNumber entities
        return phoneNumberRepository.findAllByOwner(owner);
    }
    @Override
    public String linkToReturn(Person person) {
        if (person instanceof Student) {
            return "students?id_group=" + ((Student) person).getAcademicGroup().getId();
        }
        if (person instanceof Parent) {
            return "parents?id_owner=" + person.getId();
        }
        if (person instanceof Teacher) {
            return "groups";
        }
        return "";
    }
    @Override
    public boolean updatePerson(Person entityToUpdate) {
        // Find the existing person entity
        Person existingPerson = personRepository.findById(entityToUpdate.getId()).orElse(null);

        if (existingPerson != null) {
            // Update the properties based on the new data
            existingPerson.setFirstName(entityToUpdate.getFirstName());
            existingPerson.setLastName(entityToUpdate.getLastName());
            existingPerson.setAddresses(entityToUpdate.getAddresses());
            existingPerson.setDateOfBirth(entityToUpdate.getDateOfBirth());
            existingPerson.setContacts(entityToUpdate.getContacts());

            // Save the updated person entity
            personRepository.save(existingPerson);

            return true; // Update successful
        }
        return false; // Person not found
    }
}
