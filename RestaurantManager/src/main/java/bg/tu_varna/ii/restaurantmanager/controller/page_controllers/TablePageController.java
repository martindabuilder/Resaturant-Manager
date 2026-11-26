package bg.tu_varna.ii.restaurantmanager.controller.page_controllers;
import org.springframework.ui.Model;
import bg.tu_varna.ii.restaurantmanager.exception.BusinessRuleException;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.TableResponseDto;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.TableStatus;
import bg.tu_varna.ii.restaurantmanager.service.TableService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TablePageController {

    private final TableService tableService;

    public TablePageController(TableService tableService) {
        this.tableService = tableService;
    }

    // Display all tables
    @GetMapping("/tables")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public String tablesPage(@RequestParam(required = false) TableStatus status, @RequestParam(required = false) Boolean active, Model model) {

        var tables = tableService.getAllTables().stream()
                .map(TableResponseDto::new)
                .filter(table -> status == null || table.getStatus() == status)
                .filter(table -> active == null || table.isActive() == active)
                .toList();

        model.addAttribute("tables", tables);
        model.addAttribute("status", status);
        model.addAttribute("active", active);
        model.addAttribute("statuses", TableStatus.values());

        return "tables";
    }

    // Create table
    @PostMapping("/tables/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String createTable(@RequestParam Integer tableNumber, @RequestParam Integer capacity) {
        tableService.createTable(tableNumber, capacity);

        return "redirect:/tables";
    }

    // Update table information
    @PostMapping("/tables/{id}/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String updateTable(@PathVariable Long id, @RequestParam Integer tableNumber, @RequestParam Integer capacity) {
        tableService.updateTable(id, tableNumber, capacity);

        return "redirect:/tables";
    }

    // Update table status
    @PostMapping("/tables/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER')")
    public String updateTableStatus(@PathVariable Long id, @RequestParam TableStatus status) {
        if (status == TableStatus.RESERVED) {
            throw new BusinessRuleException("Table reservation status is managed through reservations.");
        }

        tableService.updateTableStatus(id, status);

        return "redirect:/tables";
    }

    // Activate table
    @PostMapping("/tables/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String activateTable(@PathVariable Long id) {
        tableService.activateTable(id);

        return "redirect:/tables";
    }

    // Deactivate table
    @PostMapping("/tables/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String deactivateTable(@PathVariable Long id) {
        tableService.deactivateTable(id);

        return "redirect:/tables";
    }
}