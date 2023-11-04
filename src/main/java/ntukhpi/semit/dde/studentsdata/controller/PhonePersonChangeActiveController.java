package ntukhpi.semit.dde.studentsdata.controller;

import ntukhpi.semit.dde.studentsdata.entity.Person;
import ntukhpi.semit.dde.studentsdata.entity.PhoneNumber;
import ntukhpi.semit.dde.studentsdata.service.interf.PersonService;
import ntukhpi.semit.dde.studentsdata.service.interf.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PhonePersonChangeActiveController {

    private final PhoneNumberService phoneService;
    private final PersonService personService;

    @Autowired
    public PhonePersonChangeActiveController(PhoneNumberService phoneService, PersonService personService) {
        this.phoneService = phoneService;
        this.personService = personService;
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
}
