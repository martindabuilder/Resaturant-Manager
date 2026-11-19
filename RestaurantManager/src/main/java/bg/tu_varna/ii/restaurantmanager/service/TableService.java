package bg.tu_varna.ii.restaurantmanager.service;
import bg.tu_varna.ii.restaurantmanager.exception.BusinessRuleException;
import bg.tu_varna.ii.restaurantmanager.exception.DuplicateResourceException;
import bg.tu_varna.ii.restaurantmanager.exception.ResourceNotFoundException;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantTable;
import bg.tu_varna.ii.restaurantmanager.model.entity.TableStatus;
import bg.tu_varna.ii.restaurantmanager.repository.TableRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TableService {
    private final TableRepository tableRepository;

    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public RestaurantTable createTable(Integer tableNumber, Integer capacity) {
        if (tableNumber == null || tableNumber <= 0) {
            throw new BusinessRuleException("Table number must be bigger than 0.");
        }

        if (capacity == null || capacity <= 0) {
            throw new BusinessRuleException("Table capacity must be bigger than 0.");
        }

        if (tableRepository.existsByTableNumber(tableNumber)) {
            throw new DuplicateResourceException("Table number already exists.");
        }

        RestaurantTable table = new RestaurantTable(tableNumber, capacity);
        return tableRepository.save(table);
    }

    // Get all tables
    public List<RestaurantTable> getAllTables() {
        return tableRepository.findAll();
    }

    // get specific table by ID
    public RestaurantTable getTableById(Long id) {
        return tableRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Table not found with the following ID: " + id));
    }

    // get specific table by its number
    public RestaurantTable getTableByNumber(Integer tableNumber) {
        return tableRepository.findByTableNumber(tableNumber).orElseThrow(() -> new ResourceNotFoundException("Table not found with the following number: " + tableNumber));
    }

    // updates the table information
    public RestaurantTable updateTable(Long id, Integer tableNumber, Integer capacity) {
        RestaurantTable table = getTableById(id);

        if (tableNumber == null || tableNumber <= 0) {
            throw new BusinessRuleException("Table number must be bigger than 0.");
        }

        if (capacity == null || capacity <= 0) {
            throw new BusinessRuleException("Table capacity must be bigger than 0.");
        }

        if (!table.getTableNumber().equals(tableNumber) && tableRepository.existsByTableNumber(tableNumber)) {
            throw new DuplicateResourceException("Table number already exists.");
        }

        table.setTableNumber(tableNumber);
        table.setCapacity(capacity);

        return tableRepository.save(table);
    }

    // update table status
    public RestaurantTable updateTableStatus(Long id, TableStatus status) {
        RestaurantTable table = getTableById(id);

        if (status == null) {
            throw new BusinessRuleException("Table status cannot be empty.");
        }

        table.setStatus(status);
        return tableRepository.save(table);
    }

    // sets a table as active
    public RestaurantTable activateTable(Long id) {
        RestaurantTable table = getTableById(id);
        table.setActive(true);

        return tableRepository.save(table);
    }

    // sets a table as not active
    public RestaurantTable deactivateTable(Long id) {
        RestaurantTable table = getTableById(id);

        if (table.getStatus() != TableStatus.AVAILABLE) {
            throw new BusinessRuleException("Only available tables can be deactivated.");
        }

        table.setActive(false);
        return tableRepository.save(table);
    }

    // filters tables by their status
    public List<RestaurantTable> getTablesByStatus(TableStatus status) {
        return tableRepository.findByStatus(status);
    }

    // filters active tables
    public List<RestaurantTable> getTablesByActiveStatus(boolean active) {
        return tableRepository.findByActive(active);
    }

    // find tables with sufficient capacity
    public List<RestaurantTable> getTablesByCapacity(Integer capacity) {
        if (capacity == null || capacity <= 0) {
            throw new BusinessRuleException("Capacity must be greater than 0.");
        }

        return tableRepository.findByCapacityGreaterThanEqual(capacity);
    }
}
