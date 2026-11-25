package bg.tu_varna.ii.restaurantmanager.model.dto.response.reports;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.TableStatus;

public class TableOccupancyReportDto {
    private final Long tableId;
    private final Integer tableNumber;
    private final Integer capacity;
    private final TableStatus status;

    public TableOccupancyReportDto(Long tableId, Integer tableNumber, Integer capacity, TableStatus status) {
        this.tableId = tableId;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.status = status;
    }

    public Long getTableId() {return tableId;}
    public Integer getTableNumber() {
        return tableNumber;
    }
    public Integer getCapacity() {
        return capacity;
    }
    public TableStatus getStatus() {
        return status;
    }
}
