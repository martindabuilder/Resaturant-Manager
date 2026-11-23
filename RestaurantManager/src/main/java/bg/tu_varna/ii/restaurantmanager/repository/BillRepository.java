package bg.tu_varna.ii.restaurantmanager.repository;
import bg.tu_varna.ii.restaurantmanager.model.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByOrder_Id(Long orderId);
    boolean existsByOrder_Id(Long orderId);
}
