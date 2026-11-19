package bg.tu_varna.ii.restaurantmanager.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shifts")
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waiter_id", nullable = false)
    private User waiter;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Shift() {
    }

    public Shift (LocalDateTime startTime, LocalDateTime endTime, User waiter) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.waiter = waiter;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {return id;}

    public LocalDateTime getStartTime() {return startTime;}

    public void setStartTime(LocalDateTime startTime) {this.startTime = startTime;}

    public LocalDateTime getEndTime() {return endTime;}

    public void setEndTime(LocalDateTime endTime) {this.endTime = endTime;}

    public User getWaiter() {return waiter;}
    public void setWaiter(User waiter) {this.waiter = waiter;}

    public LocalDateTime getCreatedAt() {return createdAt;}

}