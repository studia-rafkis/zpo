package com.shop.backend.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.backend.dtos.CommentRequest;
import com.shop.backend.dtos.SiteSettingsRequest;
import com.shop.backend.entities.*;
import com.shop.backend.repositories.SiteSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Optional;
@RestController
public class SiteSettingsController {
    @Autowired
    private SiteSettingsRepository siteSettingsRepository;
    @GetMapping("/settings/all")
    public List<SiteSettings> getAllSettings() {
        return siteSettingsRepository.findAll();
    }
    @PostMapping("/createNewSetting")
    public ResponseEntity<SiteSettings> createSetting(@RequestBody SiteSettingsRequest siteSettingsRequest) {
        try {
            SiteSettings siteSettings = new SiteSettings();
            siteSettings.setFontFamily(siteSettingsRequest.getFontFamily());
            siteSettings.setBackgroundColor(siteSettings.getBackgroundColor());
            siteSettings.setFontSize(siteSettings.getFontSize());
            SiteSettings createdSettings = siteSettingsRepository.save(siteSettings);
            return new ResponseEntity<>(createdSettings, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("settings/updateColor/{id}")
    public ResponseEntity<SiteSettings> updateColor(@PathVariable Long id, MultipartHttpServletRequest request) throws Exception {

        Optional<SiteSettings> existingSettingOpt = siteSettingsRepository.findById(id);
        if (!existingSettingOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String setting = request.getParameter("color");
        ObjectMapper objectMapper = new ObjectMapper();
        SiteSettings receivedSetting = objectMapper.readValue(setting, SiteSettings.class);
        SiteSettings existingSetting = existingSettingOpt.get();
        existingSetting.setBackgroundColor(receivedSetting.getBackgroundColor());
        SiteSettings savedSetting = siteSettingsRepository.save(existingSetting);
        return new ResponseEntity<>(savedSetting, HttpStatus.OK);
    }
    @PutMapping("settings/updateSize/{id}")
    public ResponseEntity<SiteSettings> updateSize(@PathVariable Long id, MultipartHttpServletRequest request) throws Exception {

        Optional<SiteSettings> existingSettingOpt = siteSettingsRepository.findById(id);
        if (!existingSettingOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String setting = request.getParameter("size");
        ObjectMapper objectMapper = new ObjectMapper();
        SiteSettings receivedSetting = objectMapper.readValue(setting, SiteSettings.class);
        SiteSettings existingSetting = existingSettingOpt.get();
        existingSetting.setFontSize(receivedSetting.getFontSize());
        SiteSettings savedSetting = siteSettingsRepository.save(existingSetting);
        return new ResponseEntity<>(savedSetting, HttpStatus.OK);
    }
}