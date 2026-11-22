package bg.tu_varna.ii.restaurantmanager.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "bills")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
