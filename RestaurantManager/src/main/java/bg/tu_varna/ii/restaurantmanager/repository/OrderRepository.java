package bg.tu_varna.ii.restaurantmanager.repository;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository  extends JpaRepository<RestaurantOrder, Long> {
    List<RestaurantOrder> findByTable_Id(Long tableId);
    List<RestaurantOrder> findByWaiter_Id(long waiterId);
}
