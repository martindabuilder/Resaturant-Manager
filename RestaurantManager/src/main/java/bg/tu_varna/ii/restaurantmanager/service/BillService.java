package bg.tu_varna.ii.restaurantmanager.service;
import bg.tu_varna.ii.restaurantmanager.exception.BusinessRuleException;
import bg.tu_varna.ii.restaurantmanager.model.entity.Bill;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantOrder;
import bg.tu_varna.ii.restaurantmanager.repository.BillRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BillService {
    private final BillRepository billRepository;
    private final OrderService orderService;

    public BillService(BillRepository billRepository, OrderService orderService) {
        this.billRepository = billRepository;
        this.orderService = orderService;
    }

    public Bill createBIll(Long orderId) {
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


        Bill bill = new Bill(order, totalAmount);
        return billRepository.save(bill);
    }

    // get bill by its id

    // get bill by the order id
}
