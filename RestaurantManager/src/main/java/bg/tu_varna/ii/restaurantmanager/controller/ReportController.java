package bg.tu_varna.ii.restaurantmanager.controller;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.reports.*;
import bg.tu_varna.ii.restaurantmanager.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/restaurant/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // get current orders within a selected period
    @GetMapping("/current-orders")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<CurrentOrderReportDto> getCurrentOrders(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return reportService.getCurrentOrders(startDate, endDate);
    }

    // get current occupancy of active tables
    @GetMapping("/table-occupancy")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<TableOccupancyReportDto> getTableOccupancy() {
        return reportService.getTableOccupancy();
    }

    // get sales by dish
    @GetMapping("/sales-by-dish")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<DishSalesReportDto> getSalesByDish(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        return reportService.getSalesByDish(startDate, endDate);
    }

    // get sales by category
    @GetMapping("/sales-by-category")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<CategorySalesReportDto> getSalesByCategory(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return reportService.getSalesByCategory(startDate, endDate);
    }

    // get sales by waiter
    @GetMapping("/sales-by-waiter")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<WaiterSalesReportDto> getSalesByWaiter(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return reportService.getSalesByWaiter(startDate, endDate);
    }

    // get revenue by payment method
    @GetMapping("/revenue-by-payment-method")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<PaymentMethodReportDto> getRevenueByPaymentMethod(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return reportService.getRevenueByPaymentMethod(startDate, endDate);
    }

    // applied discounts
    @GetMapping("/applied-discounts")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<AppliedDiscountsReportDto> getAppliedDiscounts(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return reportService.getAppliedDiscounts(startDate, endDate);
    }

    // get revenue by waiter shift
    @GetMapping("/revenue-by-shift")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<ShiftRevenueReportDto> getRevenueByShift(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return reportService.getRevenueByShift(startDate, endDate);
    }
}
