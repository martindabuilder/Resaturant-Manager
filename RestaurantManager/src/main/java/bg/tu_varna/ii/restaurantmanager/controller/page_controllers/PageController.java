
package bg.tu_varna.ii.restaurantmanager.controller.page_controllers;

import bg.tu_varna.ii.restaurantmanager.model.dto.response.OrderResponseDto;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.UserResponseDto;
import bg.tu_varna.ii.restaurantmanager.service.CategoryService;
import bg.tu_varna.ii.restaurantmanager.service.DishService;
import bg.tu_varna.ii.restaurantmanager.service.OrderService;
import bg.tu_varna.ii.restaurantmanager.service.TableService;
import bg.tu_varna.ii.restaurantmanager.service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    private final TableService tableService;
    private final DishService dishService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final UserService userService;

    public PageController(
            TableService tableService,
            DishService dishService,
            CategoryService categoryService,
            OrderService orderService,
            UserService userService) {

        this.tableService = tableService;
        this.dishService = dishService;
        this.categoryService = categoryService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());

        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .orElse("UNKNOWN");

        model.addAttribute("role", role);
        return "dashboard";
    }

    @GetMapping("/tables-selection")
    public String tablesSelection(Model model) {
        model.addAttribute("tables", tableService.getAvailableTables());
        return "tables-selection";
    }

    @GetMapping("/menu")
    public String menuPage(@RequestParam Long tableId, Model model) {
        model.addAttribute("dishes", dishService.getAllDishes());
        model.addAttribute("categories", categoryService.getCategoriesByStatus(true));
        model.addAttribute("tableId", tableId);

        return "menu";
    }

    @GetMapping("/order-success")
    public String orderSuccess(
            @RequestParam Long orderId,
            Model model) {

        var order = orderService.getOrderById(orderId);

        model.addAttribute("orderId", order.getId());
        model.addAttribute("tableNumber", order.getTable().getTableNumber());

        return "order-success";
    }

    @GetMapping("/orders")
    public String ordersPage(Authentication authentication, Model model) {

        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .orElse("UNKNOWN");

        var orders = switch (role) {
            case "WAITER" -> orderService
                    .getOrdersByWaiterUsername(authentication.getName());

            case "ADMIN", "MANAGER", "CHEF" -> orderService.getAllOrders();

            default -> throw new IllegalStateException("Unknown user role.");
        };

        model.addAttribute("orders", orders.stream()
                .map(OrderResponseDto::new)
                .toList());

        model.addAttribute("role", role);

        return "orders";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

}
