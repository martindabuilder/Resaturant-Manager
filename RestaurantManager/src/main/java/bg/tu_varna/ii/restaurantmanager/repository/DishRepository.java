package bg.tu_varna.ii.restaurantmanager.repository;

import bg.tu_varna.ii.restaurantmanager.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByCategory_Id(Long categoryId);
    List<Dish> findByAvailable(boolean available);
    List<Dish> findByNameContainingIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndCategory_Id(String name, Long categoryId);
}
