package ntukhpi.semit.dde.studentsdata.controller;

import ntukhpi.semit.dde.studentsdata.entity.Address;
import ntukhpi.semit.dde.studentsdata.entity.Person;
import ntukhpi.semit.dde.studentsdata.entity.PhoneNumber;
import ntukhpi.semit.dde.studentsdata.service.interf.AddressService;
import ntukhpi.semit.dde.studentsdata.service.interf.PersonService;
import ntukhpi.semit.dde.studentsdata.service.interf.PhoneNumberService;
import ntukhpi.semit.dde.studentsdata.utils.ContactMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
@Controller
@RequestMapping("/delete_address")
public class AddressDeleteController {

    @Autowired
    private AddressService addressService;
    @Autowired
    private PersonService personService;
    @GetMapping
    public String showDeleteAddressForm(@RequestParam Long id_owner, @RequestParam Long id_address, Model model) {
        Person owner = personService.getPersonById(id_owner);
        Address addressToDelete = addressService.getAddressById(id_address);

        model.addAttribute("owner", owner);
        model.addAttribute("addr", addressToDelete);
        model.addAttribute("error", null);

        return "address_delete";
    }

    @PostMapping
    public String deleteAddress(@RequestParam Long id_owner, @RequestParam Long id_address, RedirectAttributes redirectAttributes) {
        boolean deleteRes = addressService.deleteAddressById(id_address);

        if (deleteRes) {
            redirectAttributes.addAttribute("id_owner", id_owner);
            redirectAttributes.addFlashAttribute("msgcode", 12);
        } else {
            redirectAttributes.addFlashAttribute("error", ContactMessages.MESSAGE13.getText());
            // You may want to redirect to the delete form again on failure
            return "redirect:/addresses/delete?id_owner=" + id_owner + "&id_address=" + id_address;
        }

        return "redirect:/addresses";
    }
}
