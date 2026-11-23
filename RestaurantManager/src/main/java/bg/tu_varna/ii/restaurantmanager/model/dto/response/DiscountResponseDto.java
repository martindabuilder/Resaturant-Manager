package bg.tu_varna.ii.restaurantmanager.model.dto.response;


import bg.tu_varna.ii.restaurantmanager.model.entity.Discount;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
public class DiscountResponseDto {

    private final Long id;
    private final String name;
    private final String description;
    private final DiscountType type;
    private final BigDecimal value;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final boolean active;
    private final LocalDateTime createdAt;

    public DiscountResponseDto(Discount discount) {
        this.id = discount.getId();
        this.name = discount.getName();
        this.description = discount.getDescription();
        this.type = discount.getType();
        this.value = discount.getValue();
        this.startDate = discount.getStartDate();
        this.endDate = discount.getEndDate();
        this.active = discount.isActive();
        this.createdAt = discount.getCreatedAt();
    }

    public Long getId() {return id;}
    public String getName() {return name;}
    public String getDescription() {return description;}

    public DiscountType getType() {return type;}

    public BigDecimal getValue() {return value;}

    public LocalDateTime getStartDate() {return startDate;}
    public LocalDateTime getEndDate() {return endDate;}

    public boolean isActive() {return active;}

    public LocalDateTime getCreatedAt() {return createdAt;}
}