package bg.tu_varna.ii.restaurantmanager.repository;
import bg.tu_varna.ii.restaurantmanager.model.entity.Bill;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByOrder_Id(Long orderId);
    boolean existsByOrder_Id(Long odrerId);

    List<Bill> order(RestaurantOrder order);
}
