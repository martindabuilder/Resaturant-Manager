package bg.tu_varna.ii.restaurantmanager.model.dto.response.reports;
import bg.tu_varna.ii.restaurantmanager.model.entity.Notification;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.NotificationType;
import java.time.LocalDateTime;

public class NotificationResponseDto {
    private final Long id;
    private final String message;
    private final NotificationType type;
    private final boolean read;
    private final LocalDateTime createdAt;

    public NotificationResponseDto(Notification notification) {
        this.id = notification.getId();
        this.message = notification.getMessage();
        this.type = notification.getType();
        this.read = notification.isRead();
        this.createdAt = notification.getCreatedAt();
    }

    public Long getId() {return id;}

    public String getMessage() {return message;}

    public NotificationType getType() {return type;}

    public boolean isRead() {return read;}

    public LocalDateTime getCreatedAt() {return createdAt;}
}
