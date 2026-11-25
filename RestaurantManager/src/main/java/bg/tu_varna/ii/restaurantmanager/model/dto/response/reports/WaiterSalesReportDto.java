package bg.tu_varna.ii.restaurantmanager.model.dto.response.reports;
import java.math.BigDecimal;

public class WaiterSalesReportDto {
    private Long waiterId;
    private String waiterName;
    private Long totalOrders;
    private Integer totalQuantity;
    private BigDecimal totalRevenue;

    public WaiterSalesReportDto(Long waiterId, String waiterName, Long totalOrders, Integer totalQuantity, BigDecimal totalRevenue) {
        this.waiterId = waiterId;
        this.waiterName = waiterName;
        this.totalOrders = totalOrders;
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
    }

    public Long getWaiterId() {return waiterId;}

    public String getWaiterName() {return waiterName;}

    public Long getTotalOrders() {return totalOrders;}

    public Integer getTotalQuantity() {return totalQuantity;}

    public BigDecimal getTotalRevenue() {return totalRevenue;}
}
