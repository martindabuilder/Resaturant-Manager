package bg.tu_varna.ii.restaurantmanager.repository;
import bg.tu_varna.ii.restaurantmanager.model.entity.Notification;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipient_IdOrderByCreatedAtDesc(Long recipientId);
    List<Notification> findByRecipient_IdAndReadFalseOrderByCreatedAtDesc(Long recipientId);
    long countByRecipient_IdAndReadFalse(Long recipientId);
}
