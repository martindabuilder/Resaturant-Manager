package bg.tu_varna.ii.restaurantmanager.controller;

import bg.tu_varna.ii.restaurantmanager.model.dto.response.TableResponseDto;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantTable;
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

    // activate table

    // deactivate a table

    // filter tables by status

    // filter tables by their availability

    // find tables with enough capacity
}
