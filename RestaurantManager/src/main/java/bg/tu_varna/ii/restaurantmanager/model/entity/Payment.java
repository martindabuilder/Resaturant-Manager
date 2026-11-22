package bg.tu_varna.ii.restaurantmanager.model.entity;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.PaymentMethod;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.PaymentStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime paidAt;

    protected Payment() {}
    public Payment(PaymentMethod method, PaymentStatus status, Bill bill, BigDecimal amount) {
        this.method = method;
        this.status = status;
        this.bill = bill;
        this.amount = amount;
    }

    @PrePersist
    protected void onCreate() {createdAt = LocalDateTime.now();}

    public Long getId() {return id;}

    public Bill getBill() {return bill;}
    public void setBill(Bill bill) {this.bill = bill;}

    public BigDecimal getAmount() {return amount;}
    public void setAmount(BigDecimal amount) {this.amount = amount;}

    public PaymentMethod getPaymentMethod() {return method;}
    public void setPaymentMethod(PaymentMethod method) {this.method = method;}

    public PaymentStatus getPaymentStatus() {return status;}
    public void setPaymentStatus(PaymentStatus status) {this.status = status;}

    public LocalDateTime getPaidAt() {return paidAt;}
    public void setPaidAt(LocalDateTime paidAt) {this.paidAt = paidAt;}

    public LocalDateTime getCreatedAt() {return createdAt;}
}
