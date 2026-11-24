package bg.tu_varna.ii.restaurantmanager.repository;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantOrder;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantTable;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<RestaurantOrder, Long > {

    // Current orders within a selected period
    @Query("""
            SELECT o
            FROM RestaurantOrder o
            WHERE o.status IN :statuses
            AND o.createdAt >= :startDate
            AND o.createdAt <= :endDate
            ORDER BY o.createdAt DESC
            """)
    List<RestaurantOrder> findCurrentOrders(
            @Param("statuses") List<OrderStatus> statuses,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // Active restaurant tables
    @Query("""
            SELECT t
            FROM RestaurantTable t
            WHERE t.active = true
            ORDER BY t.tableNumber ASC
            """)
    List<RestaurantTable> findActiveTables();

}
