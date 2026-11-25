package bg.tu_varna.ii.restaurantmanager.model.dto.response.reports;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderStatus;
import java.time.LocalDateTime;

public class CurrentOrderReportDto {
    private final Long orderId;
    private final Integer tableNumber;
    private final String waiterName;
    private final OrderStatus status;
    private final LocalDateTime createdAt;

    public CurrentOrderReportDto(Long orderId, Integer tableNumber, String waiterName, OrderStatus status, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.waiterName = waiterName;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getOrderId() {
        return orderId;
    }
    public Integer getTableNumber() {
        return tableNumber;
    }
    public String getWaiterName() {
        return waiterName;
    }
    public OrderStatus getStatus() {
        return status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
