package bg.tu_varna.ii.restaurantmanager.service;

import bg.tu_varna.ii.restaurantmanager.entity.Category;
import bg.tu_varna.ii.restaurantmanager.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name, String description) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be empty.");
        }

        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("Category already exists.");
        }

        Category category = new Category(name.trim(), description);
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));
    }

    public Category updateCategory(Long id, String name, String description) {
        Category category = getCategoryById(id);

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be empty.");
        }

        String newName = name.trim();

        if (!category.getName().equalsIgnoreCase(newName) && categoryRepository.existsByName(newName)) {
            throw new IllegalArgumentException("Category already exists.");
        }

        category.setName(newName);
        category.setDescription(description);
        return categoryRepository.save(category);
    }

    public Category activateCategory(Long id) {
        Category category = getCategoryById(id);
        category.setActive(true);
        return categoryRepository.save(category);
    }

    public Category deactivateCategory(Long id) {
        Category category = getCategoryById(id);
        category.setActive(false);
        return categoryRepository.save(category);
    }

    public List<Category> getCategoriesByStatus(boolean active) {
        return categoryRepository.findByActive(active);
    }

    public List<Category> searchCategories(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }
}
