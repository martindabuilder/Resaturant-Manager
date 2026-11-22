package bg.tu_varna.ii.restaurantmanager.model.dto.response;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantOrder;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {
    private final Long id;
    private final Integer tableNumber;
    private final Long waiterId;
    private final String waiterUsername;
    private final OrderStatus status;
    private final LocalDateTime createdAt;
    private final List<OrderItemResponseDto> items;

    public OrderResponseDto(RestaurantOrder order) {
        this.id = order.getId();
        this.tableNumber = order.getTable().getTableNumber();
        this.waiterId = order.getWaiter().getId();
        this.waiterUsername = order.getWaiter().getUsername();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();
        this.items = order.getItems().stream().map(OrderItemResponseDto::new).toList();
    }

    public Long getId() {return id;}

    public Integer getTableNumber() {return tableNumber;}

    public Long getWaiterId() {return waiterId;}

    public String getWaiterUsername() {return waiterUsername;}

    public OrderStatus getStatus() {return status;}

    public LocalDateTime getCreatedAt() {return createdAt;}

    public List<OrderItemResponseDto> getItems() {return items;}
}
