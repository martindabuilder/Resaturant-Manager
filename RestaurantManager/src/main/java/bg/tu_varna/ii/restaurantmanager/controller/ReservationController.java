package bg.tu_varna.ii.restaurantmanager.controller;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.ReservationResponseDto;
import bg.tu_varna.ii.restaurantmanager.model.entity.Reservation;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.ReservationStatus;
import bg.tu_varna.ii.restaurantmanager.service.ReservationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/restaurant/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // create a new reservation
    @PostMapping
    public ReservationResponseDto createReservation(@RequestParam Long tableId, @RequestParam String customerName, @RequestParam String phoneNumber, @RequestParam Integer guestCount, @RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime) {
        Reservation reservation = reservationService.createReservation(tableId, customerName, phoneNumber, guestCount, startTime, endTime);
        return new ReservationResponseDto(reservation);
    }

    // get all reservations
    @GetMapping
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public List<ReservationResponseDto> getAllReservations() {
        return reservationService.getAllReservations().stream().map(ReservationResponseDto::new).toList();
    }

    // get reservation by ID
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public ReservationResponseDto getReservationById(@PathVariable Long id) {
        return new ReservationResponseDto(reservationService.getReservationById(id));
    }

    // get reservations by table
    @GetMapping("/table/{tableId}")
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public List<ReservationResponseDto> getReservationsByTable(@PathVariable Long tableId) {
        return reservationService.getReservationsByTable(tableId).stream().map(ReservationResponseDto::new).toList();
    }

    // get reservations by status
    @GetMapping("/status/{status}")
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
    public List<ReservationResponseDto> getReservationsByStatus(@PathVariable ReservationStatus status) {
        return reservationService.getReservationsByStatus(status).stream().map(ReservationResponseDto::new).toList();
    }

    // search reservations by customer name
    @GetMapping("/search")
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER')")
    public List<ReservationResponseDto> searchReservations(@RequestParam String customerName) {
        return reservationService.searchReservationsByCustomerName(customerName).stream().map(ReservationResponseDto::new).toList();
    }

    // search reservations by phone number
    @GetMapping("/phone")
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER')")
    public List<ReservationResponseDto> getReservationsByPhoneNumber(@RequestParam String phoneNumber) {
        return reservationService.getReservationsByPhoneNumber(phoneNumber).stream().map(ReservationResponseDto::new).toList();
    }
}
