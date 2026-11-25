package bg.tu_varna.ii.restaurantmanager.model.dto.response.reports;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ShiftRevenueReportDto {
    private final Long shiftId;
    private final Long waiterId;
    private final String waiterName;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Long totalOrders;
    private final BigDecimal totalRevenue;

    public ShiftRevenueReportDto(Long shiftId, Long waiterId, String waiterName, LocalDateTime startTime, LocalDateTime endTime, Long totalOrders, BigDecimal totalRevenue) {
        this.shiftId = shiftId;
        this.waiterId = waiterId;
        this.waiterName = waiterName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
    }

    public Long getShiftId() {return shiftId;}

    public Long getWaiterId() {return waiterId;}

    public String getWaiterName() {return waiterName;}

    public LocalDateTime getStartTime() {return startTime;}

    public LocalDateTime getEndTime() {return endTime;}

    public Long getTotalOrders() {return totalOrders;}

    public BigDecimal getTotalRevenue() {return totalRevenue;}
}