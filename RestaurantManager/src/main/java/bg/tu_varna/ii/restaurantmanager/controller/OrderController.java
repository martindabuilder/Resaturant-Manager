package bg.tu_varna.ii.restaurantmanager.controller;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.OrderItemResponseDto;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.OrderResponseDto;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderItemStatus;
import bg.tu_varna.ii.restaurantmanager.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //creates a new order, can be used by a waiter only
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('WAITER')")
    public OrderResponseDto createOrder(@RequestParam Long tableId, org.springframework.security.core.Authentication authentication) {
        return new OrderResponseDto(orderService.createOrder(tableId, authentication.getName()));
    }

    // get all orders, can be used by any role
    @GetMapping
    @PreAuthorize("hasAnyRole('WAITER', 'ADMIN', 'MANAGER', 'CHEF')")
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders().stream().map(OrderResponseDto::new).toList();
    }

    // get all orders by a certain id
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('WAITER', 'ADMIN', 'MANAGER', 'CHEF')")
    public OrderResponseDto getOrderById(@PathVariable Long orderId) {
        return new OrderResponseDto(orderService.getOrderById(orderId));
    }

    // get all orders by a table
    @GetMapping("/table/{tableId}")
    @PreAuthorize("hasAnyRole('WAITER', 'ADMIN', 'MANAGER', 'CHEF')")
    public List<OrderResponseDto> getOrderByTable(@PathVariable Long tableId) {
        return orderService.getOrderByTable(tableId).stream().map(OrderResponseDto::new).toList();
    }

    // all orders by a waiter
    @GetMapping("/waiter/{waiterId}")
    @PreAuthorize("hasAnyRole('WAITER', 'ADMIN', 'MANAGER', 'CHEF')")
    public List<OrderResponseDto> getOrdersByWaiter(@PathVariable Long waiterId) {
        return orderService.getOrderByWaiter(waiterId).stream().map(OrderResponseDto::new).toList();
    }

    // add a dish to an order, accessible only to a waiter
    @PostMapping("/{orderId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('WAITER')")
    public OrderItemResponseDto addItem(@PathVariable Long orderId, @RequestParam Long dishId, @RequestParam Integer quantity) {
        return new OrderItemResponseDto(orderService.addItem(orderId, dishId, quantity));
    }

    // remove a dish from an order, accessible only to a waiter only
    @DeleteMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('WAITER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        orderService.removeItem(orderId, itemId);
    }

    // update the item status in an order, accessible only to a chef only
    @PatchMapping("/items/{itemId}/status")
    @PreAuthorize("hasRole('CHEF')")
    public OrderItemResponseDto updateOrderItemStatus(@PathVariable Long itemId, @RequestParam OrderItemStatus status) {
        return new OrderItemResponseDto(orderService.updateOrderItemStatus(itemId, status));
    }
}
