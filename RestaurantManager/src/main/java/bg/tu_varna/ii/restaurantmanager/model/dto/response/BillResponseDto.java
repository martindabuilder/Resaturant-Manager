package bg.tu_varna.ii.restaurantmanager.model.dto.response;
import bg.tu_varna.ii.restaurantmanager.model.entity.Bill;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.DiscountType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BillResponseDto {
    private final Long id;
    private final Long orderId;
    private final Integer tableNumber;

    private final BigDecimal subtotalAmount;
    private final BigDecimal discountAmount;
    private final BigDecimal totalAmount;

    private final Long discountId;
    private final String discountName;
    private final DiscountType discountType;
    private final BigDecimal discountValue;
    private final LocalDateTime createdAt;

    public BillResponseDto(Bill bill) {
        this.id = bill.getId();
        this.orderId = bill.getOrder().getId();
        this.tableNumber = bill.getOrder().getTable().getTableNumber();
        this.subtotalAmount = bill.getSubtotalAmount();
        this.discountAmount = bill.getDiscountAmount();
        this.totalAmount = bill.getTotalAmount();
        this.discountId = bill.getDiscountId();
        this.discountName = bill.getDiscountName();
        this.discountType = bill.getDiscountType();
        this.discountValue = bill.getDiscountValue();
        this.createdAt = bill.getCreatedAt();
    }

    public Long getId() {return id;}
    public Long getOrderId() {return orderId;}
    public Integer getTableNumber() {return tableNumber;}
    public BigDecimal getSubtotalAmount() {return subtotalAmount;}
    public BigDecimal getDiscountAmount() {return discountAmount;}
    public BigDecimal getTotalAmount() {return totalAmount;}
    public Long getDiscountId() {return discountId;}
    public String getDiscountName() {return discountName;}
    public DiscountType getDiscountType() {return discountType;}
    public BigDecimal getDiscountValue() {return discountValue;}
    public LocalDateTime getCreatedAt() {return createdAt;}
}
