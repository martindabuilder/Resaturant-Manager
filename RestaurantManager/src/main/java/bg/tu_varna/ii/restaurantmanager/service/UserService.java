package bg.tu_varna.ii.restaurantmanager.service;

import bg.tu_varna.ii.restaurantmanager.entity.Role;
import bg.tu_varna.ii.restaurantmanager.entity.User;
import bg.tu_varna.ii.restaurantmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) { this.userRepository = userRepository; }

    public User createUser(String username, String password, String firstName, String lastName, Role role) throws IllegalAccessException {
        if (userRepository.existsByUsername(username)) { throw new IllegalAccessException("Username already exists.");}

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
}
