package bg.tu_varna.ii.restaurantmanager.model.dto.response;

import java.time.LocalDateTime;

public class ShiftResponseDto {
    private final Long id;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    // waiter specific returns
    private final Long waiterId;
    private final String waiterUsername;
    private final String waiterFirstName;
    private final String waiterLastName;
    private final LocalDateTime createdAt;

    public ShiftResponseDto(Long id, LocalDateTime startTime, LocalDateTime endTime, Long waiterId, String waiterUsername, String waiterFirstName, String waiterLastName, LocalDateTime createdAt) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.waiterId = waiterId;
        this.waiterUsername = waiterUsername;
        this.waiterFirstName = waiterFirstName;
        this.waiterLastName = waiterLastName;
        this.createdAt = createdAt;
    }

    public Long getId() {return id;}
    public LocalDateTime getEndTime() {return endTime;}
    public Long getWaiterId() {return waiterId;}
    public String getWaiterUsername() {return waiterUsername;}
    public String getWaiterFirstName() {return waiterFirstName;}
    public String getWaiterLastName() {return waiterLastName;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public LocalDateTime getStartTime() {return startTime;}
}
