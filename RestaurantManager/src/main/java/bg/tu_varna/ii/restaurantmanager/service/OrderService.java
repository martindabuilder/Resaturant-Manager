package bg.tu_varna.ii.restaurantmanager.service;
import bg.tu_varna.ii.restaurantmanager.exception.BusinessRuleException;
import bg.tu_varna.ii.restaurantmanager.exception.ResourceNotFoundException;
import bg.tu_varna.ii.restaurantmanager.model.entity.*;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.*;
import bg.tu_varna.ii.restaurantmanager.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final TableRepository tableRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final ReservationRepository reservationRepository;

    // used for the notifications once an order is ready
    private final NotificationService notificationService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, TableRepository tableRepository, UserRepository userRepository, DishRepository dishRepository, ReservationRepository reservationRepository, NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.tableRepository = tableRepository;
        this.userRepository = userRepository;
        this.dishRepository = dishRepository;
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
    }

    // create a new order
    public RestaurantOrder createOrder(Long tableId, String waiterUsername) {
        RestaurantTable table = tableRepository.findById(tableId).orElseThrow(() -> new ResourceNotFoundException("Table not found."));
        User waiter = userRepository.findByUsername(waiterUsername).orElseThrow(() -> new ResourceNotFoundException("Waiter not found."));

        if (waiter.getRole() != Role.WAITER) {
            throw new BusinessRuleException("Only waiters can create orders.");
        }

        if (!waiter.isEnabled()) {
            throw new BusinessRuleException("Disabled waiter accounts cannot create orders.");
        }

        if (!table.isActive()) {
            throw new BusinessRuleException("Orders cannot be created for inactive tables.");
        }

        if (table.getStatus() == TableStatus.RESERVED) {
            throw new BusinessRuleException("Cannot create an order for a reserved table.");
        }

        List<ReservationStatus> blockingStatuses = List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED, ReservationStatus.SEATED);
        boolean hasActiveReservation = reservationRepository.existsBlockingReservation(table.getId(), java.time.LocalDateTime.now(), blockingStatuses);

        if (hasActiveReservation) {
            throw new BusinessRuleException("Cannot create an order for a table with an active reservation.");
        }

        if (table.getStatus() == TableStatus.OCCUPIED) {
            throw new BusinessRuleException("Table is already occupied.");
        }

        RestaurantOrder order = new RestaurantOrder(table, waiter);
        table.setStatus(TableStatus.OCCUPIED);
        tableRepository.save(table);

        return orderRepository.save(order);
    }

    // get an order by ID
    public RestaurantOrder getOrderById(Long orderId) {
        return orderRepository.findWithDetailsById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found."));
    }

    // get all orders for a table
    public List<RestaurantOrder> getOrderByTable(Long tableId) {
        if (!tableRepository.existsById(tableId)) {
            throw new ResourceNotFoundException("Table not found.");
        }

        return orderRepository.findByTable_Id(tableId);
    }


    // get all orders across the restaurant
    public List<RestaurantOrder> getAllOrders() {
        return orderRepository.findAllWithDetailsBy();
    }


    // add dishes to an order
    public OrderItem addItem(Long orderId, Long dishId, Integer quantity) {
        RestaurantOrder order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        if (order.getStatus() != OrderStatus.OPEN) {
            throw new BusinessRuleException("Dishes can only be added to an open order.");
        }

        if (quantity == null ||quantity <= 0) {
            throw new BusinessRuleException("Quantity of items must be greater than zero.");
        }

        Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new ResourceNotFoundException("Dish not found."));

        if (!dish.isAvailable()) {
            throw new BusinessRuleException("Dish is currently unavailable.");
        }

        if (!dish.getCategory().isActive()) {
            throw new BusinessRuleException("Dish is from an inactive category.");
        }

        OrderItem item = new OrderItem(dish, quantity, dish.getPrice());

        order.addItem(item);

        return orderItemRepository.saveAndFlush(item);
    }


    // remove dishes from an order
    public void removeItem(Long orderId, Long itemId) {
        RestaurantOrder order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        if (order.getStatus()!= OrderStatus.OPEN) {
            throw new BusinessRuleException("Dishes can only be removed from open orders.");
        }

        OrderItem item = orderItemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Order item not found."));

        if (!Objects.equals(item.getOrder().getId(), orderId)) {
            throw new BusinessRuleException("This item doesn't belong to the selected order");
        }


        if (item.getStatus() != OrderItemStatus.NEW) {
            throw new BusinessRuleException("Only new items can be removed.");
        }

        order.removeItem(item);
        orderRepository.save(order);
    }



    // gets orders by waiter
    public List<RestaurantOrder> getOrderByWaiter(Long waiterId) {
        User waiter = userRepository.findById(waiterId).orElseThrow(() -> new ResourceNotFoundException("Waiter not found."));

        if (!waiter.isEnabled()) {
            throw new BusinessRuleException("The selected user account is disabled.");
        }

        if (waiter.getRole() != Role.WAITER) {
            throw new BusinessRuleException("The selected user is not a waiter.");
        }

        return orderRepository.findByWaiter_Id(waiterId);
    }


    // update the status of an individual item from the order
    // only available to users with the CHEF role
    private void updateOrderStatus(RestaurantOrder order) {
        List<OrderItem> items = order.getItems();

        if (items.isEmpty()) {
            order.setStatus(OrderStatus.OPEN);
            return;
        }

        List<OrderItem> activeItems = items.stream().filter(item -> item.getStatus() != OrderItemStatus.CANCELLED).toList();

        if (activeItems.isEmpty()) {
            order.setStatus(OrderStatus.CANCELLED);
            return;
        }

        boolean allReady = activeItems.stream().allMatch(item -> item.getStatus() == OrderItemStatus.READY || item.getStatus() == OrderItemStatus.SERVED);

        if (allReady) {
            order.setStatus(OrderStatus.READY);
            return;
        }

        boolean anyStarted = activeItems.stream()
                .anyMatch(item ->
                        item.getStatus() == OrderItemStatus.ACCEPTED ||
                                item.getStatus() == OrderItemStatus.PREPARING ||
                                item.getStatus() == OrderItemStatus.READY ||
                                item.getStatus() == OrderItemStatus.SERVED
                );

        if (anyStarted) {
            order.setStatus(OrderStatus.IN_PROGRESS);
        } else {
            order.setStatus(OrderStatus.OPEN);
        }
    }

    // updates the status of an item from the menu, used by the chef
    public OrderItem updateOrderItemStatus(Long itemId, OrderItemStatus newStatus) {
        if (newStatus == null) {
            throw new BusinessRuleException("New status cannot be null.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        Role currentRole = currentUser.getRole();

        if (newStatus == OrderItemStatus.SERVED && currentRole != Role.WAITER) {
            throw new BusinessRuleException("Only waiters can mark items as served.");
        }

        if (newStatus != OrderItemStatus.SERVED && currentRole != Role.CHEF) {
            throw new BusinessRuleException("Only chefs can update preparation statuses.");
        }

        OrderItem item = orderItemRepository.findWithDetailsById(itemId).orElseThrow(() -> new ResourceNotFoundException("Order item not found."));
        RestaurantOrder order = item.getOrder();

        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessRuleException("Cannot update items in a completed or cancelled order.");
        }

        if (item.getStatus() == null) {
            throw new BusinessRuleException("Current item status cannot be null.");
        }

        boolean validTransition = switch(item.getStatus()) {
            case NEW -> newStatus == OrderItemStatus.ACCEPTED || newStatus == OrderItemStatus.CANCELLED;
            case ACCEPTED ->  newStatus == OrderItemStatus.PREPARING || newStatus == OrderItemStatus.CANCELLED;
            case PREPARING ->  newStatus == OrderItemStatus.READY || newStatus == OrderItemStatus.CANCELLED;

            case READY -> newStatus == OrderItemStatus.SERVED;
            case SERVED, CANCELLED -> false;
        };

        if (!validTransition) {
            throw new BusinessRuleException("Invalid order item status transition");
        }

        item.setStatus(newStatus);
        updateOrderStatus(order);
        orderItemRepository.save(item);
        orderRepository.save(order);
        OrderStatus previousOrderStatus = order.getStatus();

        if (previousOrderStatus != OrderStatus.READY && order.getStatus() == OrderStatus.READY) {
            System.out.println("ORDER READY NOTIFICATION TRIGGERED!");
            notificationService.createNotification(order.getWaiter().getId(), "Order #" + order.getId() + " is ready to be served.", NotificationType.ORDER_READY);
        }

        return item;
    }

    // automatically marks a paid order as COMPLETED
    public void completeOrder(Long orderId) {
        RestaurantOrder order = getOrderById(orderId);

        if (order.getStatus() != OrderStatus.READY) {
            throw new BusinessRuleException("Only ready orders can be completed.");
        }

        RestaurantTable table = order.getTable();
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        table.setStatus(TableStatus.AVAILABLE);
        tableRepository.save(table);
    }


}
