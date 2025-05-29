package com.autoxpress.usedcarsalesportal.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String manufacture;
    private String model;
    private int registrationYear;
    private BigDecimal price;
    private String imagePath;
    private boolean active;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getManufacture() { return manufacture; }
    public void setManufacture(String manufacture) { this.manufacture = manufacture; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public int getRegistrationYear() { return registrationYear; }
    public void setRegistrationYear(int registrationYear) { this.registrationYear = registrationYear; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}