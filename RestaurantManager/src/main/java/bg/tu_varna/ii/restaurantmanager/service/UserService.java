package bg.tu_varna.ii.restaurantmanager.service;

import bg.tu_varna.ii.restaurantmanager.entity.Role;
import bg.tu_varna.ii.restaurantmanager.entity.User;
import bg.tu_varna.ii.restaurantmanager.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) { this.userRepository = userRepository; }

    public User createUser(String username, String password, String firstName, String lastName, Role role) throws IllegalAccessException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Current user not found."));

        Role currentRole = currentUser.getRole();

        if (currentRole == Role.ADMIN) {
            if (role == Role.ADMIN) {
                throw new IllegalAccessException("ADMIN cannot create another ADMIN.");
            }
        }

        else if (currentRole == Role.MANAGER) {
            if (role != Role.WAITER && role != Role.CHEF) {
                throw new IllegalAccessException("MANAGER can only create WAITER and CHEF.");
            }
        }

        else {
            throw new IllegalAccessException("You do not have permission to create users.");
        }

        if (userRepository.existsByUsername(username)) {
            throw new IllegalAccessException("Username already exists.");
        }

        User user = new User(username, password, firstName, lastName, role);
        return userRepository.save(user);
    }
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        user.setEnabled(false);
        userRepository.save(user);
    }

    public void activateUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public List<User> getUsersByStatus(boolean enabled) {
        return userRepository.findByEnabled(enabled);
    }

    public List<User> searchUsersByName(String name) {
        return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }
}
