package ntukhpi.semit.dde.studentsdata.service.impl;

import ntukhpi.semit.dde.studentsdata.entity.Address;
import ntukhpi.semit.dde.studentsdata.repository.AddressRepository;
import ntukhpi.semit.dde.studentsdata.service.interf.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address getAddressById(Long id) {
        return addressRepository.findById(id).orElse(null);
    }

    @Override
    public boolean updateAddress(Address address) {
        Address existingAddress = addressRepository.findById(address.getId()).get();

        if (existingAddress != null) {
            existingAddress.setCountry(address.getCountry());
            existingAddress.setRegion(address.getRegion());
            existingAddress.setCity(address.getCity());
            existingAddress.setAddress(address.getAddress());
            addressRepository.save(existingAddress);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteAddressById(Long id) {
        try {
            addressRepository.deleteById(id);
            return true;
        }
        catch(EmptyResultDataAccessException e){
            return false;
        }
    }

    // You can add more specific methods as needed for your application
}
