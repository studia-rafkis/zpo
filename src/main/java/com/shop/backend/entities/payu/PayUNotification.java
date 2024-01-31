package com.shop.backend.entities.payu;//package com.shop.backend.entities.payu;
//
//import com.shop.backend.entities.Order;
//import lombok.Data;
//import org.hibernate.mapping.Property;
//
//import java.util.List;
//
//@Data
//public class PayUNotification {
//    private OrderDetail order;
//    private List<PropertyDetail> properties;
//
//    @Data
//    public static class OrderDetail {
//        private String extOrderId;
//        private String orderCreateDate;
//        private Long totalAmount;
//        private String status;
//    }
//
//    @Data
//    public static class PropertyDetail {
//        private String name;
//        private String value;
//    }
//}