package zpo.zpo.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "products")
@Data
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idp;

    private boolean availability;
    private String category;
    private String description;
    private String name;
    private double price;
    @Column(name = "is_carousel")
    private boolean carousel;
    private Long indexCarousel;

    private double oldPrice;
    @Column(name = "is_discount")
    private boolean discount;

    private boolean visibility;
    private String selectedDimensions;
    private String selectedMaterials;
    @Transient
    private List<Boolean> dimensions;
    @Transient
    private List<Boolean> materials;

    @Transient
    private List<String> imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ProductImage> images;

    public void convertDimensionsToString() {
        if (dimensions != null) {
            StringBuilder sb = new StringBuilder();
            for (Boolean dimension : dimensions) {
                sb.append(dimension ? "1" : "0");
            }
            this.selectedDimensions = sb.toString();
        }
    }

    public void convertMaterialsToString() {
        if (materials != null) {
            StringBuilder sb = new StringBuilder();
            for (Boolean material : materials) {
                sb.append(material ? "1" : "0");
            }
            this.selectedMaterials = sb.toString();
        }
    }
}
