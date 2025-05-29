package com.autoxpress.usedcarsalesportal.service;

import com.autoxpress.usedcarsalesportal.dto.CarDto;
import com.autoxpress.usedcarsalesportal.model.Car;
import com.autoxpress.usedcarsalesportal.model.User;
import com.autoxpress.usedcarsalesportal.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BidService bidService;

    private final String SOURCE_UPLOAD_DIR;
    private final String TARGET_UPLOAD_DIR;

    public CarService() {
        String projectRoot = System.getProperty("user.dir");
        SOURCE_UPLOAD_DIR = projectRoot + "/src/main/resources/static/images/";
        TARGET_UPLOAD_DIR = projectRoot + "/target/classes/static/images/";
    }

    public Car saveCar(CarDto dto, User user) throws IOException {
        Car car = new Car();
        car.setManufacture(dto.getManufacture());
        car.setModel(dto.getModel());
        car.setRegistrationYear(dto.getRegistrationYear());
        car.setPrice(BigDecimal.valueOf(dto.getPrice()));
        car.setUser(user);
        car.setActive(false);

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            String imagePath = saveImage(dto.getImage());
            if (imagePath != null) {
                car.setImagePath(imagePath);
                System.out.println("Image path set for car: " + imagePath);
            } else {
                car.setImagePath("/images/default-car.jpg");
                System.out.println("Failed to save image, using default: " + dto.getImage().getOriginalFilename());
            }
        } else {
            car.setImagePath("/images/default-car.jpg");
            System.out.println("No image uploaded, using default image");
        }

        Car savedCar = carRepository.save(car);
        System.out.println("Car saved with ID: " + savedCar.getId() + ", imagePath: " + savedCar.getImagePath());

        bidService.placeInitialBid(savedCar.getId(), user, dto.getPrice());
        System.out.println("Initial bid created for car ID: " + savedCar.getId() + " with price: " + dto.getPrice());

        return savedCar;
    }

    public void deactivateCar(Long carId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));
        car.setActive(false);
        carRepository.save(car);
        System.out.println("Car ID: " + carId + " deactivated");
    }

    public void activateCar(Long carId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));
        car.setActive(true);
        carRepository.save(car);
        System.out.println("Car ID: " + carId + " activated");
    }

    public List<Car> searchCars(String manufacture, String model, int yearMin, int yearMax, double priceMin, double priceMax) {
        String manufactureSearch = manufacture != null && !manufacture.isEmpty() ? manufacture : "";
        String modelSearch = model != null && !model.isEmpty() ? model : "";
        int minYear = yearMin > 0 ? yearMin : 0;
        int maxYear = yearMax > 0 ? yearMax : Integer.MAX_VALUE;
        double minPrice = priceMin > 0 ? priceMin : 0.0;
        double maxPrice = priceMax > 0 ? priceMax : Double.MAX_VALUE;

        return carRepository.findByManufactureContainingIgnoreCaseAndModelContainingIgnoreCaseAndRegistrationYearBetweenAndPriceBetweenAndActiveWithApprovedBid(
                manufactureSearch, modelSearch, minYear, maxYear, minPrice, maxPrice);
    }

    public List<Car> findAllActiveCars() {
        return carRepository.findAllActiveWithApprovedBid();
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car findById(Long id) {
        return carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found"));
    }

    public List<String> getDistinctManufacturers() {
        return carRepository.findDistinctManufacturers();
    }

    private String saveImage(MultipartFile file) {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path sourcePath = Paths.get(SOURCE_UPLOAD_DIR, fileName);
        Path targetPath = Paths.get(TARGET_UPLOAD_DIR, fileName);

        try {
            Set<PosixFilePermission> perms = new HashSet<>();
            perms.add(PosixFilePermission.OWNER_READ);
            perms.add(PosixFilePermission.OWNER_WRITE);
            perms.add(PosixFilePermission.GROUP_READ);
            perms.add(PosixFilePermission.OTHERS_READ);

            Files.createDirectories(sourcePath.getParent());
            File sourceFile = sourcePath.toFile();
            file.transferTo(sourceFile);

            try {
                Files.setPosixFilePermissions(sourcePath, perms);
            } catch (UnsupportedOperationException e) {
                sourceFile.setReadable(true, false);
                sourceFile.setWritable(true, false);
            }

            Files.createDirectories(targetPath.getParent());
            Files.copy(sourcePath, targetPath);

            File targetFile = targetPath.toFile();
            try {
                Files.setPosixFilePermissions(targetPath, perms);
            } catch (UnsupportedOperationException e) {
                targetFile.setReadable(true, false);
                targetFile.setWritable(true, false);
            }

            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(targetFile, true)) {
                fos.getFD().sync();
            }

            int retries = 5;
            long delayMs = 100;
            for (int i = 0; i < retries; i++) {
                if (Files.exists(targetPath) && Files.isReadable(targetPath)) {
                    File sourceParentDir = sourcePath.getParent().toFile();
                    File targetParentDir = targetPath.getParent().toFile();
                    sourceParentDir.setLastModified(System.currentTimeMillis());
                    targetParentDir.setLastModified(System.currentTimeMillis());

                    if (System.getProperty("os.name").toLowerCase().contains("win")) {
                        try {
                            Runtime.getRuntime().exec("cmd /c dir \"" + targetParentDir.getAbsolutePath() + "\"");
                            Runtime.getRuntime().exec("cmd /c dir \"" + sourceParentDir.getAbsolutePath() + "\"");
                        } catch (IOException e) {
                            System.out.println("Failed to refresh directory on Windows: " + e.getMessage());
                        }
                    }

                    System.out.println("Image successfully saved to source: " + sourcePath.toAbsolutePath());
                    System.out.println("Image successfully saved to target: " + targetPath.toAbsolutePath());
                    return "/images/" + fileName;
                }
                Thread.sleep(delayMs);
            }

            System.out.println("Failed to verify image save to target: " + targetPath.toAbsolutePath() + " after " + retries + " retries");
            return null;
        } catch (IOException | InterruptedException e) {
            System.out.println("Error saving image to " + sourcePath.toAbsolutePath() + " or " + targetPath.toAbsolutePath() + ": " + e.getMessage());
            return null;
        }
    }
    
    public Car updateCar(Long id, CarDto dto) throws IOException {
        Car car = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found"));
        car.setManufacture(dto.getManufacture());
        car.setModel(dto.getModel());
        car.setRegistrationYear(dto.getRegistrationYear());
        car.setPrice(BigDecimal.valueOf(dto.getPrice()));

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            String imagePath = saveImage(dto.getImage());
            if (imagePath != null) {
                car.setImagePath(imagePath);
            }
        }

        return carRepository.save(car);
    }
}