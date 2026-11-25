package bg.tu_varna.ii.restaurantmanager.model.entity;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.NotificationType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    @Column(nullable = false)
    private boolean read = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    protected Notification() {}

    public Notification(String message, NotificationType type, User recipient) {
        this.message = message;
        this.type = type;
        this.recipient = recipient;
        this.read = false;
    }

    @PrePersist
    protected void onCreate() {createdAt = LocalDateTime.now();}

    public Long getId() {return id;}

    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}

    public NotificationType getType() {return type;}
    public void setType(NotificationType type) {this.type = type;}

    public boolean isRead() {return read;}
    public void setRead(boolean read) {this.read = read;}

    public LocalDateTime getCreatedAt() {return createdAt;}

    public User getRecipient() {return recipient;}
    public void setRecipient(User recipient) {this.recipient = recipient;}
}
