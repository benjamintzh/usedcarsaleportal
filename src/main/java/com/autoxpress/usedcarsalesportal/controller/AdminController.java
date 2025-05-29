package com.autoxpress.usedcarsalesportal.controller;

import com.autoxpress.usedcarsalesportal.dto.UserRegistrationDto;
import com.autoxpress.usedcarsalesportal.service.AppointmentService;
import com.autoxpress.usedcarsalesportal.service.BidService;
import com.autoxpress.usedcarsalesportal.service.UserService;
import com.autoxpress.usedcarsalesportal.service.CarService;
import com.autoxpress.usedcarsalesportal.model.Appointment;
import com.autoxpress.usedcarsalesportal.model.Bid;
import com.autoxpress.usedcarsalesportal.model.Car;
import com.autoxpress.usedcarsalesportal.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private BidService bidService;

    @Autowired
    private CarService carService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments()
                .stream()
                .sorted((a1, a2) -> a2.getAppointmentDate().compareTo(a1.getAppointmentDate()))
                .limit(5)
                .toList());
        model.addAttribute("bids", bidService.getAllBids()
                .stream()
                .sorted((b1, b2) -> b2.getAmount().compareTo(b1.getAmount()))
                .limit(5)
                .toList());
        model.addAttribute("cars", carService.getAllCars()
                .stream()
                .sorted((c1, c2) -> c2.getId().compareTo(c1.getId()))
                .limit(5)
                .toList());
        return "dashboard";
    }

    @GetMapping("/manage-users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "manage-users";
    }

    @PostMapping("/users/make-admin/{id}")
    public String makeAdmin(@PathVariable Long id) {
        userService.makeAdmin(id);
        return "redirect:/admin/manage-users";
    }

    @GetMapping("/manage-appointments")
    public String listAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        model.addAttribute("usernames", userService.getDistinctUsernamesWithAppointments());
        return "manage-appointments";
    }

    @GetMapping("/manage-bids")
    public String listBids(Model model) {
        model.addAttribute("bids", bidService.getAllBids());
        return "manage-bids";
    }

    @GetMapping("/manage-cars")
    public String listCars(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("manufacturers", carService.getDistinctManufacturers());
        return "manage-cars";
    }

    @GetMapping("/appointment/{id}")
    public String appointmentDetails(@PathVariable Long id, Model model) {
        Appointment appointment = appointmentService.getAllAppointments()
                .stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        model.addAttribute("appointment", appointment);
        return "appointment-details";
    }

    @PostMapping("/appointment/set-status/{id}")
    public String setAppointmentStatus(@PathVariable Long id, @RequestParam("status") String status) {
        appointmentService.setAppointmentStatus(id, status);
        return "redirect:/admin/appointment/" + id;
    }

    @GetMapping("/bid/{id}")
    public String bidDetails(@PathVariable Long id, Model model) {
        Bid bid = bidService.getAllBids()
                .stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Bid not found"));
        model.addAttribute("bid", bid);
        return "bid-details";
    }

    @PostMapping("/bid/accept/{id}")
    public String acceptBid(@PathVariable Long id) {
        bidService.acceptBid(id);
        return "redirect:/admin/bid/" + id;
    }

    @PostMapping("/bid/reject/{id}")
    public String rejectBid(@PathVariable Long id) {
        bidService.rejectBid(id);
        return "redirect:/admin/bid/" + id;
    }

    @GetMapping("/car/{id}")
    public String carDetails(@PathVariable Long id, Model model) {
        Car car = carService.findById(id);
        model.addAttribute("car", car);
        return "car-details";
    }

    @PostMapping("/car/activate/{id}")
    public String activateCar(@PathVariable Long id) {
        carService.activateCar(id);
        return "redirect:/admin/car/" + id;
    }

    @PostMapping("/car/deactivate/{id}")
    public String deactivateCar(@PathVariable Long id) {
        carService.deactivateCar(id);
        return "redirect:/admin/car/" + id;
    }

    @GetMapping("/user/{id}")
    public String userDetails(@PathVariable Long id, Model model) {
        User user = userService.getAllUsers()
                .stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "user-details";
    }

    @PostMapping("/user/set-role/{id}")
    public String setUserRole(@PathVariable Long id, @RequestParam("role") String role) {
        userService.setRole(id, role);
        return "redirect:/admin/user/" + id;
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserRegistrationDto dto) {
        userService.updateUser(id, dto);
        return "redirect:/admin/user/" + id;
    }
}