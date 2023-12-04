package ntukhpi.semit.dde.studentsdata.controller;

import ntukhpi.semit.dde.studentsdata.entity.Address;
import ntukhpi.semit.dde.studentsdata.entity.Person;
import ntukhpi.semit.dde.studentsdata.service.interf.AddressService;
import ntukhpi.semit.dde.studentsdata.service.interf.PersonService;
import ntukhpi.semit.dde.studentsdata.utils.ContactMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private PersonService personService;

    @GetMapping("/addresses")
    public String getAddressPage(@RequestParam("id_owner") Long ownerId,
                                 @RequestParam(value = "msgcode", required = false) Integer messageCode,Model model) {
        System.out.println("AddressController#getAddressPage");

        Person personInDB = personService.getPersonById(ownerId);
        System.out.println(personInDB);

        model.addAttribute("owner", personInDB);

        Map<Address, Boolean> addresses = personInDB.getAddresses();
        System.out.println(addresses);

        model.addAttribute("addresses", addresses);
        model.addAttribute("ref_to_return", personService.linkToReturn(personInDB));

        if (messageCode != null) {
            String message = ContactMessages.values()[messageCode].getText();
            System.out.println("message=" + message);
            model.addAttribute("message", message);
        } else {
            model.addAttribute("message", ContactMessages.EMPTY.getText());
        }

        return "/addresses_person";
    }
    @GetMapping("/add_address")
    public String AddressController(@RequestParam("id_owner") Long ownerId, Model model) {
        Person owner = personService.getPersonById(ownerId);
        Address address = new Address("", "", "", "");
        address.setId(0L);
        model.addAttribute("addr", address);
        model.addAttribute("owner", owner);
        model.addAttribute("error", "");
        return "/address";
    }
    @PostMapping("/add_address")
    public String addAddress(@ModelAttribute("addr") Address address,
                             @RequestParam("id_owner") Long ownerId,
                             Model model) {
        try {
            Person person = personService.getPersonById(ownerId);
            System.out.println("Adding owner to address");
            //address.addOwner(person,false);
            System.out.println("Adding address to owner");
            person.addAddress(address,false);
            System.out.println("Saving to db");
            addressService.saveAddress(address);
            System.out.println("Yay!");

            System.out.println(personService.getPersonById(ownerId).getAddresses());
            //personService.updatePerson(person);
            return "redirect:/addresses?id_owner=" + ownerId + "&msgcode=1";
        } catch (Exception e) {
            model.addAttribute("error", ContactMessages.MESSAGE02.getText());
            model.addAttribute("addr", address);
            model.addAttribute("owner", personService.getPersonById(ownerId));
            return "/address";
        }
    }
    @GetMapping("/delete_address")
    public String showDeleteAddressForm(@RequestParam Long id_owner, @RequestParam Long id_address, Model model) {
        Person owner = personService.getPersonById(id_owner);
        Address addressToDelete = addressService.getAddressById(id_address);

        model.addAttribute("owner", owner);
        model.addAttribute("addr", addressToDelete);
        model.addAttribute("error", null);

        return "address_delete";
    }

    @PostMapping("/delete_address")
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
    @GetMapping("/address_current")
    public String changeCurrentAddress(@RequestParam("id_address") Long idAddress,
                                       @RequestParam("id_owner") Long idOwner,
                                       Model model) {
        System.out.println("AddressChangeCurrentController#changeCurrentAddress");

        Person owner = personService.getPersonById(idOwner);
        Address address = addressService.getAddressById(idAddress);

        Address currentAddress = owner.getCurrentAddress();
        Boolean newCurrentValue = !owner.getCurrent(address);

        boolean updateRes = false;

        if (currentAddress != null && !currentAddress.equals(address)) {
            owner.setCurrent(currentAddress, !owner.getCurrent(currentAddress));
            updateRes = addressService.updateAddress(address);

            if (!updateRes) {
                System.out.println("Error resetting the current address! Update SQL mistake!!!");
                model.addAttribute("error", ContactMessages.MESSAGE04.getText());
                model.addAttribute("owner", owner);
                return "addresses_person";
            }
        }

        owner.setCurrent(address, newCurrentValue);
        updateRes = addressService.updateAddress(address);

        if (updateRes) {
            if (address.equals(currentAddress) && !newCurrentValue) {
                System.out.println("\"Main\" address removed!");
                model.addAttribute("messageCode", 6);
            } else {
                System.out.println("\"Main\" address changed!");
                model.addAttribute("messageCode", 7);
            }
        } else {
            System.out.println("Update error! Check for the presence of an address with the status \"Main\"! Update SQL mistake!!!");
            model.addAttribute("error", ContactMessages.MESSAGE08.getText());
        }

        model.addAttribute("owner", owner);
        return "redirect:/addresses?id_owner=" + idOwner;
    }

    @PostMapping("/address_current")
    public String doPost(@RequestParam("id_address") Long idAddress,
                         @RequestParam("id_owner") Long idOwner,
                         Model model) {
        return changeCurrentAddress(idAddress, idOwner, model);
    }
}
