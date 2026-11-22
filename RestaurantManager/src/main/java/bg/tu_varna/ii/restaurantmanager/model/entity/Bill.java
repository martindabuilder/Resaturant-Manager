package bg.tu_varna.ii.restaurantmanager.model.entity;
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

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, unique = false)
    private LocalDateTime createdAt;

    protected Bill() {}
    public Bill(RestaurantOrder order, BigDecimal totalAmount) {
        this.order = order;
        this.totalAmount = totalAmount;
    }

    @PrePersist
    protected void onCreate() {createdAt = LocalDateTime.now();}

    public Long getId() {return id;}

    public RestaurantOrder getOrder() {return order;}

    public void setOrder(RestaurantOrder order) {this.order = order;}

    public BigDecimal getTotalAmount() {return totalAmount;}

    public void setTotalAmount(BigDecimal totalAmount) {this.totalAmount = totalAmount;}

    public LocalDateTime getCreatedAt() {return createdAt;}
}
