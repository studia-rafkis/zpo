package com.shop.backend.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.backend.entities.Product;
import com.shop.backend.entities.ProductImage;
import com.shop.backend.entities.Promocode;
import com.shop.backend.repositories.PromocodeRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;
@RestController
@RequestMapping("/promocode")
public class PromocodeController {
    @Autowired
    private PromocodeRepository promocodeRepository;
    @GetMapping("/all")
    public List<Promocode> getAllPromocodes() {
        return promocodeRepository.findAll();
    }
    @PostMapping("/add")
    public Promocode createPromocode(@RequestBody Promocode promocode) {
        return promocodeRepository.save(promocode);
    }
    @PostMapping("/new")
    public ResponseEntity<?> createNewPromocode(@RequestParam("promocode") String promocodeJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Promocode promocode = objectMapper.readValue(promocodeJson, Promocode.class);
            Promocode createdPromocode = promocodeRepository.save(promocode);
            return new ResponseEntity<>(createdPromocode, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create new promocode: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Promocode> updatePromocodeActive(@PathVariable Long id, MultipartHttpServletRequest request) throws Exception {
        Optional<Promocode> existingPromocodeOpt = promocodeRepository.findById(id);
        if (!existingPromocodeOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String promocodeData = request.getParameter("promocode");
        ObjectMapper objectMapper = new ObjectMapper();
        Promocode receivedPromocode = objectMapper.readValue(promocodeData, Promocode.class);
        Promocode existingPromocode = existingPromocodeOpt.get();
        existingPromocode.setActive(receivedPromocode.isActive());
        Promocode savedPromoCode = promocodeRepository.save(existingPromocode);

        return new ResponseEntity<>(savedPromoCode, HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<Promocode> updatePromocodeStatus(@PathVariable Long id, MultipartHttpServletRequest request) throws Exception {
        Optional<Promocode> existingPromocodeOpt = promocodeRepository.findById(id);
        if (!existingPromocodeOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String promocodeData = request.getParameter("promocode");
        ObjectMapper objectMapper = new ObjectMapper();
        Promocode receivedPromocode = objectMapper.readValue(promocodeData, Promocode.class);
        Promocode existingPromocode = existingPromocodeOpt.get();
        existingPromocode.setDeleted(receivedPromocode.isDeleted());
        existingPromocode.setActive(receivedPromocode.isActive());
        Promocode savedPromoCode = promocodeRepository.save(existingPromocode);
        return new ResponseEntity<>(savedPromoCode, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public void deletePromocode(@PathVariable Long id) {
        promocodeRepository.deleteById(id);
    }
}
