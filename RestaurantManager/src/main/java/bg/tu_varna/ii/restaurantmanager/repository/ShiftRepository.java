package bg.tu_varna.ii.restaurantmanager.repository;

import bg.tu_varna.ii.restaurantmanager.model.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByWaiter_Id(Long waiterId);
}
