package bg.tu_varna.ii.restaurantmanager.repository;
import bg.tu_varna.ii.restaurantmanager.model.entity.Payment;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByBill_Id(Long billId);
    List<Payment> findByStatus(PaymentStatus status);
}
