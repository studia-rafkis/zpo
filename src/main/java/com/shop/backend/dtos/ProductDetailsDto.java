package com.shop.backend.dtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailsDto {
    private Long id;
    private boolean availability;
    private String category;
    private String description;
    private String name;
    private double price;
    private double oldPrice;
    private boolean discount;
    private boolean visibility;
    private String selectedDimensions;
    private String selectedMaterials;
    private List<String> imageUrls;
}
