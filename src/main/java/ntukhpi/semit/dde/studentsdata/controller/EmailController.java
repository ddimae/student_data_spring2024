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

public class EmailController {
    private final EmailService emailService;
    private final PersonService personService;

    @Autowired
    public EmailController(EmailService emailService, PersonService personService) {
        this.emailService = emailService;
        this.personService = personService;
    }
    @GetMapping("/emails")
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
    @GetMapping("/add_email")
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
    @PostMapping("/add_email")
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
    @PostMapping("/email_active")
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
    @PostMapping("/email_prior")
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
    @GetMapping("/delete_email")
    public String showDeleteEmailPage(@RequestParam("id_email") Long emailId, @RequestParam("id_owner") Long ownerId, Model model) {
        System.out.println("EmailController#showDeleteEmailPage");
        Person owner = emailService.getOwnerById(ownerId);
        Email emailToDelete = emailService.getEmailById(emailId);
        model.addAttribute("error", null);
        model.addAttribute("email", emailToDelete);
        model.addAttribute("owner", owner);
        return "emails/email_delete";
    }

    @PostMapping("/delete_email")
    public String deleteEmail(@RequestParam("id_email") Long emailId, @RequestParam("id_owner") Long ownerId, Model model) {
        System.out.println("EmailController#deleteEmail");
        // Call the service to delete the email
        boolean deleteResult = emailService.deleteEmailById(emailId);

        if (deleteResult) {
            // Redirect to the list of person's emails
            System.out.println("Email вилучений!!!");
            return "redirect:/emails?id_owner=" + ownerId + "&msgcode=12";
        } else {
            // Display error message and forward to the delete page again
            System.out.println("Помилка вилучення! Delete SQL mistake!!!");
            model.addAttribute("error", ContactMessages.MESSAGE13.getText());
            model.addAttribute("email", emailService.getEmailById(emailId));
            model.addAttribute("owner", ownerId);
            return "emails/email_delete";
        }
    }
}
