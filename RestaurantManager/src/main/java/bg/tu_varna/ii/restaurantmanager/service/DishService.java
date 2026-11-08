package bg.tu_varna.ii.restaurantmanager.service;

import bg.tu_varna.ii.restaurantmanager.dto.DishResponseDto;
import bg.tu_varna.ii.restaurantmanager.entity.Category;
import bg.tu_varna.ii.restaurantmanager.entity.Dish;
import bg.tu_varna.ii.restaurantmanager.repository.CategoryRepository;
import bg.tu_varna.ii.restaurantmanager.repository.DishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DishService {

    private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;

    public DishService(DishRepository dishRepository, CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.dishRepository = dishRepository;
    }

    @Transactional
    public DishResponseDto createDish(String name, String description, Integer weight, BigDecimal price, Long categoryId) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Dish name cannot be empty.");
        }

        String trimmedName = name.trim();

        if (weight == null || weight <= 0) {
            throw new IllegalArgumentException("Weight must be bigger than 0.");
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be a negative amount.");
        }

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("Category with the following ID was not found: " + categoryId));

        if (!category.isActive()) {
            throw new IllegalArgumentException("Cannot add a dish to an inactive category.");
        }

        if (dishRepository.existsByNameIgnoreCaseAndCategory_Id(trimmedName, categoryId)) {
            throw new IllegalArgumentException("Dish already exists in this category.");
        }

        Dish dish = new Dish(trimmedName, description, weight, price, category);
        Dish savedDish = dishRepository.save(dish);

        return new DishResponseDto(savedDish);
    }

    @Transactional(readOnly = true)
    public List<DishResponseDto> getAllDishes() {
        return dishRepository.findAll().stream().map(DishResponseDto::new).toList();
    }

    @Transactional(readOnly = true)
    public DishResponseDto getDishById(Long id) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Dish not found with ID: " + id));
        return new DishResponseDto(dish);
    }

    @Transactional(readOnly = true)
    public List<DishResponseDto> getDishesByCategory(Long categoryId) {
        return dishRepository.findByCategory_Id(categoryId).stream().map(DishResponseDto::new).toList();
    }

    @Transactional(readOnly = true)
    public List<DishResponseDto> getDishesByAvailability(boolean available) {
        return dishRepository.findByAvailable(available).stream().map(DishResponseDto::new).toList();
    }

    @Transactional(readOnly = true)
    public List<DishResponseDto> searchDishes(String name) {
        return dishRepository.findByNameContainingIgnoreCase(name).stream().map(DishResponseDto::new).toList();
    }

    @Transactional
    public DishResponseDto updateDishAvailability(Long id, boolean available) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Dish not found with ID: " + id));
        dish.setAvailable(available);
        Dish updatedDish = dishRepository.save(dish);

        return new DishResponseDto(updatedDish);
    }
}