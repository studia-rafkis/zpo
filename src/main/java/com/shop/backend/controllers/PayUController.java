package com.shop.backend.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.shop.backend.entities.payu.AuthResponse;
import com.shop.backend.entities.payu.CreateOrderRequest;
import com.shop.backend.entities.payu.IpResponse;
import com.shop.backend.entities.payu.PaymentInfo;
//import com.shop.backend.repositories.PaymentInfoRepository;
import com.shop.backend.services.PayUService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;
@RestController
public class PayUController {
    @Autowired
    private PayUService payUService;
    @GetMapping("/authenticate")
    public AuthResponse authenticate() {
        return payUService.authenticate();
    }
    private static final String PAYU_API_URL = "https://secure.snd.payu.com/api/v2_1/orders";

@PostMapping("/createOrder")
public ResponseEntity<String> createOrder(@RequestBody CreateOrderRequest request) throws JsonProcessingException {
    String response = payUService.createOrder(request);
    System.out.println("Response from PayU: " + response);
    return ResponseEntity.ok(response);
}
    @PostMapping("/payu-callback")
    public ResponseEntity<String> handlePayUNotification(@RequestBody String payload,
                                                         @RequestHeader("OpenPayu-Signature") String signature,
                                                         @RequestHeader("X-OpenPayU-Signature") String xSignature) {
        return payUService.handleNotification(payload, signature, xSignature);
    }
    @GetMapping(value = "/processing", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IpResponse getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");

        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }

        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }

        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        return new IpResponse(clientIp);
    }
}

