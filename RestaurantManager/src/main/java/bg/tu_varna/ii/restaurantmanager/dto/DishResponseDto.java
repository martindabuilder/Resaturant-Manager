package bg.tu_varna.ii.restaurantmanager.dto;

import bg.tu_varna.ii.restaurantmanager.entity.Dish;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DishResponseDto {
    private Long id;
    private String name;
    private String description;
    private Integer weight;
    private BigDecimal price;
    private boolean available;
    private LocalDateTime createdAt;
    private Long categoryId;
    private String categoryName;

    public DishResponseDto(Dish dish) {
        this.id = dish.getId();
        this.name = dish.getName();
        this.description = dish.getDescription();
        this.weight = dish.getWeight();
        this.price = dish.getPrice();
        this.available = dish.isAvailable();
        this.createdAt = dish.getCreatedAt();
        this.categoryId = dish.getCategory().getId();
        this.categoryName = dish.getCategory().getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getWeight() {
        return weight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getDescription() {
        return description;
    }
}

