package ntukhpi.semit.dde.studentsdata.controller;

import ntukhpi.semit.dde.studentsdata.entity.Address;
import ntukhpi.semit.dde.studentsdata.entity.Person;
import ntukhpi.semit.dde.studentsdata.entity.PhoneNumber;
import ntukhpi.semit.dde.studentsdata.service.interf.AddressService;
import ntukhpi.semit.dde.studentsdata.service.interf.PersonService;
import ntukhpi.semit.dde.studentsdata.service.interf.PhoneNumberService;
import ntukhpi.semit.dde.studentsdata.utils.ContactMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@Controller
@RequestMapping("/add_address")
public class AddAddressController {

    @Autowired
    private AddressService addressService;
    @Autowired
    private PersonService personService;

    @GetMapping
    public String showAddAddressForm(@RequestParam("id_owner") Long ownerId, Model model) {
        Person owner = personService.getPersonById(ownerId);
        Address address = new Address("", "", "", "");
        address.setId(0L);
        model.addAttribute("addr", address);
        model.addAttribute("owner", owner);
        model.addAttribute("error", "");
        return "/address";
    }

    @PostMapping
    public String addAddress(@ModelAttribute("addr") Address address,
                             @RequestParam("id_owner") Long ownerId,
                             Model model) {
        try {
            Person person = personService.getPersonById(ownerId);
            System.out.println("Adding owner to address");
            //address.addOwner(person,false);
            System.out.println("Adding address to owner");
            person.addAddress(address,false);
            System.out.println("Saving to db");
            addressService.saveAddress(address);
            System.out.println("Yay!");

            System.out.println(personService.getPersonById(ownerId).getAddresses());
            //personService.updatePerson(person);
            return "redirect:/addresses?id_owner=" + ownerId + "&msgcode=1";
        } catch (Exception e) {
            model.addAttribute("error", ContactMessages.MESSAGE02.getText());
            model.addAttribute("addr", address);
            model.addAttribute("owner", personService.getPersonById(ownerId));
            return "/address";
        }
    }

    // Add other methods for handling different actions
}