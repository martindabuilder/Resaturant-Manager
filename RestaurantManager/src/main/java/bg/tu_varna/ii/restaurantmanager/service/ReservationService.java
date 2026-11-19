package bg.tu_varna.ii.restaurantmanager.service;

import bg.tu_varna.ii.restaurantmanager.exception.BusinessRuleException;
import bg.tu_varna.ii.restaurantmanager.exception.ResourceNotFoundException;
import bg.tu_varna.ii.restaurantmanager.model.entity.Reservation;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.ReservationStatus;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantTable;
import bg.tu_varna.ii.restaurantmanager.repository.ReservationRepository;
import bg.tu_varna.ii.restaurantmanager.repository.TableRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(TableRepository tableRepository, ReservationRepository reservationRepository) {
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
    }

    // handles the reservation creation and all of the checks for it
    @Transactional
    public Reservation createReservation(Long tableId, String customerName, String phoneNumber, Integer guestCount, LocalDateTime startTime, LocalDateTime endTime) {

        if (customerName == null || customerName.isBlank()) {
            throw new BusinessRuleException("Customer name is required.");
        }

        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new BusinessRuleException("Phone number is required.");
        }

        if (guestCount == null || guestCount <= 0) {
            throw new BusinessRuleException("Guest count must be bigger than 0.");
        }

        if (startTime == null || endTime == null) {
            throw new BusinessRuleException("Start time and end time of reservation are required.");
        }

        if (!startTime.isBefore(endTime)) {
            throw new BusinessRuleException("Start time must be before end time.");
        }

        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found."));

        if (!table.isActive()) {
            throw new BusinessRuleException("Table is not active.");
        }

        if (table.getCapacity() < guestCount) {
            throw new BusinessRuleException("Guest count exceeds table capacity.");
        }

        List<ReservationStatus> activeStatuses = List.of(
                ReservationStatus.PENDING,
                ReservationStatus.CONFIRMED,
                ReservationStatus.SEATED
        );

        boolean overlapping = reservationRepository.existsOverlappingReservation(tableId, startTime, endTime, activeStatuses);

        if (overlapping) {
            throw new BusinessRuleException("The table already has a reservation during this time.");
        }

        Reservation reservation = new Reservation(customerName, phoneNumber, guestCount, startTime, endTime, table);
        return reservationRepository.save(reservation);
    }

    // gets all the reservations
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // gets the reserv. by its id
    @Transactional(readOnly = true)
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found."));
    }

    // gets the reserv. by the table
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByTable(Long tableId) {
        return reservationRepository.findByTable_Id(tableId);
    }

    // gets the reserv. by its status
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }

    // gets a reserv. by the name of the customer
    @Transactional(readOnly = true)
    public List<Reservation> searchReservationsByCustomerName(String customerName) {
        return reservationRepository.findByCustomerNameContainingIgnoreCase(customerName);
    }

    // gets reserv. by the phone number attached to it
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByPhoneNumber(String phoneNumber) {
        return reservationRepository.findByPhoneNumber(phoneNumber);
    }
}
