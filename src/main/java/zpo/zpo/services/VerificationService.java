package zpo.zpo.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import zpo.zpo.entities.User;
import zpo.zpo.entities.VerificationToken;
import zpo.zpo.repositories.UserRepository;
import zpo.zpo.repositories.VerificationTokenRepository;

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
        // Save the user to the database (without verification)
        userRepository.save(user);

        // Generate a token for verification
        String token = UUID.randomUUID().toString();

        // Save the token associated with the user
        VerificationToken verificationToken = new VerificationToken(token, user);
        tokenRepository.save(verificationToken);

        // Send the email for verification
        sendVerificationEmail(user.getLogin(), token);
    }

    public void sendVerificationEmail(String recipient, String token) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipient);
        email.setSubject("Account Verification");
        email.setText("To verify your account, please click on the following link: "
                + "http://localhost:8080/verify?token=" + token);  // Adjust the URL as needed
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