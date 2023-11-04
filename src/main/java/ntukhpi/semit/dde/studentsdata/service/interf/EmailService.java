package ntukhpi.semit.dde.studentsdata.service.interf;

import ntukhpi.semit.dde.studentsdata.entity.Email;
import ntukhpi.semit.dde.studentsdata.entity.Person;

import java.util.List;

public interface EmailService {
    List<Email> getAllEmails();
    List<Email> getAllByOwner(Person Owner);

    boolean saveEmail(Email email);

    Email getEmailById(Long id);

    boolean updateEmail(Long id, Email entityToUpdate);

    Email getPriorByOwner(Person owner);
    boolean deleteEmailById(Long id);

    Person getOwnerById(Long id);
    // You can add more specific methods as needed for your application
}
