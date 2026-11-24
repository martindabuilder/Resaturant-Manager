package bg.tu_varna.ii.restaurantmanager.model.dto.response;

import java.math.BigDecimal;

public class DishSalesReportDto {
    private Long dishId;
    private String dishName;
    private Integer totalQuantity;
    private BigDecimal totalRevenue;

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
