package bg.tu_varna.ii.restaurantmanager.model.entity;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderItemStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private RestaurantOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderItemStatus status;

    protected OrderItem() {
    }

    public OrderItem(Dish dish, Integer quantity, BigDecimal unitPrice) {
        this.dish = dish;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.status = OrderItemStatus.NEW;
    }

    public Long getId() {
        return id;
    }

    public RestaurantOrder getOrder() {return order;}
    public void setOrder(RestaurantOrder order) {this.order = order;}

    public Dish getDish() {return dish;}
    public void setDish(Dish dish) {this.dish = dish;}

    public Integer getQuantity() {return quantity;}
    public void setQuantity(Integer quantity) {this.quantity = quantity;}

    public BigDecimal getUnitPrice() {return unitPrice;}
    public void setUnitPrice(BigDecimal unitPrice) {this.unitPrice = unitPrice;}

    public OrderItemStatus getStatus() {return status;}
    public void setStatus(OrderItemStatus status) {this.status = status;}
}
