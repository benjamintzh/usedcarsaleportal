package com.autoxpress.usedcarsalesportal.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.autoxpress.usedcarsalesportal.dto.AppointmentDto;
import com.autoxpress.usedcarsalesportal.dto.UserRegistrationDto;
import com.autoxpress.usedcarsalesportal.model.Appointment;
import com.autoxpress.usedcarsalesportal.model.Car;
import com.autoxpress.usedcarsalesportal.model.User;
import com.autoxpress.usedcarsalesportal.service.AppointmentService;
import com.autoxpress.usedcarsalesportal.service.CarService;
import com.autoxpress.usedcarsalesportal.service.EmailService;
import com.autoxpress.usedcarsalesportal.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserRegistrationDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        userService.saveUser(userDto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("user") UserRegistrationDto userDto, BindingResult result, Authentication authentication) {
        if (result.hasErrors()) {
            return "profile";
        }
        User user = userService.findByUsername(authentication.getName());
        userService.updateUser(user.getId(), userDto);
        return "redirect:/user/profile";
    }

    @GetMapping("/appointment")
    public String showAppointmentForm(Model model, @RequestParam(value = "carId", required = false) Long carId, Authentication authentication) {
        AppointmentDto appointmentDto = new AppointmentDto();
        if (carId != null) {
            Car car = carService.findById(carId);
            if (car != null) {
                appointmentDto.setCarId(carId);
                appointmentDto.setCarManufacture(car.getManufacture());
                appointmentDto.setCarModel(car.getModel());
            }
        }
        if (authentication != null) {
            User user = userService.findByUsername(authentication.getName());
            appointmentDto.setUsername(user.getUsername());
            appointmentDto.setEmail(user.getEmail());
            appointmentDto.setContactNumber(user.getContactNumber());
        }
        model.addAttribute("appointment", appointmentDto);
        model.addAttribute("showModal", false);
        return "appointment";
    }

    @PostMapping("/appointment")
    public String bookAppointment(@Valid @ModelAttribute("appointment") AppointmentDto appointmentDto, BindingResult result, Authentication authentication, Model model) {
        if (result.hasErrors()) {
            if (appointmentDto.getCarId() != null) {
                Car car = carService.findById(appointmentDto.getCarId());
                if (car != null) {
                    appointmentDto.setCarManufacture(car.getManufacture());
                    appointmentDto.setCarModel(car.getModel());
                } else {
                    result.rejectValue("carId", "error.carId", "Invalid car ID");
                }
            }
            model.addAttribute("showModal", false);
            return "appointment";
        }
        if (authentication == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(authentication.getName());
        Appointment appointment = appointmentService.bookAppointment(appointmentDto, user);
        try {
            emailService.sendBookingConfirmationEmail(user, appointmentDto, appointment.getId());
        } catch (Exception e) {
            // Continue to show modal even if email fails
        }
        model.addAttribute("appointment", appointmentDto);
        model.addAttribute("appointmentId", appointment.getId());
        model.addAttribute("showModal", true);
        return "appointment";
    }

    @GetMapping("/refresh")
    public String refreshPage() {
        return "redirect:/car/list";
    }
}
