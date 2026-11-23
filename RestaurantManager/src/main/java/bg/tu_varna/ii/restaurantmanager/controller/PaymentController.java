package bg.tu_varna.ii.restaurantmanager.controller;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.PaymentResponseDto;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.PaymentMethod;
import bg.tu_varna.ii.restaurantmanager.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/restaurant/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // create payment for a bill
    @PostMapping("/bill/{billId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('WAITER')")
    public PaymentResponseDto createPayment(@PathVariable Long billId, @RequestParam PaymentMethod method) {
        return new PaymentResponseDto(paymentService.createPayment(billId, method));
    }

    // marks a payment as paid
    @PutMapping("/{paymentId}/pay")
    @PreAuthorize("hasRole('WAITER')")
    public PaymentResponseDto markPaymentAsPaid(@PathVariable Long paymentId) {
        return new PaymentResponseDto((paymentService.markPaymentAsPaid(paymentId)));
    }

    // get payment by its id
    @GetMapping("/{paymentId}")
    @PreAuthorize("hasAnyRole('WAITER', 'MANAGER', 'ADMIN')")
    public PaymentResponseDto getPaymentById(@PathVariable Long paymentId) {
        return new PaymentResponseDto(paymentService.getPaymentById(paymentId));
    }
}
