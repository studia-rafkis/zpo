package zpo.zpo.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import zpo.zpo.services.VerificationService;

@RestController
public class VerificationController {

    @Autowired
    private VerificationService verificationService;

    @GetMapping("/verify")
    public RedirectView verifyAccount(@RequestParam("token") String token) {
        if (verificationService.verifyUser(token)) {
            return new RedirectView("http://localhost:4200/guest/login?status=verified");
        } else {
            return new RedirectView("http://localhost:4200/guest/login?status=failed");
        }
    }
}