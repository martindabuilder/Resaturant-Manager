
package bg.tu_varna.ii.restaurantmanager.controller;

import bg.tu_varna.ii.restaurantmanager.exception.BusinessRuleException;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.DishResponseDto;
import bg.tu_varna.ii.restaurantmanager.service.DishService;
import bg.tu_varna.ii.restaurantmanager.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class CustomerCartController {

    private final DishService dishService;
    private final OrderService orderService;

    public CustomerCartController(
            DishService dishService,
            OrderService orderService) {
        this.dishService = dishService;
        this.orderService = orderService;
    }

    @SuppressWarnings("unchecked")
    private Map<Long, Integer> getCart(HttpSession session) {
        Map<Long, Integer> cart =
                (Map<Long, Integer>) session.getAttribute("customerCart");

        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute("customerCart", cart);
        }

        return cart;
    }

    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam Long tableId,
            @RequestParam Long dishId,
            HttpSession session) {

        DishResponseDto dish = dishService.getDishById(dishId);

        if (!dish.isAvailable()) {
            throw new BusinessRuleException("This dish is unavailable.");
        }

        Object selectedTable = session.getAttribute("customerTableId");

        if (selectedTable != null && !selectedTable.equals(tableId)) {
            getCart(session).clear();
        }

        session.setAttribute("customerTableId", tableId);

        Map<Long, Integer> cart = getCart(session);
        cart.merge(dishId, 1, Integer::sum);

        return "redirect:/cart?tableId=" + tableId;
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(
            @RequestParam Long tableId,
            @RequestParam Long dishId,
            HttpSession session) {

        getCart(session).remove(dishId);

        return "redirect:/cart?tableId=" + tableId;
    }

    @GetMapping("/cart")
    public String viewCart(
            @RequestParam Long tableId,
            HttpSession session,
            Model model) {

        Map<Long, Integer> cart = getCart(session);

        var cartDishes = new ArrayList<DishResponseDto>();
        Map<Long, BigDecimal> subtotals = new LinkedHashMap<>();

        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            DishResponseDto dish = dishService.getDishById(entry.getKey());

            cartDishes.add(dish);

            BigDecimal subtotal = dish.getPrice()
                    .multiply(BigDecimal.valueOf(entry.getValue()));

            subtotals.put(dish.getId(), subtotal);
            total = total.add(subtotal);
        }

        model.addAttribute("cartItems", cart);
        model.addAttribute("cartDishes", cartDishes);
        model.addAttribute("subtotals", subtotals);
        model.addAttribute("total", total);
        model.addAttribute("tableId", tableId);

        return "cart";
    }

    @PostMapping("/cart/submit")
    public String submitOrder(
            @RequestParam Long tableId,
            HttpSession session) {

        Map<Long, Integer> cart = getCart(session);

        if (cart.isEmpty()) {
            throw new BusinessRuleException("Количката е празна.");
        }

        var order = orderService.submitCustomerOrder(tableId, cart);

        session.removeAttribute("customerCart");
        session.removeAttribute("customerTableId");

        return "redirect:/order-success?orderId=" + order.getId();
    }
}
