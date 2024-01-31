package com.shop.backend.entities;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "site_settings")
@Data
public class SiteSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ids;
    private String backgroundColor;
    private Long fontSize;
    private String fontFamily;
}