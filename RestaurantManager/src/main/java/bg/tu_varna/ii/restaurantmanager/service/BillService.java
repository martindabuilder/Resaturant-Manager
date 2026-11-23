package bg.tu_varna.ii.restaurantmanager.service;
import bg.tu_varna.ii.restaurantmanager.exception.BusinessRuleException;
import bg.tu_varna.ii.restaurantmanager.exception.ResourceNotFoundException;
import bg.tu_varna.ii.restaurantmanager.model.entity.Bill;
import bg.tu_varna.ii.restaurantmanager.model.entity.OrderItem;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantOrder;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderItemStatus;
import bg.tu_varna.ii.restaurantmanager.repository.BillRepository;
import bg.tu_varna.ii.restaurantmanager.repository.DiscountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import bg.tu_varna.ii.restaurantmanager.model.entity.Discount;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.DiscountType;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Service
@Transactional
public class BillService {
    private final BillRepository billRepository;
    private final OrderService orderService;
    private final DiscountRepository discountRepository;

    public BillService(BillRepository billRepository, OrderService orderService, DiscountRepository discountRepository) {
        this.billRepository = billRepository;
        this.orderService = orderService;
        this.discountRepository = discountRepository;
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

    // applying a discount to the bill
    public Bill applyDiscount(Long billId, Long discountId) {
        Bill bill = getBillById(billId);
        Discount discount = discountRepository.findById(discountId).orElseThrow(() -> new ResourceNotFoundException("Discount not found."));

        if (bill.getDiscountId() != null) {
            throw new BusinessRuleException("A discount has already been applied to this bill.");
        }

        if (!discount.isActive()) {
            throw new BusinessRuleException("This discount is not active.");
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(discount.getStartDate()) || now.isAfter(discount.getEndDate())) {
            throw new BusinessRuleException("This discount is outside its valid period.");
        }

        BigDecimal discountAmount;

        if (discount.getType() == DiscountType.PERCENTAGE) {
            discountAmount = bill.getSubtotalAmount().multiply(discount.getValue()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        } else {
            discountAmount = discount.getValue();

        }

        // Discount cannot exceed the bill amount
        if (discountAmount.compareTo(bill.getSubtotalAmount()) > 0) {
            throw new BusinessRuleException("Discount cannot exceed the bill amount.");
        }

        BigDecimal finalAmount = bill.getSubtotalAmount().subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);

        bill.setDiscountId(discount.getId());
        bill.setDiscountName(discount.getName());
        bill.setDiscountType(discount.getType());
        bill.setDiscountValue(discount.getValue());
        bill.setDiscountAmount(discountAmount);
        bill.setTotalAmount(finalAmount);

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
