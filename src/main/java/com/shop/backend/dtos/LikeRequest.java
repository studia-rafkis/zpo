package com.shop.backend.dtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeRequest {
    private Long idcm;
    private Long idu;
    private boolean active;
}