package bg.tu_varna.ii.restaurantmanager.service;
import bg.tu_varna.ii.restaurantmanager.model.entity.Payment;
import bg.tu_varna.ii.restaurantmanager.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BillService billService;

    public PaymentService(PaymentRepository paymentRepository, BillService billService) {
        this.paymentRepository = paymentRepository;
        this.billService = billService;
    }

    // create payment for a bill
    public Payment createPayment() {}
}
