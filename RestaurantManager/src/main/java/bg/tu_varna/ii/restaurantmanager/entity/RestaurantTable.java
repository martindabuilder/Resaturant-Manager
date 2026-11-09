package bg.tu_varna.ii.restaurantmanager.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "restaurant_tables")
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer tableNumber;

    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private TableStatus status;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected RestaurantTable() {}

    @PrePersist
    protected void onCreate() {createdAt = LocalDateTime.now();}

    public Long getId() {return id;}

    public Integer getTableNumber() {return tableNumber;}

    public void setTableNumber(Integer tableNumber) {this.tableNumber = tableNumber;}

    public Integer getCapacity() {return capacity;}

    public void setCapacity(Integer capacity) {this.capacity = capacity;}

    public TableStatus getStatus() {return status;}

    public void setStatus(TableStatus status) {this.status = status;}

    public boolean isActive() {return active;}

    public void setActive(boolean active) {this.active = active;}

    public LocalDateTime getCreatedAt() {return createdAt;}
}
