package ntukhpi.semit.dde.studentsdata.controller;

import ntukhpi.semit.dde.studentsdata.entity.Person;
import ntukhpi.semit.dde.studentsdata.entity.PhoneNumber;
import ntukhpi.semit.dde.studentsdata.service.interf.PersonService;
import ntukhpi.semit.dde.studentsdata.utils.ContactMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PhonePersonAllController {

    private final PersonService personService;

    @Autowired
    public PhonePersonAllController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/phones")
    public String getAllPhonesForPerson(
            @RequestParam Long id_owner,
            Model model) {
        Person personInDB = personService.getPersonById(id_owner);
        model.addAttribute("owner", personInDB);

        List<PhoneNumber> phones = personService.findAllPhonesNumberByOwner(personInDB);
        model.addAttribute("phones", phones);

        model.addAttribute("ref_to_return", personService.linkToReturn(personInDB));

        // Handle messages
        String messageCode = "EMPTY";
        String message = ContactMessages.valueOf(messageCode).getText();
        model.addAttribute("message", message);

        return "phones_person";
    }
}
