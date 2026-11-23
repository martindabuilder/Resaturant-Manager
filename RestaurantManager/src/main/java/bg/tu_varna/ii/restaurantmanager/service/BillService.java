package bg.tu_varna.ii.restaurantmanager.service;
import bg.tu_varna.ii.restaurantmanager.exception.BusinessRuleException;
import bg.tu_varna.ii.restaurantmanager.model.entity.Bill;
import bg.tu_varna.ii.restaurantmanager.model.entity.OrderItem;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantOrder;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderItemStatus;
import bg.tu_varna.ii.restaurantmanager.repository.BillRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Transactional
public class BillService {
    private final BillRepository billRepository;
    private final OrderService orderService;

    public BillService(BillRepository billRepository, OrderService orderService) {
        this.billRepository = billRepository;
        this.orderService = orderService;
    }

    public Bill createBill(Long orderId) {
        RestaurantOrder order = orderService.getOrderById(orderId);

        if (billRepository.existsByOrder_Id(orderId)) {
            throw new BusinessRuleException("This order already has an existing bill.");
        }

        if (order.getStatus() == null ||
                order.getStatus().name().equals("CANCELLED") ||
                order.getStatus().name().equals("COMPLETED")) {
            throw new BusinessRuleException("A bill cannot be created for a cancelled/completed order.");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        
        // calculating the total amount for all the items
        for (OrderItem item : order.getItems()) {
            if (item.getStatus() != OrderItemStatus.CANCELLED) {
                BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                totalAmount = totalAmount.add(itemTotal);
            }
        }

        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Cannot create a bill with zero amount to pay.");
        }

        Bill bill = new Bill(order, totalAmount);
        return billRepository.save(bill);
    }

    // get bill by its id
    public Bill getBillById(Long billId) {
        return billRepository.findById(billId).orElseThrow(() -> new BusinessRuleException("Bill not found."));
    }

    // get bill by the order id
    public Bill getBillByOrder(Long orderId) {
        return billRepository.findByOrder_Id(orderId).orElseThrow(() -> new BusinessRuleException("Bill not found for this order."));
    }
}
