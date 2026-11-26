package bg.tu_varna.ii.restaurantmanager.repository;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantTable;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TableRepository extends JpaRepository<RestaurantTable, Long> {
    Optional<RestaurantTable> findByTableNumber(Integer tableNumber);
    boolean existsByTableNumber(Integer tableNumber);

    List<RestaurantTable> findByStatus(TableStatus status);
    List<RestaurantTable> findByActive(boolean active);
    List<RestaurantTable> findByCapacityGreaterThanEqual(Integer capacity);

    List<RestaurantTable> findByActiveTrueAndStatusOrderByTableNumberAsc(TableStatus status);
}
