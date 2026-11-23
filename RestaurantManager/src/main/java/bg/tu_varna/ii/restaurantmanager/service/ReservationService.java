package bg.tu_varna.ii.restaurantmanager.service;
import bg.tu_varna.ii.restaurantmanager.exception.BusinessRuleException;
import bg.tu_varna.ii.restaurantmanager.exception.ResourceNotFoundException;
import bg.tu_varna.ii.restaurantmanager.model.entity.Reservation;
import bg.tu_varna.ii.restaurantmanager.model.entity.RestaurantTable;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.OrderStatus;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.ReservationStatus;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.TableStatus;
import bg.tu_varna.ii.restaurantmanager.repository.OrderRepository;
import bg.tu_varna.ii.restaurantmanager.repository.ReservationRepository;
import bg.tu_varna.ii.restaurantmanager.repository.TableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;
    private final OrderRepository orderRepository;

    public ReservationService(TableRepository tableRepository, ReservationRepository reservationRepository, OrderRepository orderRepository) {
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
        this.orderRepository = orderRepository;
    }

    // Creates a reservation and checks all required conditions
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

    // Gets all reservations
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Gets a reservation by ID
    @Transactional(readOnly = true)
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found."));
    }

    // Gets reservations by table
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByTable(Long tableId) {
        return reservationRepository.findByTable_Id(tableId);
    }

    // Gets reservations by status
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }

    // Searches reservations by customer name
    @Transactional(readOnly = true)
    public List<Reservation> searchReservationsByCustomerName(String customerName) {
        return reservationRepository.findByCustomerNameContainingIgnoreCase(customerName);
    }

    // Gets reservations by phone number
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByPhoneNumber(String phoneNumber) {
        return reservationRepository.findByPhoneNumber(phoneNumber);
    }

    // Updates reservation status and synchronizes table status
    @Transactional
    public Reservation updateReservationStatus(Long reservationId, ReservationStatus newStatus) {
        if (newStatus == null) {
            throw new BusinessRuleException("New reservation status cannot be null.");
        }

        Reservation reservation = reservationRepository.findWithTableById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation not found."));

        ReservationStatus currentStatus = reservation.getStatus();

        boolean validTransition = switch (currentStatus) {
            case PENDING -> newStatus == ReservationStatus.CONFIRMED || newStatus == ReservationStatus.CANCELLED;
            case CONFIRMED -> newStatus == ReservationStatus.SEATED || newStatus == ReservationStatus.CANCELLED;
            case SEATED -> newStatus == ReservationStatus.COMPLETED;
            case COMPLETED, CANCELLED -> false;
        };

        if (!validTransition) {
            throw new BusinessRuleException("Invalid reservation status transition.");
        }

        RestaurantTable table = reservation.getTable();

        // When the guests are seated, the table becomes occupied
        if (newStatus == ReservationStatus.SEATED) {
            table.setStatus(TableStatus.OCCUPIED);
            tableRepository.save(table);
        }

        // When the reservation is completed, check for unfinished orders and other active reservations
        if (newStatus == ReservationStatus.COMPLETED) {
            List<OrderStatus> unfinishedStatuses = List.of(OrderStatus.OPEN, OrderStatus.IN_PROGRESS, OrderStatus.READY);

            boolean hasUnfinishedOrder = orderRepository.existsByTable_IdAndStatusIn(table.getId(), unfinishedStatuses);

            List<ReservationStatus> blockingStatuses = List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED, ReservationStatus.SEATED);

            boolean hasOtherActiveReservation = reservationRepository.existsOtherBlockingReservation(table.getId(), reservation.getId(), LocalDateTime.now(), blockingStatuses);

            if (!hasUnfinishedOrder && !hasOtherActiveReservation) {
                table.setStatus(TableStatus.AVAILABLE);
                tableRepository.save(table);
            }
        }

        reservation.setStatus(newStatus);

        return reservationRepository.save(reservation);
    }
}
