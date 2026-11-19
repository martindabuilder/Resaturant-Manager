package bg.tu_varna.ii.restaurantmanager.service;

import bg.tu_varna.ii.restaurantmanager.exception.BusinessRuleException;
import bg.tu_varna.ii.restaurantmanager.exception.ResourceNotFoundException;
import bg.tu_varna.ii.restaurantmanager.model.entity.functionality.Role;
import bg.tu_varna.ii.restaurantmanager.model.entity.Shift;
import bg.tu_varna.ii.restaurantmanager.model.entity.User;
import bg.tu_varna.ii.restaurantmanager.repository.ShiftRepository;
import bg.tu_varna.ii.restaurantmanager.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;

    public ShiftService(ShiftRepository shiftRepository, UserRepository userRepository) {
        this.shiftRepository = shiftRepository;
        this.userRepository = userRepository;
    }

    // handles the checks and creation of the shifts
    public Shift createShift(Long waiterId, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new BusinessRuleException("Shift start and end times are required.");
        }

        if (!startTime.isBefore(endTime)) {
            throw new BusinessRuleException("Shift start time must be before end time.");
        }

        User waiter = userRepository.findById(waiterId).orElseThrow(() -> new ResourceNotFoundException("Waiter not found."));

        if (waiter.getRole() != Role.WAITER) {
            throw new BusinessRuleException("Shifts can only be assigned to users with the 'WAITER' role");
        }

        if (!waiter.isEnabled()) {
            throw new BusinessRuleException("Cannot assign shift to a user with disabled 'WAITER' role.");
        }

        List<Shift> existingShifts = shiftRepository.findByWaiter_Id(waiterId);

        for (Shift existingShift : existingShifts) {
            boolean overlaps = startTime.isBefore(existingShift.getEndTime()) && endTime.isAfter(existingShift.getStartTime());

            if (overlaps) {
                throw new BusinessRuleException("Waiter already has a shift during this time.");
            }}

        Shift shift = new Shift(startTime, endTime, waiter);

        return shiftRepository.save(shift);
    }

    // gets all the shifts for a certain waiter
    public List<Shift> getShiftsByWaiter(Long waiterId) {
        User waiter = userRepository.findById(waiterId).orElseThrow(() -> new ResourceNotFoundException("Waiter not found."));

        if (waiter.getRole() != Role.WAITER) {
            throw new BusinessRuleException("User does not have the 'WAITER' role.");
        }

        return shiftRepository.findByWaiter_Id(waiterId);
    }

    // gets the shifts
    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    // deletes a shift by its id
    public void deleteShift(long shiftId) {
        Shift shift = shiftRepository.findById(shiftId).orElseThrow(() -> new ResourceNotFoundException("Shift not found."));

        shiftRepository.delete(shift);
    }
}
