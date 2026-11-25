package bg.tu_varna.ii.restaurantmanager.repository;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.reports.*;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantOrder;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantTable;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderStatus;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderItemStatus;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<RestaurantOrder, Long> {

    // current orders within a selected period
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

    // active restaurant tables
    @Query("""
            SELECT t
            FROM RestaurantTable t
            WHERE t.active = true
            ORDER BY t.tableNumber ASC
            """)
    List<RestaurantTable> findActiveTables();

    // sales by dish
    @Query("""
            SELECT new bg.tu_varna.ii.restaurantmanager.model.dto.response.reports.DishSalesReportDto(
                d.id,
                d.name,
                SUM(oi.quantity),
                SUM(oi.unitPrice * oi.quantity)
            )
            FROM OrderItem oi
            JOIN oi.dish d
            JOIN oi.order o
            WHERE o.status = :completedStatus
            AND oi.status IN :itemStatuses
            AND o.createdAt >= :startDate
            AND o.createdAt <= :endDate
            GROUP BY d.id, d.name
            ORDER BY d.name
            """)
    List<DishSalesReportDto> getSalesByDish(
            @Param("completedStatus") OrderStatus completedStatus,
            @Param("itemStatuses") List<OrderItemStatus> itemStatuses,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // sales by category
    @Query("""
            SELECT new bg.tu_varna.ii.restaurantmanager.model.dto.response.reports.CategorySalesReportDto(
                c.id,
                c.name,
                SUM(oi.quantity),
                SUM(oi.unitPrice * oi.quantity)
            )
            FROM OrderItem oi
            JOIN oi.dish d
            JOIN d.category c
            JOIN oi.order o
            WHERE o.status = :completedStatus
            AND oi.status IN :itemStatuses
            AND o.createdAt >= :startDate
            AND o.createdAt <= :endDate
            GROUP BY c.id, c.name
            ORDER BY c.name
            """)
    List<CategorySalesReportDto> getSalesByCategory(
            @Param("completedStatus") OrderStatus completedStatus,
            @Param("itemStatuses") List<OrderItemStatus> itemStatuses,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // sales by waiter
    @Query("""
            SELECT new bg.tu_varna.ii.restaurantmanager.model.dto.response.reports.WaiterSalesReportDto(
                u.id,
                CONCAT(u.firstName, ' ', u.lastName),
                COUNT(DISTINCT o.id),
                SUM(oi.quantity),
                SUM(oi.unitPrice * oi.quantity)
            )
            FROM OrderItem oi
            JOIN oi.order o
            JOIN o.waiter u
            WHERE o.status = :completedStatus
            AND oi.status IN :itemStatuses
            AND o.createdAt >= :startDate
            AND o.createdAt <= :endDate
            GROUP BY u.id, u.firstName, u.lastName
            ORDER BY u.lastName, u.firstName
            """)
    List<WaiterSalesReportDto> getSalesByWaiter(
            @Param("completedStatus") OrderStatus completedStatus,
            @Param("itemStatuses") List<OrderItemStatus> itemStatuses,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // revenue by waiter shift
    @Query("""
        SELECT new bg.tu_varna.ii.restaurantmanager.model.dto.response.reports.ShiftRevenueReportDto(
            s.id,
            s.waiter.id,
            CONCAT(s.waiter.firstName, ' ', s.waiter.lastName),
            s.startTime,
            s.endTime,
            COUNT(DISTINCT o.id),
            SUM(p.amount)
        )
        FROM Payment p
        JOIN p.bill b
        JOIN b.order o,
             Shift s
        WHERE o.waiter.id = s.waiter.id
        AND p.status = :paidStatus
        AND p.paidAt >= s.startTime
        AND p.paidAt < s.endTime
        AND p.paidAt >= :startDate
        AND p.paidAt <= :endDate
        GROUP BY s.id, s.waiter.id,
                 s.waiter.firstName, s.waiter.lastName,
                 s.startTime, s.endTime
        ORDER BY s.startTime
        """)
    List<ShiftRevenueReportDto> getRevenueByShift(
            @Param("paidStatus") PaymentStatus paidStatus,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // revenue by the payment method
    @Query("""
        SELECT new bg.tu_varna.ii.restaurantmanager.model.dto.response.reports.PaymentMethodReportDto(
            CAST(p.method AS string),
            COUNT(p.id),
            SUM(p.amount)
        )
        FROM Payment p
        WHERE p.status = :paidStatus
        AND p.paidAt >= :startDate
        AND p.paidAt <= :endDate
        GROUP BY p.method
        ORDER BY p.method
        """)
    List<PaymentMethodReportDto> getRevenueByPaymentMethod(
            @Param("paidStatus") PaymentStatus paidStatus,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // applied discounts
    @Query("""
        SELECT new bg.tu_varna.ii.restaurantmanager.model.dto.response.reports.AppliedDiscountsReportDto(
            b.discountId,
            b.discountName,
            COUNT(b.id),
            SUM(b.discountAmount)
        )
        FROM Bill b
        WHERE b.discountId IS NOT NULL
        AND b.createdAt >= :startDate
        AND b.createdAt <= :endDate
        GROUP BY b.discountId, b.discountName
        ORDER BY b.discountName
        """)
    List<AppliedDiscountsReportDto> getAppliedDiscounts(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}