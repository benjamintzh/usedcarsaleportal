package com.autoxpress.usedcarsalesportal.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public class CarDto {
    @NotEmpty
    private String manufacture;
    
    @NotEmpty
    private String model;
    
    @Min(1900)
    private int registrationYear;
    
    @Min(0)
    private double price;
    
    private MultipartFile image;
    
    private Long id;

    // Getters and Setters
    public String getManufacture() { return manufacture; }
    public void setManufacture(String manufacture) { this.manufacture = manufacture; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public int getRegistrationYear() { return registrationYear; }
    public void setRegistrationYear(int registrationYear) { this.registrationYear = registrationYear; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public MultipartFile getImage() { return image; }
    public void setImage(MultipartFile image) { this.image = image; }
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
}