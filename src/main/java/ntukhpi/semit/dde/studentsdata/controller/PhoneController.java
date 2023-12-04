package ntukhpi.semit.dde.studentsdata.controller;

import ntukhpi.semit.dde.studentsdata.entity.Person;
import ntukhpi.semit.dde.studentsdata.entity.PhoneNumber;
import ntukhpi.semit.dde.studentsdata.service.interf.PersonService;
import ntukhpi.semit.dde.studentsdata.service.interf.PhoneNumberService;
import ntukhpi.semit.dde.studentsdata.utils.ContactMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PhoneController {
    private final PhoneNumberService phoneService;
    private final PersonService personService;

    @Autowired
    public PhoneController(PhoneNumberService phoneService, PersonService personService) {
        this.phoneService = phoneService;
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

    @GetMapping("/add_phone")
    public String showAddPhoneForm(@RequestParam("id_owner") Long ownerId, Model model) {
        Person owner = personService.getPersonById(ownerId);
        PhoneNumber phoneToIns = new PhoneNumber(true, false, owner, "");
        phoneToIns.setId(0L);
        model.addAttribute("error", "");
        model.addAttribute("phone", phoneToIns);
        model.addAttribute("owner", phoneToIns.getOwner());
        return "phone"; // Assuming your JSP file is in the "phones" folder.
    }

    @PostMapping("/add_phone")
    public String processAddPhoneForm(@RequestParam("id_phone") Long idPhone,
                                      @RequestParam("phone_number") String phoneNumber,
                                      @RequestParam("active") String activeStr,
                                      @RequestParam("prior") String priorStr,
                                      @RequestParam("id_owner") Long ownerId,
                                      Model model) {

        System.out.println(idPhone);
        System.out.println(phoneNumber);
        System.out.println(activeStr);
        System.out.println(priorStr);

        boolean active = "Активний".equals(activeStr);
        boolean prior = "Основний".equals(priorStr);
        Person owner = personService.getPersonById(ownerId);
        PhoneNumber phone = new PhoneNumber(active, prior, owner, phoneNumber);
        if (phone.isPrior() && !phone.isActive()) {
            model.addAttribute("error", ContactMessages.MESSAGE03.getText());
            model.addAttribute("phone", phone);
            model.addAttribute("owner", personService.getPersonById(ownerId));
            return "phone";
        }

        PhoneNumber phonePrior = phoneService.findPrior(personService.getPersonById(ownerId));
        if (phonePrior != null && phone.isPrior()) {
            phonePrior.setPrior(!phone.isPrior());
            phoneService.updatePhone(phonePrior.getId(), phonePrior);
        }

        phoneService.savePhoneNumber(phone);

        return "redirect:/phones?id_owner=" + ownerId;
    }
    @GetMapping("/delete_phone")
    public String showDeletePhoneForm(@RequestParam("id_owner") Long ownerId,
                                      @RequestParam("id_phone") Long phoneId,
                                      Model model) {
        PhoneNumber phoneToDel = phoneService.getPhoneNumberById(phoneId);
        model.addAttribute("error", null);
        model.addAttribute("phone", phoneToDel);
        model.addAttribute("owner", ownerId);
        return "phone_delete"; // Assuming your Thymeleaf template is in the "phones" folder.
    }

    @PostMapping("/delete_phone")
    public String processDeletePhoneForm(@RequestParam("id_phone") Long phoneId,
                                         @RequestParam("id_owner") Long ownerId,
                                         Model model) {
        PhoneNumber phoneToDelete = phoneService.getPhoneNumberById(phoneId);

        // Call the service method to delete the phone
        boolean deleteRes = phoneService.deletePhoneNumberById(phoneId);

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
    @GetMapping("/phone_active")
    public String changePhoneActiveStatus(
            @RequestParam Long id_phone,
            @RequestParam Long id_owner,
            Model model
    ) {
        Person owner = personService.getPersonById(id_owner);
        PhoneNumber phone = phoneService.getPhoneNumberById(id_phone);

        // If attempting to make the "Primary" phone inactive, it's not allowed
        if (phone.isActive() && phone.isPrior()) {
            System.out.println("Зробити  \"Основний\" телефон \"Неактивним\" неможливо !!!");
            model.addAttribute("msgcode", 9);
            return "redirect:/phones?id_owner=" + id_owner;
        }

        // Toggle the active status
        phone.setActive(!phone.isActive());

        // Update the phone
        if (phoneService.updatePhone(phone.getId(), phone)) {
            System.out.println("Статус телефону змінений!");
            model.addAttribute("msgcode", 10);
        } else {
            System.out.println("Помилка оновлення статусу актуальності! Update SQL mistake!!!");
            model.addAttribute("msgcode", 11);
        }

        return "redirect:/phones?id_owner=" + id_owner;
    }
    @GetMapping("/phone_prior")
    public String changePhonePriorGet(@RequestParam("id_phone") Long idPhone,
                                      @RequestParam("id_owner") Long idOwner, RedirectAttributes redirectAttributes) {
        Person owner = personService.getPersonById(idOwner);
        PhoneNumber phone = phoneService.getPhoneNumberById(idPhone);
        PhoneNumber phonePrior = phoneService.findPrior(owner);

        if (!phone.isPrior() && !phone.isActive()) {
            redirectAttributes.addAttribute("id_owner", idOwner);
            redirectAttributes.addAttribute("msgcode", 5);
            return "redirect:/phones";
        }

        phone.setPrior(!phone.isPrior());
        boolean updateRes = false;

        if (phonePrior != null && !phonePrior.equals(phone)) {
            phonePrior.setPrior(!phone.isPrior());
            updateRes = phoneService.updatePhone(phonePrior.getId(), phonePrior);

            if (!updateRes) {
                redirectAttributes.addAttribute("id_owner", idOwner);
                redirectAttributes.addAttribute("msgcode", 4);
                return "redirect:/phones";
            }
        }

        updateRes = phoneService.updatePhone(phone.getId(), phone);

        if (updateRes) {
            if (phone.equals(phonePrior) && !phone.isPrior()) {
                redirectAttributes.addAttribute("id_owner", idOwner);
                redirectAttributes.addAttribute("msgcode", 6);
                return "redirect:/phones";
            } else {
                redirectAttributes.addAttribute("id_owner", idOwner);
                redirectAttributes.addAttribute("msgcode", 7);
                return "redirect:/phones";
            }
        } else {
            redirectAttributes.addAttribute("id_owner", idOwner);
            redirectAttributes.addAttribute("msgcode", 8);
            return "redirect:/phones";
        }
    }

    @PostMapping("/phone_prior")
    public void changePhonePriorPost(@RequestParam("id_phone") Long idPhone,
                                     @RequestParam("id_owner") Long idOwner,
                                     RedirectAttributes redirectAttributes) {
        changePhonePriorGet(idPhone,idOwner, redirectAttributes);
    }
}
