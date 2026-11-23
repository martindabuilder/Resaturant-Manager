package bg.tu_varna.ii.restaurantmanager.model.entity;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.DiscountType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private RestaurantOrder order;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotalAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    private Long discountId;

    @Column(length = 100)
    private String discountName;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private DiscountType discountType;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Bill() {}
    public Bill(RestaurantOrder order, BigDecimal totalAmount) {
        this.order = order;
        this.subtotalAmount = totalAmount;
        this.discountAmount = BigDecimal.ZERO;
        this.totalAmount = totalAmount;
    }

    @PrePersist
    protected void onCreate() {createdAt = LocalDateTime.now();}

    public Long getId() {return id;}

    public RestaurantOrder getOrder() {return order;}

    public void setOrder(RestaurantOrder order) {this.order = order;}
    public BigDecimal getSubtotalAmount() {return subtotalAmount;}

    public void setSubtotalAmount(BigDecimal subtotalAmount) {this.subtotalAmount = subtotalAmount;}

    public BigDecimal getDiscountAmount() {return discountAmount;}
    public void setDiscountAmount(BigDecimal discountAmount) {this.discountAmount = discountAmount;}

    public BigDecimal getTotalAmount() {return totalAmount;}
    public void setTotalAmount(BigDecimal totalAmount) {this.totalAmount = totalAmount;}

    public Long getDiscountId() {return discountId;}
    public void setDiscountId(Long discountId) {this.discountId = discountId;}

    public String getDiscountName() {return discountName;}
    public void setDiscountName(String discountName) {this.discountName = discountName;}

    public DiscountType getDiscountType() {return discountType;}
    public void setDiscountType(DiscountType discountType) {this.discountType = discountType;}

    public BigDecimal getDiscountValue() {return discountValue;}
    public void setDiscountValue(BigDecimal discountValue) {this.discountValue = discountValue;}

    public LocalDateTime getCreatedAt() {return createdAt;}
}
