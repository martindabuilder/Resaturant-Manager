package bg.tu_varna.ii.restaurantmanager.model.dto.response.reports;
import java.math.BigDecimal;

public class CategorySalesReportDto {
    private final Long categoryId;
    private final String categoryName;
    private final Integer totalQuantity;
    private final BigDecimal totalRevenue;

    public CategorySalesReportDto(Long categoryId, String categoryName, Integer totalQuantity, BigDecimal totalRevenue) {

        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
    }

    public Long getCategoryId() {return categoryId;}

    public String getCategoryName() {return categoryName;}

    public Integer getTotalQuantity() {return totalQuantity;}

    public BigDecimal getTotalRevenue() {return totalRevenue;}
}
