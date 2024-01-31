package com.shop.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.shop.backend.entities.Order;
import com.shop.backend.entities.payu.AuthResponse;
import com.shop.backend.entities.payu.CreateOrderRequest;
//import com.shop.backend.entities.payu.PayUNotification;
import com.shop.backend.entities.payu.PaymentInfo;
//import com.shop.backend.repositories.PaymentInfoRepository;
import com.shop.backend.repositories.OrderRepository;
import com.shop.backend.repositories.PaymentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
@Service
public class PayUService {
    private static final String PAYU_AUTH_URL = "https://secure.snd.payu.com/pl/standard/user/oauth/authorize";
    private static final String PAYU_API_URL = "https://secure.snd.payu.com/api/v2_1/orders";
    private static final String CLIENT_ID = "475486";
    private static final String CLIENT_SECRET = "cdc75575c9727f3a92fd9d5b41423a5b";
    @Autowired
    private PaymentInfoRepository paymentInfoRepository;
    @Autowired
    private OrderRepository orderRepository;
    public AuthResponse authenticate() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        map.add("client_id", CLIENT_ID);
        map.add("client_secret", CLIENT_SECRET);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(PAYU_AUTH_URL, entity, AuthResponse.class);
        return response.getBody();
    }

    public String createOrder(CreateOrderRequest request) {
        String apiKey = request.getApiKey();
        String jsonPayload = request.getOrderData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpRequest = new HttpEntity<>(jsonPayload, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(PAYU_API_URL, httpRequest, String.class);
        System.out.println((response.getBody()));
        return response.getBody();
    }
    public ResponseEntity<String> handleNotification(String payload, String signature, String xSignature) {
        System.out.println("Otrzymano powiadomienie od PayU: " + payload);

        boolean isSignatureValid = verifySignature(payload, signature);
        if (!isSignatureValid) {
            System.out.println("Weryfikacja podpisu nie powiodła się!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
        }
        System.out.println("Weryfikacja podpisu przebiegła pomyślnie.");

        JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();
        JsonObject order = jsonObject.getAsJsonObject("order");

        String orderId = order.get("orderId").getAsString();
        String extOrderId = order.get("extOrderId").getAsString();
        String orderCreateDate = order.get("orderCreateDate").getAsString();
        Long totalAmount = order.get("totalAmount").getAsLong();
        String status = order.get("status").getAsString();

        String paymentId = null;
        JsonArray propertiesArray = jsonObject.getAsJsonArray("properties");
        for (JsonElement element : propertiesArray) {
            JsonObject propertyObject = element.getAsJsonObject();
            String propertyName = propertyObject.get("name").getAsString();
            if ("PAYMENT_ID".equals(propertyName)) {
                paymentId = propertyObject.get("value").getAsString();
                break;
            }
        }

        Optional<Order> orderOptional = orderRepository.findById(Long.parseLong(extOrderId));
        if (orderOptional.isPresent()) {
            Order orderEntity = orderOptional.get();
            PaymentInfo paymentInfo = orderEntity.getPaymentInfo();

            paymentInfo.setPayuOrderId(orderId);
            paymentInfo.setOrderCreateDate(orderCreateDate);
            paymentInfo.setTotalAmount(totalAmount);
            paymentInfo.setPaymentStatus(status);
            paymentInfo.setPaymentId(paymentId);
            paymentInfo.setNew(true);

            paymentInfoRepository.save(paymentInfo);
        } else {
            System.out.println("Nie znaleziono zamówienia o id: " + extOrderId);
        }

        return ResponseEntity.ok("Notification processed");
    }

    private boolean verifySignature(String payload, String signature) {

        String secret = "18602dfd2793901eb87ec3572a26a494";

        String[] parts = signature.split(";");
        Map<String, String> signatureParams = new HashMap<>();
        for (String part : parts) {
            String[] keyValue = part.split("=");
            signatureParams.put(keyValue[0], keyValue[1]);
        }

        String algorithm = signatureParams.get("algorithm");
        if (!"MD5".equals(algorithm)) {

            System.out.println("Nieobsługiwany algorytm: " + algorithm);
            return false;
        }

        String expectedSignature = md5(payload + secret);

        return expectedSignature.equals(signatureParams.get("signature"));
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Nie można znaleźć algorytmu MD5", e);
        }
    }
}