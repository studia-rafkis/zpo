package com.shop.backend.dtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SiteSettingsRequest {
    private Long ids;
    private String backgroundColor;
    private Long fontSize;
    private String fontFamily;
}