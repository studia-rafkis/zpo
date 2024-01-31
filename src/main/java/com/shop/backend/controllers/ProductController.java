package com.shop.backend.controllers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.backend.entities.Product;
import com.shop.backend.entities.ProductImage;
import com.shop.backend.repositories.ProductImageRepository;
import com.shop.backend.repositories.ProductRepository;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    @GetMapping("/id/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id).orElse(null);
    }
private static final Path UPLOAD_DIR = Paths.get("var/myapp/productImages");
    @GetMapping("/image/{imageName:.+}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws IOException {
        Path imagePath = UPLOAD_DIR.resolve(imageName);
        byte[] image = Files.readAllBytes(imagePath);
        HttpHeaders headers = new HttpHeaders();
        String contentType = determineContentType(imageName);
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentLength(image.length);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }
    private String determineContentType(String imageName) {
        String extension = FilenameUtils.getExtension(imageName).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "webp":
                return "image/webp";
            default:
                return "image/jpeg";
        }
    }
    @PostMapping("/add")
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }
    @Setter
    @Getter
    public static class ImageInfo {
        private Long id;
        private String src;
    }
    @PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> updateProduct(
            @PathVariable Long productId,
            @RequestParam("product") String productJson,
            @RequestParam(value = "images", required = false) MultipartFile[] newImages,
            @RequestParam("imagesToRemove") String imagesToRemoveJson) {

        System.out.println("ID : " + productId);
        System.out.println("Received Product JSON: " + productJson);
        System.out.println("Received imagesToRemove JSON: " + imagesToRemoveJson);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> response = new HashMap<>();

        try {
            Product updatedProduct = objectMapper.readValue(productJson, Product.class);

            Product existingProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

            List<ImageInfo> imagesToRemoveList = objectMapper.readValue(imagesToRemoveJson, new TypeReference<List<ImageInfo>>() {});

            for (ImageInfo imageInfo : imagesToRemoveList) {
                Long imageId = imageInfo.getId();
                existingProduct.getImages().removeIf(image -> image.getId().equals(imageId));
                productImageRepository.deleteImage(imageId);
            }

            if (newImages != null && newImages.length > 0) {
                try {
                    List<String> uploadedFileNames = new ArrayList<>();

                    for (MultipartFile newImage : newImages) {
                        String originalFileName = newImage.getOriginalFilename();
                        String baseName = FilenameUtils.getBaseName(originalFileName);
                        String extension = FilenameUtils.getExtension(originalFileName);

                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        String uniqueId = "_" + String.format("%d", timestamp.getTime());
                        String newFileName = baseName + uniqueId + "." + extension;

                        byte[] bytes = newImage.getBytes();
                        Path path = UPLOAD_DIR.resolve(newFileName);
                        Files.write(path, bytes);
                        uploadedFileNames.add(newFileName);
                    }

                    List<ProductImage> imageList = existingProduct.getImages();
                    for (String imageUrl : uploadedFileNames) {
                        ProductImage productImage = new ProductImage();
                        productImage.setSrc(imageUrl);
                        productImage.setProduct(existingProduct);
                        imageList.add(productImageRepository.save(productImage));
                    }

                    existingProduct.setImages(imageList);

                } catch (Exception e) {
                    e.printStackTrace();
                    response.put("error", "Unable to upload files. Reason: " + e.getMessage());
                    return ResponseEntity.badRequest().body(response);
                }
            }
            existingProduct.setIdp(productId);
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setSelectedDimensions(updatedProduct.getSelectedDimensions());
            existingProduct.setSelectedMaterials(updatedProduct.getSelectedMaterials());
            existingProduct.setVisibility(updatedProduct.isVisibility());
            existingProduct.setDiscount(updatedProduct.isDiscount());
            existingProduct.setOldPrice(updatedProduct.getOldPrice());
            existingProduct.setAvailability(updatedProduct.isAvailability());
            existingProduct.setCarousel(updatedProduct.isCarousel());
            existingProduct.setIndexCarousel(updatedProduct.getIndexCarousel());
            Product savedProduct = productRepository.save(existingProduct);
            response.put("message", "Product updated successfully");
            response.put("updatedProductName", savedProduct.getName());
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Invalid JSON"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "An error occurred"));
        }
    }
    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> addProduct(
            @RequestParam("product") String productJson,
            @RequestParam(value = "image", required = false) MultipartFile[] files) {

        Product product = null;
        try {
            product = new ObjectMapper().readValue(productJson, Product.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Map<String, String> response = new HashMap<>();
        Random random = new Random();

        List<String> uploadedFileNames = new ArrayList<>();
        if (files != null) {
            try {
                for (MultipartFile file : files) {
                    String originalFileName = file.getOriginalFilename();
                    String baseName = FilenameUtils.getBaseName(originalFileName);
                    String extension = FilenameUtils.getExtension(originalFileName);

                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        String uniqueId = "_" + String.format("%d", timestamp.getTime());
                        String newFileName = baseName + uniqueId + "." + extension;
                        byte[] bytes = file.getBytes();
                        Path path = UPLOAD_DIR.resolve(newFileName);
                        Files.write(path, bytes);
                        uploadedFileNames.add(newFileName);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.put("error", "Nie udało się przesłać plików. Powód: " + e.getMessage());
                return ResponseEntity.badRequest().body(response);
            }
        }
        try {
            product.convertDimensionsToString();
            product.convertMaterialsToString();
            product = productRepository.save(product);
            List<ProductImage> imageList = new ArrayList<>();

            for (String imageUrl : uploadedFileNames) {
                ProductImage productImage = new ProductImage();
                productImage.setSrc(imageUrl);
                productImage.setProduct(product);
                imageList.add(productImageRepository.save(productImage));
            }
            product.setImages(imageList);
            productRepository.save(product);
            response.put("message", "Produkt został dodany");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Nie udało się dodać produktu. Powód: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}


