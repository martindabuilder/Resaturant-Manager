package bg.tu_varna.ii.restaurantmanager.controller;

import bg.tu_varna.ii.restaurantmanager.model.dto.response.DishResponseDto;
import bg.tu_varna.ii.restaurantmanager.service.DishService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/restaurant/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public DishResponseDto createDish(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam Integer weight,
            @RequestParam BigDecimal price,
            @RequestParam Long categoryId,
            @RequestParam Integer quantity) {

        return dishService.createDish(name, description, weight, price, categoryId, quantity);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public List<DishResponseDto> getAllDishes() {
        return dishService.getAllDishes();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public DishResponseDto getDishById(@PathVariable Long id) {
        return dishService.getDishById(id);
    }

    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public List<DishResponseDto> getDishesByCategory(@PathVariable Long categoryId) {
        return dishService.getDishesByCategory(categoryId);
    }

    @GetMapping("/availability")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public List<DishResponseDto> getDishesByAvailability(@RequestParam boolean available) {
        return dishService.getDishesByAvailability(available);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public List<DishResponseDto> searchDishes(@RequestParam String name) {
        return dishService.searchDishes(name);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public DishResponseDto updateDish(@PathVariable Long id, @RequestParam String name, @RequestParam(required = false) String description, @RequestParam Integer weight, @RequestParam BigDecimal price, @RequestParam Long categoryId) {
        return dishService.updateDish(id, name, description, weight, price, categoryId);
    }

    @PutMapping("/{id}/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public DishResponseDto updateDishActive(@PathVariable Long id, @RequestParam boolean active) {
        return dishService.updateDishAvailability(id, active);
    }

    @PatchMapping("/{id}/availability")
    @PreAuthorize("hasRole('CHEF')")
    public DishResponseDto setDishAvailability(@PathVariable Long id, @RequestParam boolean available) {

        return dishService.setDishAvailability(id, available);
    }

    @PatchMapping("/{id}/restock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public DishResponseDto restockDish(@PathVariable Long id, @RequestParam Integer quantity) {

        return dishService.restockDish(id, quantity);
    }
}
