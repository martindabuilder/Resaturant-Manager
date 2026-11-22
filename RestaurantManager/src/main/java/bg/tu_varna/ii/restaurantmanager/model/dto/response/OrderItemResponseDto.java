package bg.tu_varna.ii.restaurantmanager.model.dto.response;
import bg.tu_varna.ii.restaurantmanager.model.entity.OrderItem;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderItemStatus;
import java.math.BigDecimal;

public class OrderItemResponseDto {
    private final Long id;
    private final Long dishId;
    private final String dishName;
    private final Integer quantity;
    private final BigDecimal unitPrice;
    private final OrderItemStatus status;

    public OrderItemResponseDto(OrderItem item) {
        this.id = item.getId();
        this.dishId = item.getDish().getId();
        this.dishName = item.getDish().getName();
        this.quantity = item.getQuantity();
        this.unitPrice = item.getUnitPrice();
        this.status = item.getStatus();
    }

    public Long getId() {return id;}

    public Long getDishId() {return dishId;}

    public String getDishName() {return dishName;}

    public Integer getQuantity() {return quantity;}

    public BigDecimal getUnitPrice() {return unitPrice;}

    public OrderItemStatus getStatus() {return status;}
}