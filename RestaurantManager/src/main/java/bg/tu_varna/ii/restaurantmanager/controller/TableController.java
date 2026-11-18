package bg.tu_varna.ii.restaurantmanager.controller;

import bg.tu_varna.ii.restaurantmanager.model.dto.response.TableResponseDto;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantTable;
import bg.tu_varna.ii.restaurantmanager.model.entity.TableStatus;
import bg.tu_varna.ii.restaurantmanager.service.TableService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant/tables")
public class TableController {
    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    // create a new table
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public TableResponseDto createTable(@RequestParam Integer tableNumber, @RequestParam Integer capacity) {
        RestaurantTable table = tableService.createTable(tableNumber, capacity);

        return new TableResponseDto(table);
    }

    // get all tables
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CHEF', 'WAITER')")
    public List<TableResponseDto> getAllTables() {
        return tableService.getAllTables().stream().map(TableResponseDto::new).toList();
    }

    // get a table by its id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CHEF', 'WAITER')")
    public TableResponseDto getTableById(@PathVariable Long id) {
        return new TableResponseDto(tableService.getTableById(id));
    }

    // get a table by its number
    @GetMapping("/number/{tableNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CHEF', 'WAITER')")
    public TableResponseDto getTableByNumber(@PathVariable Integer tableNumber) {
        return new TableResponseDto(tableService.getTableByNumber(tableNumber));
    }

    // update a table's information
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public TableResponseDto updateTable(@PathVariable Long id, @RequestParam Integer tableNumber, @RequestParam Integer capacity) {
        return new TableResponseDto(tableService.updateTable(id, tableNumber, capacity));
    }
    
    // update table status
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public TableResponseDto updateTableStatus(@PathVariable Long id, @RequestParam TableStatus status) {
        return new TableResponseDto(tableService.updateTableStatus(id, status));
    }

    // activate table
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public TableResponseDto activateTable(@PathVariable Long id) {
        return new TableResponseDto(tableService.activateTable(id));
    }

    // deactivate a table
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public TableResponseDto deactivateTable(@PathVariable Long id) {
        return new TableResponseDto(tableService.deactivateTable(id));
    }

    // filter tables by status
    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CHEF', 'WAITER')")
    public List<TableResponseDto> getTablesByStatus(@RequestParam TableStatus status) {
        return tableService.getTablesByStatus(status).stream().map(TableResponseDto::new).toList();
    }

    // filter tables by their availability
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<TableResponseDto> getTablesByActiveStatus(@RequestParam boolean active) {
        return tableService.getTablesByActiveStatus(active).stream().map(TableResponseDto::new).toList();
    }

    // find tables with enough capacity
    @GetMapping("/capacity")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER')")
    public List<TableResponseDto> getTablesByCapacity(@RequestParam Integer capacity) {
        return tableService.getTablesByCapacity(capacity).stream().map(TableResponseDto::new).toList();
    }
}
