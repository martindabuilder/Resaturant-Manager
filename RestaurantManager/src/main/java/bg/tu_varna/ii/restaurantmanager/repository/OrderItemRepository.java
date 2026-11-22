package bg.tu_varna.ii.restaurantmanager.repository;
import bg.tu_varna.ii.restaurantmanager.model.entity.OrderItem;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderItemStatus;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @EntityGraph(attributePaths = {"dish", "order", "order.table", "order.waiter"})
    Optional<OrderItem> findWithDetailsById(Long id);

    @EntityGraph(attributePaths = {"dish", "order", "order.table", "order.waiter"})
    List<OrderItem> findByOrder_Id(Long orderId);

    List<OrderItem> findByStatus(OrderItemStatus status);
}