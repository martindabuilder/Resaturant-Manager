package bg.tu_varna.ii.restaurantmanager.model.dto.response;

import bg.tu_varna.ii.restaurantmanager.model.entity.Reservation;
import bg.tu_varna.ii.restaurantmanager.model.entity.ReservationStatus;
import java.time.LocalDateTime;

public class ReservationResponseDto {
    private final Long id;
    private final String customerName;
    private final String phoneNumber;
    private final Integer guestCount;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final ReservationStatus status;
    private final Long tableId;
    private final Integer tableNumber;
    private final LocalDateTime createdAt;

    public ReservationResponseDto(Reservation reservation) {
        this.id = reservation.getId();
        this.customerName = reservation.getCustomerName();
        this.phoneNumber = reservation.getPhoneNumber();
        this.guestCount = reservation.getGuestCount();
        this.startTime = reservation.getStartTime();
        this.endTime = reservation.getEndTime();
        this.status = reservation.getStatus();
        this.tableId = reservation.getTable().getId();
        this.tableNumber = reservation.getTable().getTableNumber();
        this.createdAt = reservation.getCreatedAt();
    }

    public Long getId() {return id;}
    public String getCustomerName() {return customerName;}
    public String getPhoneNumber() {return phoneNumber;}
    public Integer getGuestCount() {return guestCount;}
    public LocalDateTime getStartTime() {return startTime;}
    public LocalDateTime getEndTime() {return endTime;}
    public ReservationStatus getStatus() {return status;}
    public Long getTableId() {return tableId;}
    public Integer getTableNumber() {return tableNumber;}
    public LocalDateTime getCreatedAt() {return createdAt;}
}
