package bg.tu_varna.ii.restaurantmanager.model.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String customerName;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private Integer guestCount;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private RestaurantTable table;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Reservation(){}

    public Reservation(
            String customerName,
            String phoneNumber,
            Integer guestCount,
            LocalDateTime startTime,
            LocalDateTime endTime,
            RestaurantTable table) {

        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.guestCount = guestCount;
        this.startTime = startTime;
        this.endTime = endTime;
        this.table = table;
        this.status = ReservationStatus.PENDING;
    }

    public Long getId() {return id;}

    public String getCustomerName() {return customerName;}
    public void setCustomerName(String customerName) {this.customerName = customerName;}

    public ReservationStatus getStatus() {return status;}
    public void setStatus(ReservationStatus status) {this.status = status;}

    public LocalDateTime getCreatedAt() {return createdAt;}

    public RestaurantTable getTable() {return table;}
    public void setTable(RestaurantTable table) {this.table = table;}

    public LocalDateTime getEndTime() {return endTime;}
    public void setEndTime(LocalDateTime endTime) {this.endTime = endTime;}

    public LocalDateTime getStartTime() {return startTime;}
    public void setStartTime(LocalDateTime startTime) {this.startTime = startTime;}

    public Integer getGuestCount() {return guestCount;}
    public void setGuestCount(Integer guestCount) {this.guestCount = guestCount;}

    public String getPhoneNumber() {return phoneNumber;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
