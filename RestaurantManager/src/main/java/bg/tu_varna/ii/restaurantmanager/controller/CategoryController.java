package bg.tu_varna.ii.restaurantmanager.controller;

import bg.tu_varna.ii.restaurantmanager.dto.CategoryResponseDto;
import bg.tu_varna.ii.restaurantmanager.entity.Category;
import bg.tu_varna.ii.restaurantmanager.service.CategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public CategoryResponseDto createCategory(
            @RequestParam String name,
            @RequestParam(required = false) String description) {

        Category category = categoryService.createCategory(name, description);
        return new CategoryResponseDto(category);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public List<CategoryResponseDto> getAllCategories() {
        return categoryService.getAllCategories().stream().map(CategoryResponseDto::new).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public CategoryResponseDto getCategoryById(@PathVariable Long id) {
        return new CategoryResponseDto(categoryService.getCategoryById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public CategoryResponseDto updateCategory(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam(required = false) String description) {

        Category category = categoryService.updateCategory(id, name, description);
        return new CategoryResponseDto(category);
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public CategoryResponseDto activateCategory(@PathVariable Long id) {
        return new CategoryResponseDto(categoryService.activateCategory(id));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public CategoryResponseDto deactivateCategory(@PathVariable Long id) {
        return new CategoryResponseDto(categoryService.deactivateCategory(id));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public List<CategoryResponseDto> searchCategories(
            @RequestParam String name) {
        return categoryService.searchCategories(name).stream().map(CategoryResponseDto::new).toList();
    }

    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<CategoryResponseDto> getCategoriesByStatus(
            @RequestParam boolean active) {

        return categoryService.getCategoriesByStatus(active).stream().map(CategoryResponseDto::new).toList();
    }
}
