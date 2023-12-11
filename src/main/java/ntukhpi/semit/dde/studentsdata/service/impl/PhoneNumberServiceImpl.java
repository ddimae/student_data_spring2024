package ntukhpi.semit.dde.studentsdata.service.impl;

import jakarta.transaction.Transactional;
import ntukhpi.semit.dde.studentsdata.entity.Person;
import ntukhpi.semit.dde.studentsdata.entity.PhoneNumber;
import ntukhpi.semit.dde.studentsdata.repository.PhoneNumberRepository;
import ntukhpi.semit.dde.studentsdata.service.interf.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

    private final PhoneNumberRepository phoneNumberRepository;

    @Autowired
    public PhoneNumberServiceImpl(PhoneNumberRepository phoneNumberRepository) {
        this.phoneNumberRepository = phoneNumberRepository;
    }

    @Override
    public List<PhoneNumber> getAllPhoneNumbers() {
        return phoneNumberRepository.findAll();
    }

    @Override
    public List<PhoneNumber> getAllByOwner(Person owner) {
        return phoneNumberRepository.findAllByOwner(owner);
    }
    @Override
    public PhoneNumber savePhoneNumber(PhoneNumber phoneNumber) {
        return phoneNumberRepository.save(phoneNumber);
    }

    @Override
    public PhoneNumber getPhoneNumberById(Long id) {
        return phoneNumberRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deletePhoneNumberById(Long id) {
        try {
            phoneNumberRepository.deleteById(id);
            return true; // Deletion was successful
        } catch (EmptyResultDataAccessException e) {
            // Entity with the given ID was not found, deletion failed
            return false;
        }
    }
    @Transactional
    @Override
    public boolean updatePhone(Long id, PhoneNumber entityToUpdate) {
        // Find the existing phone entity
        PhoneNumber existingPhone = phoneNumberRepository.findById(id).orElse(null);

        if (existingPhone != null) {
            // Update the properties based on the new data
            existingPhone.setActive(entityToUpdate.isActive());
            existingPhone.setPrior(entityToUpdate.isPrior());

            // Save the updated phone entity
            phoneNumberRepository.save(existingPhone);

            return true; // Update successful
        }
        return false; // Phone not found
    }
    @Override
    public PhoneNumber findPrior(Person owner) {
        return phoneNumberRepository.findPriorByOwner(owner);
    }
}
