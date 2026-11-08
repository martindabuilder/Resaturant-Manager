package bg.tu_varna.ii.restaurantmanager.service;

import bg.tu_varna.ii.restaurantmanager.entity.Category;
import bg.tu_varna.ii.restaurantmanager.entity.Dish;
import bg.tu_varna.ii.restaurantmanager.repository.CategoryRepository;
import bg.tu_varna.ii.restaurantmanager.repository.DishRepository;

import java.math.BigDecimal;
import java.util.List;

public class DishService {

    private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;

    public DishService(DishRepository dishRepository, CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.dishRepository = dishRepository;
    }

    public Dish createDish(String name, String description, Integer weight, BigDecimal price, Long categoryId) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Dish name cannot be empty.");
        }

        String trimmedName = name.trim();

        if (weight == null || weight <= 0) {
            throw new IllegalArgumentException("Weight must be bigger than 0.");
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be a negative number.");
        }

        Category category = categoryRepository.findById(categoryId).
                orElseThrow(() -> new IllegalArgumentException(
                        "Category with the following ID not found: " + categoryId
                ));

        if(!category.isActive()) {
            throw new IllegalArgumentException("Cannot add a dish to an inactive category.");
        }

        if (dishRepository.existsByNameIgnoreCaseAndCategory_Id(trimmedName, categoryId)) {
            throw new IllegalArgumentException("Dish already exists in this category.");
        }

        Dish dish = new Dish(trimmedName, description, weight, price, category);

        return dishRepository.save(dish);
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Dish getDishById(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dish with the following ID is not found: " + id));
    }

    public List<Dish> getDishesByCategory(Long categoryId) {
        return dishRepository.findByCategory_Id(categoryId);
    }

    public List<Dish> getDishesByAvailability(boolean available) {
        return dishRepository.findByAvailable(available);
    }

    public List<Dish> searchDishes(String name) {
        return dishRepository.findByNameContainingIgnoreCase(name);
    }
}
