package bg.tu_varna.ii.restaurantmanager.repository;

import bg.tu_varna.ii.restaurantmanager.model.entity.Reservation;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByTable_Id(Long tableId);

    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByCustomerNameContainingIgnoreCase(String customerName);
    List<Reservation> findByPhoneNumber(String phoneNumber);
    List<Reservation> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    // checks for overlapping reservations
    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.table.id = :tableId AND r.status IN :statuses AND r.startTime < :endTime AND r.endTime > :startTime")
    boolean existsOverlappingReservation(@Param("tableId") Long tableId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("statuses") List<ReservationStatus> statuses);
}
