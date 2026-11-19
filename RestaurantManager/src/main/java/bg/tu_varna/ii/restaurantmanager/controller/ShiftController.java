package bg.tu_varna.ii.restaurantmanager.controller;

import bg.tu_varna.ii.restaurantmanager.model.entity.Shift;
import bg.tu_varna.ii.restaurantmanager.repository.ShiftRepository;
import bg.tu_varna.ii.restaurantmanager.service.ShiftService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant/shifts")
public class ShiftController {
    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }


    //get all shifts

    // get shifts for a specific waiter

    // delete a specific shift by id
}
