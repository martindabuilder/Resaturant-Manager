package bg.tu_varna.ii.restaurantmanager.repository;
import bg.tu_varna.ii.restaurantmanager.model.entity.OrderItem;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder_Id(Long orderId);
    List<OrderItem> findByStatus(OrderStatus status);
}
