package bg.tu_varna.ii.restaurantmanager.controller;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.reports.CurrentOrderReportDto;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.reports.TableOccupancyReportDto;
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
}
