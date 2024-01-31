package com.shop.backend.services;
import com.shop.backend.entities.User;
import com.shop.backend.entities.VerificationToken;
import com.shop.backend.repositories.UserRepository;
import com.shop.backend.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class VerificationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Autowired
    private JavaMailSender mailSender;

    public void registerUser(User user) {
        userRepository.save(user);

        if(!user.isOauth_provider())
        {
            String token = UUID.randomUUID().toString();
            VerificationToken verificationToken = new VerificationToken(token, user);
            tokenRepository.save(verificationToken);
            sendVerificationEmail(user.getLogin(), token);
        }

    }
    public void sendVerificationEmail(String recipient, String token) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipient);
        email.setSubject("Weryfikacja");
        email.setText("Link do aktywacji konta: "
                + "http://localhost:8080/verify?token=" + token);
        mailSender.send(email);
    }
    public boolean verifyUser(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken != null) {
            User user = userRepository.findById(verificationToken.getUser().getId()).orElse(null);
            if (user != null && !user.isVerified()) {
                user.setVerified(true);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}