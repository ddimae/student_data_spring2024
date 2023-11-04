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
@RequestMapping("/addresses")
public class AddressController {
    @Autowired
    private PersonService personService;

    @GetMapping
    public String getAddressPage(@RequestParam("id_owner") Long ownerId,
                                 @RequestParam(value = "msgcode", required = false) Integer messageCode,Model model) {
        System.out.println("AddressController#getAddressPage");

        Person personInDB = personService.getPersonById(ownerId);
        System.out.println(personInDB);

        model.addAttribute("owner", personInDB);

        Map<Address, Boolean> addresses = personInDB.getAddresses();
        System.out.println(addresses);

        model.addAttribute("addresses", addresses);
        model.addAttribute("ref_to_return", personService.linkToReturn(personInDB));

        if (messageCode != null) {
            String message = ContactMessages.values()[messageCode].getText();
            System.out.println("message=" + message);
            model.addAttribute("message", message);
        } else {
            model.addAttribute("message", ContactMessages.EMPTY.getText());
        }

        return "/addresses_person";
    }
}
