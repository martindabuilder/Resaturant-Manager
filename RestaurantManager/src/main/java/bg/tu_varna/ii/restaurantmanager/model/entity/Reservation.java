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

    @Column(nullable = false, length = 100)
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

    protected Reservation() {}

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
        this.status = ReservationStatus.CONFIRMED;
    }
}
