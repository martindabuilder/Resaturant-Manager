package bg.tu_varna.ii.restaurantmanager.model.dto.response.reports;
import java.math.BigDecimal;

public class DishSalesReportDto {
    private final Long dishId;
    private final String dishName;
    private final Long totalQuantity;
    private final BigDecimal totalRevenue;

    public DishSalesReportDto(Long dishId, String dishName, Long totalQuantity, BigDecimal totalRevenue) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
    }

    public Long getDishId() {return dishId;}

    public String getDishName() {return dishName;}

    public Long getTotalQuantity() {return totalQuantity;}

    public BigDecimal getTotalRevenue() {return totalRevenue;}
}