package bg.tu_varna.ii.restaurantmanager.dto;

import bg.tu_varna.ii.restaurantmanager.entity.Category;
import java.time.LocalDateTime;

public class CategoryResponseDto {
    private final Long id;
    private final String name;
    private final String description;
    private final boolean active;
    private final LocalDateTime createdAt;

    public CategoryResponseDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.active = category.isActive();
        this.createdAt = category.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}