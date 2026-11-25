package bg.tu_varna.ii.restaurantmanager.model.dto.response.reports;
import java.math.BigDecimal;

public class AppliedDiscountsReportDto {
    private final Long discountId;
    private final String discountName;
    private final Long totalApplications;
    private final BigDecimal totalDiscountAmount;

    public AppliedDiscountsReportDto(Long discountId, String discountName, Long totalApplications, BigDecimal totalDiscountAmount) {
        this.discountId = discountId;
        this.discountName = discountName;
        this.totalApplications = totalApplications;
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public Long getDiscountId() {return discountId;}

    public String getDiscountName() {return discountName;}

    public Long getTotalApplications() {return totalApplications;}

    public BigDecimal getTotalDiscountAmount() {return totalDiscountAmount;}
}
