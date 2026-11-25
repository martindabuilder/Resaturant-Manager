package bg.tu_varna.ii.restaurantmanager.service;
import bg.tu_varna.ii.restaurantmanager.exception.ResourceNotFoundException;
import bg.tu_varna.ii.restaurantmanager.model.entity.Notification;
import bg.tu_varna.ii.restaurantmanager.model.entity.User;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.NotificationType;
import bg.tu_varna.ii.restaurantmanager.repository.NotificationRepository;
import bg.tu_varna.ii.restaurantmanager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Notification createNotification(Long recipientId, String message, NotificationType type) {
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + recipientId));

        Notification notification = new Notification(message, type, recipient);
        return notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(Long recipientId) {
        if (!userRepository.existsById(recipientId)) {
            throw new ResourceNotFoundException("User not found with id: " + recipientId);
        }

        return notificationRepository
                .findByRecipient_IdOrderByCreatedAtDesc(recipientId);
    }

    // Само непрочетените известия
    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(Long recipientId) {
        if (!userRepository.existsById(recipientId)) {
            throw new ResourceNotFoundException("User not found with id: " + recipientId);
        }

        return notificationRepository.findByRecipient_IdAndReadFalseOrderByCreatedAtDesc(recipientId);
    }

    @Transactional(readOnly = true)
    public long getUnreadNotificationCount(Long recipientId) {
        if (!userRepository.existsById(recipientId)) {
            throw new ResourceNotFoundException("User not found with id: " + recipientId);
        }

        return notificationRepository.countByRecipient_IdAndReadFalse(recipientId);
    }

    @Transactional
    public Notification markAsRead(Long notificationId, Long recipientId) throws IllegalAccessException {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));

        if (!notification.getRecipient().getId().equals(recipientId)) {
            throw new IllegalAccessException("You cannot access this notification.");
        }

        notification.setRead(true);

        return notificationRepository.save(notification);
    }
}
