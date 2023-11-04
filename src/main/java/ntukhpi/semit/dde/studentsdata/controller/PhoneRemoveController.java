package ntukhpi.semit.dde.studentsdata.controller;

import ntukhpi.semit.dde.studentsdata.entity.PhoneNumber;
import ntukhpi.semit.dde.studentsdata.service.interf.PhoneNumberService;
import ntukhpi.semit.dde.studentsdata.utils.ContactMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/delete_phone")
public class PhoneRemoveController {

    private final PhoneNumberService phoneNumberService;

    @Autowired
    public PhoneRemoveController(PhoneNumberService phoneNumberService) {
        this.phoneNumberService = phoneNumberService;
    }

    @GetMapping
    public String showDeletePhoneForm(@RequestParam("id_owner") Long ownerId,
                                      @RequestParam("id_phone") Long phoneId,
                                      Model model) {
        PhoneNumber phoneToDel = phoneNumberService.getPhoneNumberById(phoneId);
        model.addAttribute("error", null);
        model.addAttribute("phone", phoneToDel);
        model.addAttribute("owner", ownerId);
        return "phone_delete"; // Assuming your Thymeleaf template is in the "phones" folder.
    }

    @PostMapping
    public String processDeletePhoneForm(@RequestParam("id_phone") Long phoneId,
                                         @RequestParam("id_owner") Long ownerId,
                                         Model model) {
        PhoneNumber phoneToDelete = phoneNumberService.getPhoneNumberById(phoneId);

        // Call the service method to delete the phone
        boolean deleteRes = phoneNumberService.deletePhoneNumberById(phoneId);

        if (deleteRes) {
            // Redirect to the list of person's phones
            return "redirect:/phones?id_owner=" + ownerId + "&msgcode=12";
        } else {
            // Error handling if deletion fails
            model.addAttribute("error", ContactMessages.MESSAGE13.getText());
            model.addAttribute("phone", phoneToDelete);
            model.addAttribute("owner", ownerId); //!!!!
            return "phone_delete";
        }
    }
}
