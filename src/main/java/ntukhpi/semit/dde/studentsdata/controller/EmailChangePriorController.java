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
@RequestMapping("/email_prior")
public class EmailChangePriorController {

    private final PersonService personService;
    private final EmailService emailService;

    @Autowired
    public EmailChangePriorController(PersonService personService, EmailService emailService) {
        this.personService = personService;
        this.emailService = emailService;
    }

    @PostMapping
    public String changeEmailPriorStatus(@RequestParam("id_email") Long idEmail,
                                         @RequestParam("id_owner") Long idOwner,
                                         Model model) {
        // Find objects
        Person owner = personService.getPersonById(idOwner);
        Email email = emailService.getEmailById(idEmail);
        Email emailPrior = emailService.getPriorByOwner(owner);

        // Check if making "Non-active" email primary is not allowed
        if (!email.isPrior() && !email.isActive()) {
            return "redirect:/emails?id_owner=" + idOwner + "&msgcode=5";
        }

        // Prepare for status change
        email.setPrior(!email.isPrior());

        // Call Update for the current email
        boolean updateRes = emailService.updateEmail(email.getId(),email);

        if (emailPrior != null && !emailPrior.equals(email)) {
            // Change the status of the current primary email to secondary
            emailPrior.setPrior(!email.isPrior());
            updateRes = emailService.updateEmail(emailPrior.getId(),emailPrior);
            if (!updateRes) {
                return "redirect:/emails?id_owner=" + idOwner + "&msgcode=4";
            }
        }

        // Set the current email as primary
        updateRes = emailService.updateEmail(email.getId(),email);

        if (updateRes) {
            // Back to emails_persons
            if (email.equals(emailPrior) && !email.isPrior()) {
                return "redirect:/emails?id_owner=" + idOwner + "&msgcode=6";
            } else {
                return "redirect:/emails?id_owner=" + idOwner + "&msgcode=7";
            }
        } else {
            // Update failed, redirect with an error message
            return "redirect:/emails?id_owner=" + idOwner + "&msgcode=8";
        }
    }
}
