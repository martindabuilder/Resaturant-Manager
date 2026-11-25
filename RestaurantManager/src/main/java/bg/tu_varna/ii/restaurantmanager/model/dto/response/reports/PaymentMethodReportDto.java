package bg.tu_varna.ii.restaurantmanager.model.dto.response.reports;
import java.math.BigDecimal;

public class PaymentMethodReportDto {
    private final String paymentMethod;
    private final Long totalPayments;
    private final BigDecimal totalRevenue;

    public PaymentMethodReportDto(String paymentMethod, Long totalPayments, BigDecimal totalRevenue) {
        this.paymentMethod = paymentMethod;
        this.totalPayments = totalPayments;
        this.totalRevenue = totalRevenue;
    }

    public String getPaymentMethod() {return paymentMethod;}

    public Long getTotalPayments() {return totalPayments;}

    public BigDecimal getTotalRevenue() {return totalRevenue;}
}
