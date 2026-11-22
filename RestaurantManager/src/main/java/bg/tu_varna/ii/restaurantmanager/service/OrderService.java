package bg.tu_varna.ii.restaurantmanager.service;
import bg.tu_varna.ii.restaurantmanager.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final TableRepository tableRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, TableRepository tableRepository, UserRepository userRepository, DishRepository dishRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.tableRepository = tableRepository;
        this.userRepository = userRepository;
        this.dishRepository = dishRepository;
    }

    // create a new order

    // get an order by ID

    // get all orders for a table

    // add dishes to an order

    // remove dishes from an order

    // gets orders by waiter

    // update the status of an individual item from the order
    // only available to users with the CHEF role

}
