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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Controller
@RequestMapping("/email_active")
public class EmailChangeActiveController {

    private final PersonService personService;
    private final EmailService emailService;

    @Autowired
    public EmailChangeActiveController(PersonService personService, EmailService emailService) {
        this.personService = personService;
        this.emailService = emailService;
    }

    @PostMapping
    public String changeEmailActiveStatus(@RequestParam("id_email") Long idEmail,
                                          @RequestParam("id_owner") Long idOwner,
                                          Model model) {
        // Find objects
        Person owner = personService.getPersonById(idOwner);
        Email email = emailService.getEmailById(idEmail);

        // Check if making "Primary" email inactive is not allowed
        if (email.isActive() && email.isPrior()) {
            return "redirect:/emails?id_owner=" + idOwner + "&msgcode=9";
        }

        // Prepare for status change
        email.setActive(!email.isActive());

        // Call the service to update the email
        boolean updateRes = emailService.updateEmail(email.getId(), email);

        if (updateRes) {
            // Status changed successfully, redirect to emails_person
            return "redirect:/emails?id_owner=" + idOwner + "&msgcode=10";
        } else {
            // Update failed, redirect with an error message
            return "redirect:/emails?id_owner=" + idOwner + "&msgcode=11";
        }
    }
}
