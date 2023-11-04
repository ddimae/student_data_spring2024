package ntukhpi.semit.dde.studentsdata.service.interf;

import ntukhpi.semit.dde.studentsdata.entity.Person;
import ntukhpi.semit.dde.studentsdata.entity.PhoneNumber;

import java.util.List;

public interface PhoneNumberService {
    List<PhoneNumber> getAllPhoneNumbers();

    PhoneNumber savePhoneNumber(PhoneNumber phoneNumber);

    PhoneNumber getPhoneNumberById(Long id);

    boolean updatePhone(Long id, PhoneNumber entityToUpdate);
    boolean deletePhoneNumberById(Long id);
    PhoneNumber findPrior(Person owner);
    // You can add more specific methods as needed for your application
}
