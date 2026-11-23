package bg.tu_varna.ii.restaurantmanager.service;
import bg.tu_varna.ii.restaurantmanager.exception.BusinessRuleException;
import bg.tu_varna.ii.restaurantmanager.exception.ResourceNotFoundException;
import bg.tu_varna.ii.restaurantmanager.model.entity.Bill;
import bg.tu_varna.ii.restaurantmanager.model.entity.Payment;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.PaymentMethod;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.PaymentStatus;
import bg.tu_varna.ii.restaurantmanager.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BillService billService;
    private final OrderService orderService;

    public PaymentService(PaymentRepository paymentRepository, BillService billService, OrderService orderService) {
        this.paymentRepository = paymentRepository;
        this.billService = billService;
        this.orderService = orderService;
    }

    // automatically create payment for a bill
    public Payment createPayment(Long billId, PaymentMethod paymentMethod) {
        Bill bill = billService.getBillById(billId);

        if (paymentMethod == null) {
            throw new BusinessRuleException("Must choose a payment method.");
        }

        if (bill.getTotalAmount() == null || bill.getTotalAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Bill amount must be greater than zero.");
        }

        boolean paymentAlreadyExists = paymentRepository.findByBill_Id(billId).stream()
                .anyMatch(payment ->
                        payment.getPaymentStatus() == PaymentStatus.PENDING ||
                                payment.getPaymentStatus() == PaymentStatus.PAID);

        if (paymentAlreadyExists) {
            throw new BusinessRuleException("A pending or completed payment already exists for this order.");
        }

        Payment payment = new Payment(paymentMethod, PaymentStatus.PENDING, bill, bill.getTotalAmount());

        return paymentRepository.save(payment);
    }

    // get payment by its id
    public Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(() -> new ResourceNotFoundException("Payment not found."));
    }

    // mark successfully paid payments
    public Payment markPaymentAsPaid(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new ResourceNotFoundException("Payment not found."));

        if (payment.getPaymentStatus() != PaymentStatus.PENDING) {
            throw new BusinessRuleException("Only pending payments can be marked as paid.");
        }

        Long orderId = payment.getBill().getOrder().getId();
        orderService.completeOrder(orderId);

        payment.setPaidAt(LocalDateTime.now());
        payment.setPaymentStatus(PaymentStatus.PAID);

        return paymentRepository.save(payment);
    }
}
