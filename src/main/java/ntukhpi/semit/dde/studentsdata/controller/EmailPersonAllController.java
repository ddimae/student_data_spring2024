package ntukhpi.semit.dde.studentsdata.controller;

import ntukhpi.semit.dde.studentsdata.entity.Email;
import ntukhpi.semit.dde.studentsdata.entity.Person;
import ntukhpi.semit.dde.studentsdata.service.interf.EmailService;
import ntukhpi.semit.dde.studentsdata.service.interf.PersonService;
import ntukhpi.semit.dde.studentsdata.utils.ContactMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/emails")
public class EmailPersonAllController {

    private final PersonService personService;
    private final EmailService emailService;

    @Autowired
    public EmailPersonAllController(PersonService personService, EmailService emailService) {
        this.personService = personService;
        this.emailService = emailService;
    }

    @GetMapping
    public String showPersonEmails(@RequestParam("id_owner") Long ownerId,
                                   @RequestParam(value = "msgcode", required = false) Integer messageCode, Model model) {
        Person personInDB = personService.getPersonById(ownerId);
        List<Email> emails = emailService.getAllByOwner(personInDB);

        model.addAttribute("owner", personInDB);
        model.addAttribute("emails", emails);
        model.addAttribute("ref_to_return", personService.linkToReturn(personInDB));

        if (messageCode != null) {
            String message = ContactMessages.values()[messageCode].getText();
            model.addAttribute("message", message);
        } else {
            model.addAttribute("message", ContactMessages.EMPTY.getText());
        }

        return "emails_person";
    }
}