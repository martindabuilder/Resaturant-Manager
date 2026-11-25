package bg.tu_varna.ii.restaurantmanager.model.dto.response.reports;

import java.math.BigDecimal;

public class DishSalesReportDto {
    private final Long dishId;
    private final String dishName;
    private final Integer totalQuantity;
    private final BigDecimal totalRevenue;

    public DishSalesReportDto(Long dishId, String dishName, Integer totalQuantity, BigDecimal totalRevenue) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
    }

    public Long getDishId() {return dishId;}
    public String getDishName() {return dishName;}
    public Integer getTotalQuantity() {return totalQuantity;}
    public BigDecimal getTotalRevenue() {return totalRevenue;}
}
