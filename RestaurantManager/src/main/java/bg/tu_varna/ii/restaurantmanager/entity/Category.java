package bg.tu_varna.ii.restaurantmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Category() {}

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
        this.active = true;
    }

    @PrePersist
    protected void onCreate() {createdAt = LocalDateTime.now();}

    public void setName(String name) {this.name = name;}
    public void setDescription(String description) {this.description = description;}
    public void setActive(boolean active) {this.active = active;}

}
