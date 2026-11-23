package bg.tu_varna.ii.restaurantmanager.controller;


import bg.tu_varna.ii.restaurantmanager.model.dto.response.DiscountResponseDto;
import bg.tu_varna.ii.restaurantmanager.model.entity.Discount;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.DiscountType;
import bg.tu_varna.ii.restaurantmanager.service.DiscountService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/restaurant/discounts")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    // create the discount
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public DiscountResponseDto createDiscount(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam DiscountType type,
            @RequestParam BigDecimal value,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {

        Discount discount = discountService.createDiscount(name, description, type, value, startDate, endDate);
        return new DiscountResponseDto(discount);
    }

    // get all discounts
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<DiscountResponseDto> getAllDiscounts() {
        return discountService.getAllDiscounts().stream().map(DiscountResponseDto::new).toList();
    }

    // get discount by ID
    @GetMapping("/{discountId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public DiscountResponseDto getDiscountById(@PathVariable Long discountId) {
        return new DiscountResponseDto(discountService.getDiscountById(discountId));
    }

    // get discounts by active status
    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<DiscountResponseDto> getDiscountsByStatus(@RequestParam boolean active) {
        return discountService.getDiscountsByStatus(active).stream().map(DiscountResponseDto::new).toList();
    }

    // update discount
    @PutMapping("/{discountId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public DiscountResponseDto updateDiscount(
            @PathVariable Long discountId,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam DiscountType type,
            @RequestParam BigDecimal value,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {

        Discount discount = discountService.updateDiscount(discountId, name, description, type, value, startDate, endDate);
        return new DiscountResponseDto(discount);
    }

    // activate discount
    @PutMapping("/{discountId}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public DiscountResponseDto activateDiscount(@PathVariable Long discountId) {
        return new DiscountResponseDto(discountService.activateDiscount(discountId));
    }

    // deactivate discount
    @PutMapping("/{discountId}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public DiscountResponseDto deactivateDiscount(@PathVariable Long discountId) {
        return new DiscountResponseDto(discountService.deactivateDiscount(discountId));
    }
}