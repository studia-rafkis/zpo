package com.shop.backend.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.backend.dtos.ContactRequest;
import com.shop.backend.dtos.NewsletterRequest;
import com.shop.backend.entities.Contact;
import com.shop.backend.entities.Newsletter;
import com.shop.backend.entities.Order;
import com.shop.backend.entities.Promocode;
import com.shop.backend.entities.payu.CreateOrderRequest;
//import com.shop.backend.services.EmailService;
import com.shop.backend.repositories.ContactRepository;
import com.shop.backend.repositories.NewsletterRepository;
import com.shop.backend.repositories.PromocodeRepository;
import jakarta.mail.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import java.util.Properties;
@RestController
public class ContactController {
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private NewsletterRepository newsletterRepository;
    @Autowired
    private JavaMailSender mailSender;
    @PostMapping("/createContact")
    public ResponseEntity<Contact> createContact(@RequestBody ContactRequest contactRequest) {
        try {
            Contact contact = new Contact();
            contact.setFirstName(contactRequest.getFirstName());
            contact.setEmail(contactRequest.getEmail());
            contact.setSubject(contactRequest.getSubject());
            contact.setContents(contactRequest.getContents());
            contact.setReturnEmail(contactRequest.isReturnEmail());
            Contact createdContact = contactRepository.save(contact);

            if (contactRequest.isReturnEmail()) {
                sendEmailContact(contactRequest.getEmail(),contactRequest.getSubject(), contactRequest.getContents());
            }

            return new ResponseEntity<>(createdContact, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private void sendEmailContact(String to, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message + "\n\n---\nTa wiadomość została wygenerowana automatycznie. Prosimy o nie odpowiadanie.");
        mailSender.send(mailMessage);
    }
    @PostMapping("/createNewsletter")
    public ResponseEntity<Newsletter> createNewsletter(@RequestBody NewsletterRequest newsletterRequest) {
        try {
            Newsletter newsletter = new Newsletter();
            newsletter.setEmail(newsletterRequest.getEmail());
            newsletter.setActive(newsletterRequest.isActive());
            Newsletter createdNewsletter = newsletterRepository.save(newsletter);
            sendEmailNewsletter(newsletterRequest.getEmail());

            return new ResponseEntity<>(createdNewsletter, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private void sendEmailNewsletter(String to) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject("Dziękujemy za dołączenie do naszego newslettera!");
        mailMessage.setText("Szanowni Subskrybenci,\n" +
                "\n" +
                "Dziękujemy za dołączenie do naszego newslettera!\n" +
                "\n" +
                "Chcieliśmy Was poinformować, że dzięki subskrypcji naszego newslettera nie ominie Was żadna z naszych aktualnych promocji. Będziecie na bieżąco otrzymywać informacje o wszystkich fantastycznych ofertach i zniżkach, które przygotowaliśmy specjalnie dla Was.\n" +
                "\n" +
                "Sprawdźcie naszą stronę internetową lub odwiedźcie nasz sklep online, aby zobaczyć, co mamy dla Was teraz!\n" +
                "\n" +
                "Dziękujemy, że jesteście z nami!\n" +
                "\n" +
                "Z pozdrowieniami,\n" +
                "Beloobel\n\n---\nTa wiadomość została wygenerowana automatycznie. Prosimy o nie odpowiadanie.");
        mailSender.send(mailMessage);
    }
}
