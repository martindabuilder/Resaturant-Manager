package bg.tu_varna.ii.restaurantmanager.repository;

import bg.tu_varna.ii.restaurantmanager.entity.RestaurantTable;
import bg.tu_varna.ii.restaurantmanager.entity.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TableRepository extends JpaRepository<RestaurantTable, Long> {

    Optional<RestaurantTable> findByTableNumber(Integer TableNumber);

    boolean existsByTableNumber(Integer tableNumber);

    List<RestaurantTable> findByStatus(TableStatus status);
    List<RestaurantTable> findBYActive(boolean active);
    List<RestaurantTable> findByCapacity(Integer capacity);
}
