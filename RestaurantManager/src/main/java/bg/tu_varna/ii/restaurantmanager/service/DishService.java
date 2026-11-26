package bg.tu_varna.ii.restaurantmanager.service;

import bg.tu_varna.ii.restaurantmanager.exception.BusinessRuleException;
import bg.tu_varna.ii.restaurantmanager.exception.ResourceNotFoundException;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.DishResponseDto;
import bg.tu_varna.ii.restaurantmanager.model.entity.Category;
import bg.tu_varna.ii.restaurantmanager.model.entity.Dish;
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
    public DishResponseDto createDish(String name, String description, Integer weight, BigDecimal price, Long categoryId, Integer quantity) {

        if (name == null || name.isBlank()) {
            throw new BusinessRuleException("Dish name cannot be empty.");
        }

        String trimmedName = name.trim();

        if (weight == null || weight <= 0) {
            throw new BusinessRuleException("Weight must be bigger than 0.");
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("Price cannot be a negative amount.");
        }

        if (quantity == null || quantity < 0) {
            throw new BusinessRuleException("Quantity cannot be negative.");
        }

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category with the following ID was not found: " + categoryId));

        if (!category.isActive()) {
            throw new BusinessRuleException("Cannot add a dish to an inactive category.");
        }

        if (dishRepository.existsByNameIgnoreCaseAndCategory_Id(trimmedName, categoryId)) {
            throw new BusinessRuleException("Dish already exists in this category.");
        }

        Dish dish = new Dish(trimmedName, description, weight, price, category, quantity);
        Dish savedDish = dishRepository.save(dish);

        return new DishResponseDto(savedDish);
    }

    @Transactional(readOnly = true)
    public List<DishResponseDto> getAllDishes() {
        return dishRepository.findAll().stream().map(DishResponseDto::new).toList();
    }

    @Transactional(readOnly = true)
    public DishResponseDto getDishById(Long id) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dish not found with ID: " + id));
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
    public DishResponseDto updateDishAvailability(Long id, boolean active) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dish not found with ID: " + id));
        dish.setActive(active);
        dish.setAvailable(active && dish.getQuantity() > 0);
        Dish updatedDish = dishRepository.save(dish);

        return new DishResponseDto(updatedDish);
    }

    @Transactional
    public DishResponseDto restockDish(Long id, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new BusinessRuleException("Restock quantity must be greater than 0.");
        }

        Dish dish = dishRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dish not found with ID: " + id));

        int newQuantity;

        try {
            newQuantity = Math.addExact(dish.getQuantity(), quantity);
        } catch (ArithmeticException e) {
            throw new BusinessRuleException("Restock quantity is too large.");
        }

        dish.setQuantity(newQuantity);
        dish.setAvailable(dish.isActive() && newQuantity > 0);
        Dish updatedDish = dishRepository.save(dish);

        return new DishResponseDto(updatedDish);
    }



    @Transactional
    public DishResponseDto updateDish(Long id, String name, String description, Integer weight, BigDecimal price, Long categoryId) {

        Dish dish = dishRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dish not found with ID: " + id));

        if (name == null || name.isBlank()) {
            throw new BusinessRuleException("Dish name cannot be empty.");
        }

        String trimmedName = name.trim();

        if (weight == null || weight <= 0) {
            throw new BusinessRuleException("Weight must be bigger than 0.");
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("Price cannot be negative.");
        }

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        if (!category.isActive()) {
            throw new BusinessRuleException("Cannot assign a dish to an inactive category.");
        }

        boolean duplicate = dishRepository.existsByNameIgnoreCaseAndCategory_Id(trimmedName, categoryId);

        if (duplicate && (!dish.getName().equalsIgnoreCase(trimmedName) || !dish.getCategory().getId().equals(categoryId))) {
            throw new BusinessRuleException("Dish already exists in this category.");
        }

        dish.setName(trimmedName);
        dish.setDescription(description);
        dish.setWeight(weight);
        dish.setPrice(price);
        dish.setCategory(category);

        return new DishResponseDto(dishRepository.save(dish));
    }

    @Transactional
    public DishResponseDto setDishAvailability(Long id, boolean available) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dish not found with ID: " + id));

        if (available && !dish.isActive()) {
            throw new BusinessRuleException("An inactive dish cannot be made available.");
        }

        if (available && dish.getQuantity() <= 0) {
            throw new BusinessRuleException("A dish with zero quantity cannot be made available.");
        }

        dish.setAvailable(available);

        return new DishResponseDto(dishRepository.save(dish));
    }


}