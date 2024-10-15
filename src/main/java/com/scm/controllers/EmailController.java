package com.scm.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.services.ContactService;
import com.scm.services.EmailService;

import jakarta.mail.MessagingException;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ContactService contactService;

    // Show the email composition form
    @GetMapping("/user/compose-email")
    public String composeEmail(@RequestParam("contactId") String contactId, Model model) {
        Contact contact = contactService.getById(contactId);
        model.addAttribute("contact", contact);
        return "user/compose-email";  // form for email composition
    }

    // Handle sending the email
    @PostMapping("/user/send-email")
    public String sendEmail(@RequestParam("contactId") String contactId,
                            @RequestParam("subject") String subject,
                            @RequestParam("body") String body, Model model) {
        Contact contact = contactService.getById(contactId);
        
        try {
            emailService.sendEmail(contact.getEmail(), subject, body);
            model.addAttribute("message", "Email sent successfully to " + contact.getEmail());
        } catch (MessagingException e) {
            model.addAttribute("error", "Failed to send email. Please try again.");
        }

        // Redirect back to contact list or email page
        return "redirect:/user/contacts";  
    }
}
