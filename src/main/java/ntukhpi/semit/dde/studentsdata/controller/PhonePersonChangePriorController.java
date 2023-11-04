package ntukhpi.semit.dde.studentsdata.controller;

import ntukhpi.semit.dde.studentsdata.entity.Person;
import ntukhpi.semit.dde.studentsdata.entity.PhoneNumber;
import ntukhpi.semit.dde.studentsdata.service.interf.PersonService;
import ntukhpi.semit.dde.studentsdata.service.interf.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/phone_prior")
public class PhonePersonChangePriorController {

    private final PhoneNumberService phoneNumberService;
    private final PersonService personService;

    @Autowired
    public PhonePersonChangePriorController(PhoneNumberService phoneNumberService, PersonService personService) {
        this.phoneNumberService = phoneNumberService;
        this.personService = personService;
    }

    @GetMapping
    public String changePhonePriorGet(@RequestParam("id_phone") Long idPhone,
                                      @RequestParam("id_owner") Long idOwner, RedirectAttributes redirectAttributes) {
        Person owner = personService.getPersonById(idOwner);
        PhoneNumber phone = phoneNumberService.getPhoneNumberById(idPhone);
        PhoneNumber phonePrior = phoneNumberService.findPrior(owner);

        if (!phone.isPrior() && !phone.isActive()) {
            redirectAttributes.addAttribute("id_owner", idOwner);
            redirectAttributes.addAttribute("msgcode", 5);
            return "redirect:/phones";
        }

        phone.setPrior(!phone.isPrior());
        boolean updateRes = false;

        if (phonePrior != null && !phonePrior.equals(phone)) {
            phonePrior.setPrior(!phone.isPrior());
            updateRes = phoneNumberService.updatePhone(phonePrior.getId(), phonePrior);

            if (!updateRes) {
                redirectAttributes.addAttribute("id_owner", idOwner);
                redirectAttributes.addAttribute("msgcode", 4);
                return "redirect:/phones";
            }
        }

        updateRes = phoneNumberService.updatePhone(phone.getId(), phone);

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

    @PostMapping
    public void changePhonePriorPost(@RequestParam("id_phone") Long idPhone,
                                       @RequestParam("id_owner") Long idOwner,
                                     RedirectAttributes redirectAttributes) {
        changePhonePriorGet(idPhone,idOwner, redirectAttributes);
    }
}
