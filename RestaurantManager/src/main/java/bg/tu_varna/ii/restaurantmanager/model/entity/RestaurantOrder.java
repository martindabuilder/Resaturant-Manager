package bg.tu_varna.ii.restaurantmanager.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurant_orders")
public class RestaurantOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
