package bg.tu_varna.ii.restaurantmanager.controller.page_controllers;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.UserResponseDto;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.Role;
import bg.tu_varna.ii.restaurantmanager.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class UserPageController {

    private final UserService userService;

    public UserPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String usersPage(Authentication authentication, @RequestParam(required = false) String name, @RequestParam(required = false) Role roleFilter, @RequestParam(required = false) Boolean enabledFilter, Model model) {

        String currentRole = authentication.getAuthorities().stream()
                .findFirst()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .orElse("UNKNOWN");

        var users = userService.getAllUsers().stream()
                .map(UserResponseDto::new)
                .filter(user -> name == null || name.isBlank()
                        || (user.getFirstName() != null
                        && user.getFirstName().toLowerCase().contains(name.toLowerCase()))
                        || (user.getLastName() != null
                        && user.getLastName().toLowerCase().contains(name.toLowerCase())))
                .filter(user -> roleFilter == null || user.getRole() == roleFilter)
                .filter(user -> enabledFilter == null || user.isEnabled() == enabledFilter)
                .toList();

        model.addAttribute("users", users);
        model.addAttribute("username", authentication.getName());
        model.addAttribute("currentRole", currentRole);
        model.addAttribute("roles", Role.values());
        model.addAttribute("name", name);
        model.addAttribute("roleFilter", roleFilter);
        model.addAttribute("enabledFilter", enabledFilter);

        return "users";
    }

    @PostMapping("/users/create")
    public String createUser(@RequestParam String username, @RequestParam String password, @RequestParam String firstName, @RequestParam String lastName, @RequestParam Role role, RedirectAttributes redirectAttributes) throws IllegalAccessException {

        userService.createUser(username, password, firstName, lastName, role);

        redirectAttributes.addFlashAttribute("successMessage", "Служителят беше създаден успешно.");

        return "redirect:/users";
    }

    @PostMapping("/users/{id}/deactivate")
    public String deactivateUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deactivateUser(id);

        redirectAttributes.addFlashAttribute("successMessage", "Служителят беше деактивиран.");

        return "redirect:/users";
    }

    @PostMapping("/users/{id}/activate")
    public String activateUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        userService.activateUser(id);

        redirectAttributes.addFlashAttribute("successMessage", "Служителят беше активиран.");

        return "redirect:/users";
    }
}