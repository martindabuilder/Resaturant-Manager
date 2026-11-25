package bg.tu_varna.ii.restaurantmanager.service;
import bg.tu_varna.ii.restaurantmanager.exception.BusinessRuleException;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.reports.*;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantOrder;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantTable;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderStatus;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderItemStatus;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.PaymentStatus;
import bg.tu_varna.ii.restaurantmanager.repository.ReportRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // get current orders within a selected period
    @Transactional(readOnly = true)
    public List<CurrentOrderReportDto> getCurrentOrders(LocalDateTime startDate, LocalDateTime endDate) {

        validatePeriod(startDate, endDate);

        List<OrderStatus> currentStatuses = List.of(OrderStatus.OPEN, OrderStatus.IN_PROGRESS, OrderStatus.READY);
        List<RestaurantOrder> orders = reportRepository.findCurrentOrders(currentStatuses, startDate, endDate);

        return orders.stream()
                .map(order -> new CurrentOrderReportDto(
                        order.getId(),
                        order.getTable().getTableNumber(),
                        order.getWaiter().getUsername(),
                        order.getStatus(),
                        order.getCreatedAt()
                ))
                .toList();
    }

    // get current occupancy of active tables
    @Transactional(readOnly = true)
    public List<TableOccupancyReportDto> getTableOccupancy() {

        List<RestaurantTable> tables = reportRepository.findActiveTables();

        return tables.stream()
                .map(table -> new TableOccupancyReportDto(
                        table.getId(),
                        table.getTableNumber(),
                        table.getCapacity(),
                        table.getStatus()
                ))
                .toList();
    }

    // validate selected date period
    private void validatePeriod(LocalDateTime startDate, LocalDateTime endDate) {

        if (startDate == null || endDate == null) {
            throw new BusinessRuleException("Start date and end date are required.");
        }

        if (startDate.isAfter(endDate)) {
            throw new BusinessRuleException("Start date must be before or equal to end date.");
        }
    }

    // get all sales by dish over a certain period of time
    @Transactional(readOnly = true)
    public List<DishSalesReportDto> getSalesByDish(LocalDateTime startDate, LocalDateTime endDate) {validatePeriod(startDate, endDate);
        return reportRepository.getSalesByDish(OrderStatus.COMPLETED, getIncludedItemStatuses(), startDate, endDate);
    }

    // get all sales by category over a certain period of time
    @Transactional(readOnly = true)
    public List<CategorySalesReportDto> getSalesByCategory(LocalDateTime startDate, LocalDateTime endDate) {
        validatePeriod(startDate, endDate);
        return reportRepository.getSalesByCategory(OrderStatus.COMPLETED, getIncludedItemStatuses(), startDate, endDate);
    }

    // get all sales by a specific waiter over a certain period of time
    @Transactional(readOnly = true)
    public List<WaiterSalesReportDto> getSalesByWaiter(LocalDateTime startDate, LocalDateTime endDate) {
        validatePeriod(startDate, endDate);
        return reportRepository.getSalesByWaiter(OrderStatus.COMPLETED, getIncludedItemStatuses(), startDate, endDate);
    }

    // exclude canceled order items from sales reports
    private List<OrderItemStatus> getIncludedItemStatuses() {
        return List.of(
                OrderItemStatus.NEW,
                OrderItemStatus.ACCEPTED,
                OrderItemStatus.PREPARING,
                OrderItemStatus.READY,
                OrderItemStatus.SERVED
        );
    }

    // get all payments with a certain payment method over a certain period of time
    @Transactional(readOnly = true)
    public List<PaymentMethodReportDto> getRevenueByPaymentMethod(LocalDateTime startDate, LocalDateTime endDate) {
        validatePeriod(startDate, endDate);
        return reportRepository.getRevenueByPaymentMethod(PaymentStatus.PAID, startDate, endDate);
    }

    // get all applied discounts over a certain period of time
    @Transactional(readOnly = true)
    public List<AppliedDiscountsReportDto> getAppliedDiscounts(LocalDateTime startDate, LocalDateTime endDate) {
        validatePeriod(startDate, endDate);
        return reportRepository.getAppliedDiscounts(startDate, endDate);
    }

    // get revenue by waiter shift
    @Transactional(readOnly = true)
    public List<ShiftRevenueReportDto> getRevenueByShift(LocalDateTime startDate, LocalDateTime endDate) {
        validatePeriod(startDate, endDate);
        return reportRepository.getRevenueByShift(PaymentStatus.PAID, startDate, endDate);
    }
}
