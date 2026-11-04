package bg.tu_varna.ii.restaurantmanager.dto;

import bg.tu_varna.ii.restaurantmanager.entity.Role;
import bg.tu_varna.ii.restaurantmanager.entity.User;
import java.time.LocalDateTime;

public class UserResponseDto {
    private final Long id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final Role role;
    private final boolean enabled;
    private final LocalDateTime createdAt;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.role = user.getRole();
        this.enabled = user.isEnabled();
        this.createdAt = user.getCreatedAt();
    }

    public Long getId() {
        return id;
    }
    public String getUsername() { return username; }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public Role getRole() {
        return role;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}