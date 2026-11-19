package bg.tu_varna.ii.restaurantmanager.controller;

import bg.tu_varna.ii.restaurantmanager.model.dto.response.ShiftResponseDto;
import bg.tu_varna.ii.restaurantmanager.model.entity.Shift;
import bg.tu_varna.ii.restaurantmanager.service.ShiftService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/restaurant/shifts")
public class ShiftController {
    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    private ShiftResponseDto convertToDto(Shift shift) {
        return new ShiftResponseDto(
                shift.getId(),
                shift.getStartTime(),
                shift.getEndTime(),
                shift.getWaiter().getId(),
                shift.getWaiter().getUsername(),
                shift.getWaiter().getFirstName(),
                shift.getWaiter().getLastName(),
                shift.getCreatedAt()
        );
    }

    // create a shift
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ShiftResponseDto createShift(
            @RequestParam Long waiterId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        Shift shift = shiftService.createShift(waiterId, startTime, endTime);
        return convertToDto(shift);
    }

    // get all shifts
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER')")
    public List<ShiftResponseDto> getAllShifts() {
        return shiftService.getAllShifts().stream().map(this::convertToDto).toList();
    }

    // get shifts for a specific waiter
    @GetMapping("/waiter/{waiterId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER')")
    public List<ShiftResponseDto> getAllShiftsByWaiter(@PathVariable Long waiterId) {
        return shiftService.getShiftsByWaiter(waiterId).stream().map(this::convertToDto).toList();
    }

    // delete a specific shift by id
    @DeleteMapping("/{shiftId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void deleteShift(@PathVariable Long shiftId) {
        shiftService.deleteShift(shiftId);
    }


}
