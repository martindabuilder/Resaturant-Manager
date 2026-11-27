package bg.tu_varna.ii.restaurantmanager.controller.page_controllers;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.CategoryResponseDto;
import bg.tu_varna.ii.restaurantmanager.service.CategoryService;
import bg.tu_varna.ii.restaurantmanager.service.DishService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DishPageController {
    private static final Logger logger = LogManager.getLogger(DishPageController.class);
    private final DishService dishService;
    private final CategoryService categoryService;

    public DishPageController(DishService dishService, CategoryService categoryService) {
        this.dishService = dishService;
        this.categoryService = categoryService;
    }

    @GetMapping("/dishes")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public String dishesPage(Authentication authentication, @RequestParam(required = false) String name, @RequestParam(required = false) Long categoryId, @RequestParam(required = false) Boolean available, Model model) {

        String role = authentication.getAuthorities().stream().findFirst().map(authority -> authority.getAuthority().replace("ROLE_", "")).orElse("UNKNOWN");

        var dishes = dishService.getAllDishes().stream()
                .filter(dish -> name == null || name.isBlank()
                        || dish.getName().toLowerCase()
                        .contains(name.toLowerCase()))
                .filter(dish -> categoryId == null
                        || dish.getCategoryId().equals(categoryId))
                .filter(dish -> available == null
                        || dish.isAvailable() == available)
                .toList();

        model.addAttribute("dishes", dishes);
        model.addAttribute("categories", categoryService.getAllCategories().stream().map(CategoryResponseDto::new).filter(CategoryResponseDto::isActive).toList());
        model.addAttribute("name", name);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("available", available);
        model.addAttribute("role", role);

        return "dishes";
    }

    @PostMapping("/dishes/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String createDish(@RequestParam String name, @RequestParam(required = false) String description, @RequestParam Integer weight, @RequestParam java.math.BigDecimal price, @RequestParam Long categoryId, @RequestParam Integer quantity) {
        dishService.createDish(name, description, weight, price, categoryId, quantity);
        logger.info("Създадено е ново ястие: {}", name);

        return "redirect:/dishes";
    }

    @PostMapping("/dishes/{id}/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String updateDish(@PathVariable Long id, @RequestParam String name, @RequestParam(required = false) String description, @RequestParam Integer weight, @RequestParam java.math.BigDecimal price, @RequestParam Long categoryId) {
        dishService.updateDish(id, name, description, weight, price, categoryId);

        return "redirect:/dishes";
    }

    @PostMapping("/dishes/{id}/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String updateDishActive(@PathVariable Long id, @RequestParam boolean active) {
        dishService.updateDishAvailability(id, active);

        return "redirect:/dishes";
    }

    @PostMapping("/dishes/{id}/availability")
    @PreAuthorize("hasRole('CHEF')")
    public String setDishAvailability(@PathVariable Long id, @RequestParam boolean available) {
        dishService.setDishAvailability(id, available);

        return "redirect:/dishes";
    }

    @PostMapping("/dishes/{id}/restock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String restockDish(@PathVariable Long id, @RequestParam Integer quantity) {
        dishService.restockDish(id, quantity);

        return "redirect:/dishes";
    }
}