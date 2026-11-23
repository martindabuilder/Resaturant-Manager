package bg.tu_varna.ii.restaurantmanager.service;
import bg.tu_varna.ii.restaurantmanager.exception.BusinessRuleException;
import bg.tu_varna.ii.restaurantmanager.exception.DuplicateResourceException;
import bg.tu_varna.ii.restaurantmanager.exception.ResourceNotFoundException;
import bg.tu_varna.ii.restaurantmanager.model.entity.Discount;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.DiscountType;
import bg.tu_varna.ii.restaurantmanager.repository.DiscountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class DiscountService {

    private final DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public Discount createDiscount(String name, String description, DiscountType type, BigDecimal value, LocalDateTime startDate, LocalDateTime endDate) {

        if (name == null || name.isBlank()) {
            throw new BusinessRuleException("Discount name cannot be empty.");
        }

        name = name.trim();

        if (type == null) {
            throw new BusinessRuleException("Discount type must be selected.");
        }

        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Discount value must be greater than zero.");
        }

        if (type == DiscountType.PERCENTAGE &&
                value.compareTo(new BigDecimal("100")) > 0) {
            throw new BusinessRuleException("Percentage discount cannot exceed 100.");
        }

        if (startDate == null || endDate == null ||
                !startDate.isBefore(endDate)) {
            throw new BusinessRuleException("Invalid discount period.");
        }

        if (discountRepository.existsByName(name)) {
            throw new DuplicateResourceException("Discount with this name already exists.");
        }

        Discount discount = new Discount(
                name, description, type, value, startDate, endDate
        );

        return discountRepository.save(discount);
    }

    @Transactional(readOnly = true)
    public Discount getDiscountById(Long discountId) {
        return discountRepository.findById(discountId).orElseThrow(() -> new ResourceNotFoundException("Discount not found."));
    }

    @Transactional(readOnly = true)
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Discount> getDiscountsByStatus(boolean active) {
        return discountRepository.findByActive(active);
    }

    public Discount updateDiscount(Long discountId, String name, String description, DiscountType type, BigDecimal value, LocalDateTime startDate, LocalDateTime endDate) {

        Discount discount = getDiscountById(discountId);

        if (name == null || name.isBlank()) {
            throw new BusinessRuleException("Discount name cannot be empty.");
        }

        name = name.trim();

        if (discountRepository.existsByName(name) &&
                !discount.getName().equalsIgnoreCase(name)) {
            throw new DuplicateResourceException("Discount with this name already exists.");
        }

        if (type == null) {
            throw new BusinessRuleException("Discount type must be selected.");
        }

        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Discount value must be greater than zero.");
        }

        if (type == DiscountType.PERCENTAGE &&
                value.compareTo(new BigDecimal("100")) > 0) {
            throw new BusinessRuleException("Percentage discount cannot exceed 100.");
        }

        if (startDate == null || endDate == null ||
                !startDate.isBefore(endDate)) {
            throw new BusinessRuleException("Invalid discount period.");
        }

        discount.setName(name);
        discount.setDescription(description);
        discount.setType(type);
        discount.setValue(value);
        discount.setStartDate(startDate);
        discount.setEndDate(endDate);

        return discountRepository.save(discount);
    }

    public Discount activateDiscount(Long discountId) {
        Discount discount = getDiscountById(discountId);
        discount.setActive(true);
        return discountRepository.save(discount);
    }

    // deactivates a discount
    public Discount deactivateDiscount(Long discountId) {
        Discount discount = getDiscountById(discountId);
        discount.setActive(false);
        return discountRepository.save(discount);
    }
}