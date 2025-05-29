package com.autoxpress.usedcarsalesportal.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.autoxpress.usedcarsalesportal.model.Car;
import com.autoxpress.usedcarsalesportal.model.User;
import com.autoxpress.usedcarsalesportal.service.BidService;
import com.autoxpress.usedcarsalesportal.service.CarService;
import com.autoxpress.usedcarsalesportal.service.UserService;
import com.autoxpress.usedcarsalesportal.dto.CarDto;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/car")
public class CarController {
	
	@Autowired
	private CarService carService;
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private BidService bidService;

	@GetMapping("/list")
    public String listCars(Model model) {
        model.addAttribute("cars", carService.findAllActiveCars());
        return "list";
    }

    @GetMapping("/search")
    public String searchCars(
        @RequestParam(required = false) String manufacture,
        @RequestParam(value = "model", required = false) String carModel, // Use value="model" to map form input
        @RequestParam(required = false, defaultValue = "0") int yearMin,
        @RequestParam(required = false, defaultValue = "0") int yearMax,
        @RequestParam(required = false, defaultValue = "0") double priceMin,
        @RequestParam(required = false, defaultValue = "0") double priceMax,
        Model model) {
        model.addAttribute("cars", carService.searchCars(manufacture, carModel, yearMin, yearMax, priceMin, priceMax));
        return "search";
    }

    @GetMapping("/details/{id}")
    public String carDetails(@PathVariable Long id, Model model) {
        model.addAttribute("car", carService.findById(id));
        return "details";
    }

    @GetMapping("/post")
    public String showCarPostForm(Model model) {
        model.addAttribute("car", new CarDto());
        return "post";
    }

    @PostMapping("/post")
    public String postCar(@Valid @ModelAttribute("car") CarDto carDto, BindingResult result, Authentication authentication) throws IOException {
        if (result.hasErrors()) {
            return "post";
        }
        User user = userService.findByUsername(authentication.getName());
        carService.saveCar(carDto, user);
        return "redirect:/car/list";
    }

    @GetMapping("/deactivate/{id}")
    public String deactivateCar(@PathVariable Long id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Car car = carService.findById(id);
        if (car.getUser().getId().equals(user.getId())) {
            carService.deactivateCar(id);
        }
        return "redirect:/car/list";
    }
    
    @PostMapping("/bid/{carId}")
    public String placeBid(@PathVariable Long carId, @RequestParam double amount, Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName());
        try {
            bidService.placeBid(carId, user, amount);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("car", carService.findById(carId));
            return "details"; // return to car details with error message
        }
        return "redirect:/car/details/" + carId;
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Authentication authentication) {
        Car car = carService.findById(id);
        User user = userService.findByUsername(authentication.getName());

        if (!car.getUser().getId().equals(user.getId())) {
            return "redirect:/car/list";
        }

        CarDto carDto = new CarDto();
        carDto.setManufacture(car.getManufacture());
        carDto.setModel(car.getModel());
        carDto.setRegistrationYear(car.getRegistrationYear());
        carDto.setPrice(car.getPrice().doubleValue());
        model.addAttribute("car", carDto);
        model.addAttribute("id", id);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCar(@PathVariable Long id,
                            @Valid @ModelAttribute("car") CarDto carDto,
                            BindingResult result,
                            Authentication authentication,
                            Model model) throws IOException {
        if (result.hasErrors()) {
            return "edit";
        }

        User user = userService.findByUsername(authentication.getName());
        Car existingCar = carService.findById(id);

        if (!existingCar.getUser().getId().equals(user.getId())) {
            return "redirect:/car/list";
        }

        carService.updateCar(id, carDto);
        return "redirect:/car/list";
    }
}
