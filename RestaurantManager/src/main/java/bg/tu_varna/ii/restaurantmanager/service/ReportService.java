package bg.tu_varna.ii.restaurantmanager.service;
import bg.tu_varna.ii.restaurantmanager.exception.BusinessRuleException;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.CurrentOrderReportDto;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.TableOccupancyReportDto;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantOrder;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantTable;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderStatus;
import bg.tu_varna.ii.restaurantmanager.repository.ReportRepository;
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

}
