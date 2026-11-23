package bg.tu_varna.ii.restaurantmanager.controller;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.BillResponseDto;
import bg.tu_varna.ii.restaurantmanager.service.BillService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurant/bills")
public class BillController {
    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    // create bill for a certain order
    @PostMapping("/order/{orderId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('WAITER')")
    public BillResponseDto createBill(@PathVariable Long orderId) {
        return new BillResponseDto(billService.createBill(orderId));
    }

    // get bill by its id
    @GetMapping("/{billId}")
    @PreAuthorize("hasAnyRole('WAITER', 'ADMIN', 'MANAGER')")
    public BillResponseDto getBillById(@PathVariable Long billId) {
        return new BillResponseDto(billService.getBillById(billId));
    }

    // get a bill by its order id
    @GetMapping("/order/{orderId}")
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('WAITER', 'ADMIN', 'MANAGER')")
    public BillResponseDto getBillByOrder(@PathVariable Long orderId) {
        return new BillResponseDto(billService.getBillByOrder(orderId));
    }

}
