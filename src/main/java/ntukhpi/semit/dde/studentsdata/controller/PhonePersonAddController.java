package ntukhpi.semit.dde.studentsdata.controller;

import ntukhpi.semit.dde.studentsdata.entity.Person;
import ntukhpi.semit.dde.studentsdata.entity.PhoneNumber;
import ntukhpi.semit.dde.studentsdata.service.interf.PersonService;
import ntukhpi.semit.dde.studentsdata.service.interf.PhoneNumberService;
import ntukhpi.semit.dde.studentsdata.utils.ContactMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/add_phone")
public class PhonePersonAddController {
    private final PhoneNumberService phoneService;
    private final PersonService personService;

    @Autowired
    public PhonePersonAddController(PhoneNumberService phoneService, PersonService personService) {
        this.phoneService = phoneService;
        this.personService = personService;
    }

    @GetMapping
    public String showAddPhoneForm(@RequestParam("id_owner") Long ownerId, Model model) {
        Person owner = personService.getPersonById(ownerId);
        PhoneNumber phoneToIns = new PhoneNumber(true, false, owner, "");
        phoneToIns.setId(0L);
        model.addAttribute("error", "");
        model.addAttribute("phone", phoneToIns);
        model.addAttribute("owner", phoneToIns.getOwner());
        return "phone"; // Assuming your JSP file is in the "phones" folder.
    }

    @PostMapping
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
}
