package bg.tu_varna.ii.restaurantmanager.repository;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantOrder;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<RestaurantOrder, Long> {
    @EntityGraph(attributePaths = {
            "table",
            "waiter",
            "items",
            "items.dish"
    })
    Optional<RestaurantOrder> findWithDetailsById(Long id);

    @EntityGraph(attributePaths = {
            "table",
            "waiter",
            "items",
            "items.dish"
    })
    List<RestaurantOrder> findAllWithDetailsBy();

    @EntityGraph(attributePaths = {
            "table",
            "waiter",
            "items",
            "items.dish"
    })
    List<RestaurantOrder> findByTable_Id(Long tableId);

    @EntityGraph(attributePaths = {
            "table",
            "waiter",
            "items",
            "items.dish"
    })
    List<RestaurantOrder> findByWaiter_Id(Long waiterId);

    boolean existsByTable_IdAndStatusIn(Long tableId, List<OrderStatus> statuses);
}
