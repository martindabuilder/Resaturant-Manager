package bg.tu_varna.ii.restaurantmanager.model.dto.response;
import bg.tu_varna.ii.restaurantmanager.model.entity.Payment;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.PaymentMethod;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponseDto {
    private final Long id;
    private final Long billId;
    private final BigDecimal amount;
    private final PaymentStatus status;
    private final PaymentMethod method;
    private final LocalDateTime createdAt;
    private final LocalDateTime paidAt;

    public PaymentResponseDto(Payment payment) {
        this.id = payment.getId();
        this.billId = payment.getBill().getId();
        this.amount = payment.getAmount();
        this.status = payment.getPaymentStatus();
        this.method = payment.getPaymentMethod();
        this.createdAt = payment.getCreatedAt();
        this.paidAt = payment.getPaidAt();
    }

    public Long getId() {return id;}

    public Long getBillId() {return billId;}

    public PaymentStatus getStatus() {return status;}

    public LocalDateTime getPaidAt() {return paidAt;}

    public LocalDateTime getCreatedAt() {return createdAt;}

    public PaymentMethod getMethod() {return method;}

    public BigDecimal getAmount() {return amount;}
}
