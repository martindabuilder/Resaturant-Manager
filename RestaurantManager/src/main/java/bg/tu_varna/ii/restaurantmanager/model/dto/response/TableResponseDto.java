package bg.tu_varna.ii.restaurantmanager.model.dto.response;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantTable;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.TableStatus;
import java.time.LocalDateTime;

public class TableResponseDto {
    private final Long id;
    private final Integer tableNumber;
    private final Integer capacity;
    private final TableStatus status;
    private final boolean active;
    private final LocalDateTime createdAt;

    public TableResponseDto(RestaurantTable table) {
        this.id = table.getId();
        this.tableNumber = table.getTableNumber();
        this.capacity = table.getCapacity();
        this.status = table.getStatus();
        this.active = table.isActive();
        this.createdAt = table.getCreatedAt();
    }

    public Long getId() {return id;}
    public Integer getCapacity() {return capacity;}
    public Integer getTableNumber() {return tableNumber;}
    public TableStatus getStatus() {return status;}
    public boolean isActive() {return active;}
    public LocalDateTime getCreatedAt() {return createdAt;}
}
