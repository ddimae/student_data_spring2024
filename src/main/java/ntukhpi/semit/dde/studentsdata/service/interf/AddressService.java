package ntukhpi.semit.dde.studentsdata.service.interf;

import ntukhpi.semit.dde.studentsdata.entity.Address;
import ntukhpi.semit.dde.studentsdata.entity.Person;

import java.util.List;

public interface AddressService {
    List<Address> getAllAddresses();

    Address saveAddress(Address address);

    Address getAddressById(Long id);

    boolean updateAddress(Address address);

    boolean deleteAddressById(Long id);

    // You can add more specific methods as needed for your application
}
