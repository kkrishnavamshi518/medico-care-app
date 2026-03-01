package com.doctor.appointment.controller;
import com.doctor.appointment.entities.Contact;
import com.doctor.appointment.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class HomeController {
    @Autowired
    private ContactRepository contactRepository;
    @GetMapping("/")
    public String home() {
        return "index";
    }
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
    @PostMapping("/contact")
    public String saveContact(@ModelAttribute Contact contact, RedirectAttributes redirectAttributes) {
        contactRepository.save(contact);
        redirectAttributes.addFlashAttribute("successMessage",
                "Thank you for contacting Medico! We will respond soon.");
        return "redirect:/contact";
    }
}