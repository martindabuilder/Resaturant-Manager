package bg.tu_varna.ii.restaurantmanager.controller;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.reports.NotificationResponseDto;
import bg.tu_varna.ii.restaurantmanager.model.entity.Notification;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.NotificationType;
import bg.tu_varna.ii.restaurantmanager.repository.UserRepository;
import bg.tu_varna.ii.restaurantmanager.service.NotificationService;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public NotificationController(NotificationService notificationService, UserRepository userRepository) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getMyNotifications(Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        List<NotificationResponseDto> notifications = notificationService.getUserNotifications(userId).stream().map(NotificationResponseDto::new).toList();

        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponseDto>> getMyUnreadNotifications(Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        List<NotificationResponseDto> notifications = notificationService.getUnreadNotifications(userId).stream().map(NotificationResponseDto::new).toList();

        return ResponseEntity.ok(notifications);
    }

    // Брой непрочетени известия
    @GetMapping("/unread/count")
    public ResponseEntity<Long> getMyUnreadNotificationCount(Authentication authentication) {
        Long userId = getCurrentUserId(authentication);

        return ResponseEntity.ok(notificationService.getUnreadNotificationCount(userId));
    }

    // Маркиране на известие като прочетено
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponseDto> markAsRead(@PathVariable Long notificationId, Authentication authentication) throws IllegalAccessException {
        Long userId = getCurrentUserId(authentication);
        Notification notification = notificationService.markAsRead(notificationId, userId);

        return ResponseEntity.ok(new NotificationResponseDto(notification));
    }

    private Long getCurrentUserId(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new RuntimeException("Authenticated user not found.")).getId();
    }
}