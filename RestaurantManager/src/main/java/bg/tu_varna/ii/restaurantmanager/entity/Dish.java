package bg.tu_varna.ii.restaurantmanager.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "dishes")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Integer weight;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean available = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    protected Dish() {}

    public Dish(String name, String description, Integer weight, BigDecimal price, Category category) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.price = price;
        this.category = category;
        this.available = true;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {return id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public Integer getWeight() {return weight;}

    public void setWeight(Integer weight) {this.weight = weight;}

    public BigDecimal getPrice() {return price;}

    public void setPrice(BigDecimal price) {this.price = price;}

    public boolean isAvailable() {return available;}

    public void setAvailable(boolean available) {this.available = available;}

    public LocalDateTime getCreatedAt() {return createdAt;}

    public Category getCategory() {return category;}

    public void setCategory(Category category) {this.category = category;}
}
