package bg.tu_varna.ii.restaurantmanager.model.dto.response;
import bg.tu_varna.ii.restaurantmanager.model.entity.Bill;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BillResponseDto {
    private final Long id;
    private final Long orderId;
    private final Integer tableNumber;
    private final BigDecimal totalAmount;
    private final LocalDateTime createdAt;

    public BillResponseDto(Bill bill) {
        this.id = bill.getId();
        this.orderId = bill.getOrder().getId();
        this.tableNumber = bill.getOrder().getTable().getTableNumber();
        this.totalAmount = bill.getTotalAmount();
        this.createdAt = bill.getCreatedAt();
    }

    public Long getId() {return id;}

    public LocalDateTime getCreatedAt() {return createdAt;}

    public BigDecimal getTotalAmount() {return totalAmount;}

    public Long getOrderId() {return orderId;}

    public Integer getTableNumber() {return tableNumber;}
}
