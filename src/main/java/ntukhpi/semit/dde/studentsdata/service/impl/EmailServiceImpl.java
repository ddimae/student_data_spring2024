package ntukhpi.semit.dde.studentsdata.service.impl;

import ntukhpi.semit.dde.studentsdata.entity.Email;
import ntukhpi.semit.dde.studentsdata.entity.Person;
import ntukhpi.semit.dde.studentsdata.repository.EmailRepository;
import ntukhpi.semit.dde.studentsdata.service.interf.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;

    @Autowired
    public EmailServiceImpl(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public List<Email> getAllEmails() {
        return emailRepository.findAll();
    }

    @Override
    public boolean saveEmail(Email email) {
        Email savedEmail = emailRepository.save(email);
        if(savedEmail!=null) return true;
        else return false;
    }

    @Override
    public Email getEmailById(Long id) {
        return emailRepository.findById(id).orElse(null);
    }

    public List<Email> getAllByOwner(Person owner){
        return emailRepository.findAllByOwner(owner);
    }

    @Override
    public Person getOwnerById(Long id) {
        return emailRepository.findById(id).get().getOwner();
    }


    @Override
    public boolean updateEmail(Long id, Email entityToUpdate) {
        // Find the existing email entity
        Email existingEmail = emailRepository.findById(id).orElse(null);

        if (existingEmail != null) {
            // Update the properties based on the new data
            existingEmail.setActive(entityToUpdate.isActive());
            existingEmail.setPrior(entityToUpdate.isPrior());

            // Save the updated email entity
            emailRepository.save(existingEmail);

            return true; // Update successful
        }
        return false; // Email not found
    }

    public Email getPriorByOwner(Person owner) {
        return emailRepository.findPriorByOwner(owner);
    }
    @Override
    public boolean deleteEmailById(Long id){
        try {
            emailRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    // You can add more specific methods as needed for your application
}
