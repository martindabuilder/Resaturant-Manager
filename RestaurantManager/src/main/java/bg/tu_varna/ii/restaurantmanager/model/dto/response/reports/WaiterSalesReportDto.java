package bg.tu_varna.ii.restaurantmanager.model.dto.response.reports;
import java.math.BigDecimal;

public class WaiterSalesReportDto {
    private final Long waiterId;
    private final String waiterName;
    private final Long totalOrders;
    private final Long totalQuantity;
    private final BigDecimal totalRevenue;

    public WaiterSalesReportDto(Long waiterId, String waiterName, Long totalOrders, Long totalQuantity, BigDecimal totalRevenue) {
        this.waiterId = waiterId;
        this.waiterName = waiterName;
        this.totalOrders = totalOrders;
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
    }

    public Long getWaiterId() {return waiterId;}

    public String getWaiterName() {return waiterName;}

    public Long getTotalOrders() {return totalOrders;}

    public Long getTotalQuantity() {return totalQuantity;}

    public BigDecimal getTotalRevenue() {return totalRevenue;}
}