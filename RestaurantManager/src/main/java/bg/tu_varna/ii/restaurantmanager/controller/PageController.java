
package bg.tu_varna.ii.restaurantmanager.controller;

import bg.tu_varna.ii.restaurantmanager.service.CategoryService;
import bg.tu_varna.ii.restaurantmanager.service.DishService;
import bg.tu_varna.ii.restaurantmanager.service.TableService;
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

    public PageController(TableService tableService,
                          DishService dishService,
                          CategoryService categoryService) {
        this.tableService = tableService;
        this.dishService = dishService;
        this.categoryService = categoryService;
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
}
