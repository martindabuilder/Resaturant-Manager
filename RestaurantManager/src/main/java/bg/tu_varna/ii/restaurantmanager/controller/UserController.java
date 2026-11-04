package bg.tu_varna.ii.restaurantmanager.controller;

import bg.tu_varna.ii.restaurantmanager.entity.Role;
import bg.tu_varna.ii.restaurantmanager.entity.User;
import bg.tu_varna.ii.restaurantmanager.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import bg.tu_varna.ii.restaurantmanager.dto.UserResponseDto;

import java.util.List;

@RestController
@RequestMapping("/restaurant/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public UserResponseDto createUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam Role role
    ) throws IllegalAccessException {
        User user = userService.createUser(username, password, firstName, lastName, role);
        return new UserResponseDto(user);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers().stream().map(UserResponseDto::new).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return new UserResponseDto(userService.getUserById(id));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void activateUser(@PathVariable Long id) {
        userService.activateUser(id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<UserResponseDto> searchUsers(@RequestParam String name) {
        return userService.searchUsersByName(name).stream().map(UserResponseDto::new).toList();
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<UserResponseDto> getUsersByRole(@PathVariable Role role) {
        return userService.getUsersByRole(role).stream().map(UserResponseDto::new).toList();
    }

    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<UserResponseDto> getUsersByStatus(@RequestParam boolean enabled) {
        return userService.getUsersByStatus(enabled).stream().map(UserResponseDto::new).toList();
    }
}