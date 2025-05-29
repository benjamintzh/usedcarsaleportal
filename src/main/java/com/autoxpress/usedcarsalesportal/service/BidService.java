package com.autoxpress.usedcarsalesportal.service;

import com.autoxpress.usedcarsalesportal.model.Bid;
import com.autoxpress.usedcarsalesportal.model.Car;
import com.autoxpress.usedcarsalesportal.model.User;
import com.autoxpress.usedcarsalesportal.repository.BidRepository;
import com.autoxpress.usedcarsalesportal.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BidService {
    @Autowired
    private BidRepository bidRepository;
    
    @Autowired
    private CarRepository carRepository;

    public Bid placeBid(Long carId, User user, double amount) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        Bid bid = new Bid();
        bid.setUser(user);
        bid.setCar(car);
        bid.setAmount(BigDecimal.valueOf(amount));
        bid.setStatus("PENDING");
        return bidRepository.save(bid);
    }

    public Bid placeInitialBid(Long carId, User user, double amount) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        Bid bid = new Bid();
        bid.setUser(user);
        bid.setCar(car);
        bid.setAmount(BigDecimal.valueOf(amount));
        bid.setStatus("PENDING");
        System.out.println("Initial bid placed for car ID: " + carId + " with amount: " + amount);
        return bidRepository.save(bid);
    }

    public void acceptBid(Long bidId) {
        Bid bid = bidRepository.findById(bidId).orElseThrow(() -> new RuntimeException("Bid not found"));
        bid.setStatus("ACCEPTED");
        bidRepository.save(bid);
        Car car = bid.getCar();
        car.setPrice(bid.getAmount()); // Update car price to bid amount
        car.setActive(true); // Make car visible
        carRepository.save(car);
        System.out.println("Bid ID: " + bidId + " accepted, car ID: " + car.getId() + " set to active with price: " + car.getPrice());
    }

    public void rejectBid(Long bidId) {
        Bid bid = bidRepository.findById(bidId).orElseThrow(() -> new RuntimeException("Bid not found"));
        bid.setStatus("REJECTED");
        bidRepository.save(bid);
        Car car = bid.getCar();
        car.setActive(false); // Ensure car is not visible
        carRepository.save(car);
        System.out.println("Bid ID: " + bidId + " rejected, car ID: " + car.getId() + " remains inactive");
    }

    public List<Bid> getPendingBids() {
        return bidRepository.findByStatus("PENDING");
    }

    public List<Bid> getAllBids() {
        return bidRepository.findByStatusIn(List.of("PENDING", "ACCEPTED", "REJECTED"));
    }
}