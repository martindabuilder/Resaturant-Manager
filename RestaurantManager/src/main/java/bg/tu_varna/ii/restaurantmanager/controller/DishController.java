package bg.tu_varna.ii.restaurantmanager.controller;

import bg.tu_varna.ii.restaurantmanager.dto.DishResponseDto;
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
            @RequestParam Long categoryId) {

        return dishService.createDish(
                name, description, weight, price, categoryId
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public List<DishResponseDto> getAllDishes() {
        return dishService.getAllDishes();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public DishResponseDto getDishById(@PathVariable("id") Long id) {
        return dishService.getDishById(id);
    }

    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public List<DishResponseDto> getDishesByCategory(
            @PathVariable("categoryId") Long categoryId) {
        return dishService.getDishesByCategory(categoryId);
    }

    @GetMapping("/availability")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public List<DishResponseDto> getDishesByAvailability(
            @RequestParam boolean available) {
        return dishService.getDishesByAvailability(available);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public List<DishResponseDto> searchDishes(@RequestParam String name) {
        return dishService.searchDishes(name);
    }

    @PutMapping("/{id}/availability")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CHEF')")
    public DishResponseDto updateAvailability(
            @PathVariable("id") Long id,
            @RequestParam boolean available) {

        return dishService.updateDishAvailability(id, available);
    }
}