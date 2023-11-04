package ntukhpi.semit.dde.studentsdata.controller;

import ntukhpi.semit.dde.studentsdata.entity.Email;
import ntukhpi.semit.dde.studentsdata.entity.Person;
import ntukhpi.semit.dde.studentsdata.service.interf.EmailService;
import ntukhpi.semit.dde.studentsdata.utils.ContactMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/delete_email")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public String showDeleteEmailPage(@RequestParam("id_email") Long emailId, @RequestParam("id_owner") Long ownerId, Model model) {
        System.out.println("EmailController#showDeleteEmailPage");
        Person owner = emailService.getOwnerById(ownerId);
        Email emailToDelete = emailService.getEmailById(emailId);
        model.addAttribute("error", null);
        model.addAttribute("email", emailToDelete);
        model.addAttribute("owner", owner);
        return "emails/email_delete";
    }

    @PostMapping
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
