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

@Controller
@RequestMapping("/add_email")
public class AddEmailController {

    private final EmailService emailService;
    private final PersonService personService;

    @Autowired
    public AddEmailController(EmailService emailService, PersonService personService) {
        this.emailService = emailService;
        this.personService = personService;
    }

    @GetMapping
    public String showAddEmailForm(@RequestParam("id_owner") Long ownerId, Model model) {
        System.out.println("EmailController#showAddEmailForm");
        Person owner = personService.getPersonById(ownerId);
        System.out.println(owner.getClass() + ": " + owner);
        model.addAttribute("error", "");
        Email emailToInsert = new Email(true, false, owner, "");
        // Now id = null!!! We must set not null value!!!
        emailToInsert.setId(0L);
        System.out.println("emailToInsert:" + emailToInsert);
        model.addAttribute("email", emailToInsert);
        model.addAttribute("owner", emailToInsert.getOwner());
        return "email";
    }

    @PostMapping
    public String addEmail(@RequestParam("id_email") Long idEmail,
                           @RequestParam("id_owner") Long idOwner,
                           @RequestParam("email") String email,
                           @RequestParam("active") String activeStr,
                           @RequestParam("prior") String priorStr,
                           Model model) {
        System.out.println("EmailController#addEmail");

        Person owner = personService.getPersonById(idOwner);
        boolean active = "Активний".equals(activeStr);
        boolean prior = "Основний".equals(priorStr);
        Email emailToInsert = new Email(active, prior, owner, email);
        emailToInsert.setId(idEmail);

        if (emailToInsert.isPrior() && !emailToInsert.isActive()) {
            System.out.println("\"Неактивний\" email не може бути \"Основним\"!!!");
            model.addAttribute("error", ContactMessages.MESSAGE03.getText());
            model.addAttribute("email", emailToInsert);
            model.addAttribute("owner", owner);
            return "emails/email";
        }

        // Search for the current "Primary" contact
        // If the new contact is primary, the current one is reset to secondary
        Email emailPrior = emailService.getPriorByOwner(owner);
        if (emailPrior != null && emailToInsert.isPrior()) {
            // Change the current primary email to secondary first
            emailPrior.setPrior(!emailToInsert.isPrior());
            boolean updateRes = emailService.updateEmail(emailPrior.getId(), emailPrior);
            if (!updateRes) {
                System.out.println("Error resetting Primary email! Update SQL mistake!!!");
                return "redirect:/emails?id_owner=" + idOwner + "&msgcode=4";
            }
        }

        // Call Insert
        boolean insertRes = emailService.saveEmail(emailToInsert);
        if (insertRes) {
            System.out.println("New email has been added!");
            return "redirect:/emails?id_owner=" + idOwner + "&msgcode=1";
        } else {
            System.out.println("Error adding email! Insert SQL mistake!!!");
            model.addAttribute("error", ContactMessages.MESSAGE02.getText());
            model.addAttribute("email", emailToInsert);
            model.addAttribute("owner", owner);
            return "emails/email";
        }
    }
}