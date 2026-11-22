package bg.tu_varna.ii.restaurantmanager.model.entity;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant_orders")
public class RestaurantOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private RestaurantTable table;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waiter_id", nullable = false)
    private User waiter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    protected RestaurantOrder() {}

    public RestaurantOrder(RestaurantTable table, User waiter) {
        this.table = table;
        this.waiter = waiter;
        this.status = OrderStatus.OPEN;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    public Long getId() {return id;}

    public RestaurantTable getTable() {return table;}
    public void setTable(RestaurantTable table) {this.table = table;}

    public User getWaiter() {return waiter;}
    public void setWaiter(User waiter) {this.waiter = waiter;}

    public OrderStatus getStatus() {return status;}
    public void setStatus(OrderStatus status) {this.status = status;}

    public LocalDateTime getCreatedAt() {return createdAt;}

    public List<OrderItem> getItems() {return items;}
}
