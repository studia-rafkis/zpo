package com.shop.backend.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.backend.dtos.OrderPaymentInfoDto;
import com.shop.backend.dtos.OrderProductRequest;
import com.shop.backend.dtos.OrderRequest;
import com.shop.backend.dtos.ProductDetailsDto;
import com.shop.backend.entities.*;
import com.shop.backend.entities.payu.PaymentInfo;
import com.shop.backend.repositories.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.shop.backend.entities.ProductImage;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    PromocodeRepository promocodeRepository;
    @Autowired
    private PaymentInfoRepository paymentInfoRepository;
    @GetMapping("/all")
    public List<OrderPaymentInfoDto> getAllOrders() {
        return orderRepository.findAllWithPaymentInfo();
    }
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id).orElse(null);
    }
    @PostMapping("/new")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        Order order = new Order();
        order.setCompany(orderRequest.isCompany());
        order.setActive(orderRequest.isActive());
        order.setFirstName(orderRequest.getFirstName());
        order.setLastName(orderRequest.getLastName());
        order.setEmail(orderRequest.getEmail());
        order.setPhone(orderRequest.getPhone());
        order.setStreet(orderRequest.getStreet());
        order.setStreetNumber(orderRequest.getStreetNumber());
        order.setHouseNumber(orderRequest.getHouseNumber());
        order.setCountry(orderRequest.getCountry());
        order.setState(orderRequest.getState());
        order.setZipCode(orderRequest.getZipCode());
        order.setFirstName2(orderRequest.getFirstName2());
        order.setLastName2(orderRequest.getLastName2());
        order.setEmail2(orderRequest.getEmail2());
        order.setPhone2(orderRequest.getPhone2());
        order.setStreet2(orderRequest.getStreet2());
        order.setStreetNumber2(orderRequest.getStreetNumber2());
        order.setHouseNumber2(orderRequest.getHouseNumber2());
        order.setCountry2(orderRequest.getCountry2());
        order.setState2(orderRequest.getState2());
        order.setZipCode2(orderRequest.getZipCode2());
        order.setOrderNotes(orderRequest.getOrderNotes());
        order.setOrderStatus(orderRequest.getOrderStatus());
        Long promocodeId = orderRequest.getPromoCodeId();

        if (promocodeId != null && promocodeId != 0) {
            Promocode promocode = promocodeRepository.findById(promocodeId)
                    .orElseThrow(() -> new RuntimeException("Promocode o ID: " + promocodeId + " nie został znaleziony"));
            order.setPromocode(promocode);
        }

        List<OrderProduct> orderProducts = new ArrayList<>();
        for(OrderProductRequest opRequest : orderRequest.getOrderProducts()) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setQuantity(opRequest.getQuantity());
            orderProduct.setMaterial(opRequest.getMaterial());
            orderProduct.setSize(opRequest.getSize());
            orderProduct.setOrder(order);

            Product product = productRepository.findById(opRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produkt o ID: " + opRequest.getProductId() + " nie został znaleziony"));
            orderProduct.setProduct(product);
            orderProducts.add(orderProduct);
        }
        order.setOrderProducts(orderProducts);
        Order savedOrder = orderRepository.save(order);
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrder(savedOrder);
        paymentInfoRepository.save(paymentInfo);
        return ResponseEntity.ok(savedOrder);
    }
    @PutMapping("/update/{orderId}")
    public ResponseEntity<Order> updateOrderActiveStatus(
            @PathVariable Long orderId,
            @RequestParam Boolean newActiveStatus) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Zamówienie o ID: " + orderId + " nie zostało znalezione"));
        order.setActive(newActiveStatus);
        Order updatedOrder = orderRepository.save(order);
        return ResponseEntity.ok(updatedOrder);
    }
    @GetMapping("/findByOrderId")
    public ResponseEntity<List<OrderProduct>> findByOrderId(@RequestParam Long ido) {
        List<OrderProduct> orderProducts = orderProductRepository.findByOrder_Ido(ido);

        if (orderProducts.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(orderProducts);
        }
    }
    @GetMapping("/getIdp/{id}")
    public ResponseEntity<ProductDetailsDto> getProductById(@PathVariable Long id) {
        OrderProduct orderProduct = orderProductRepository.findById(id).orElse(null);

        if (orderProduct == null) {
            return ResponseEntity.notFound().build();
        } else {
            ProductDetailsDto productDetailsDTO = new ProductDetailsDto();
            productDetailsDTO.setId(orderProduct.getProduct().getIdp());
            productDetailsDTO.setAvailability(orderProduct.getProduct().isAvailability());
            productDetailsDTO.setCategory(orderProduct.getProduct().getCategory());
            productDetailsDTO.setDescription(orderProduct.getProduct().getDescription());
            productDetailsDTO.setName(orderProduct.getProduct().getName());
            productDetailsDTO.setPrice(orderProduct.getProduct().getPrice());
            productDetailsDTO.setOldPrice(orderProduct.getProduct().getOldPrice());
            productDetailsDTO.setDiscount(orderProduct.getProduct().isDiscount());
            productDetailsDTO.setVisibility(orderProduct.getProduct().isVisibility());
            productDetailsDTO.setSelectedDimensions(orderProduct.getProduct().getSelectedDimensions());
            productDetailsDTO.setSelectedMaterials(orderProduct.getProduct().getSelectedMaterials());

            List<String> imageUrls = new ArrayList<>();
            for (ProductImage productImage : orderProduct.getProduct().getImages()) {
                imageUrls.add(productImage.getSrc());
            }
            productDetailsDTO.setImageUrls(imageUrls);
            return ResponseEntity.ok(productDetailsDTO);
        }
    }

    @PutMapping("/updateStatus/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Zamówienie o ID: " + orderId + " nie zostało znalezione"));
        order.setOrderStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        return ResponseEntity.ok(updatedOrder);
    }
}
