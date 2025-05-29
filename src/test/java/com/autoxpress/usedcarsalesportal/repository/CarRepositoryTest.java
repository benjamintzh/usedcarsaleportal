package com.autoxpress.usedcarsalesportal.repository;

import com.autoxpress.usedcarsalesportal.model.Car;
import com.autoxpress.usedcarsalesportal.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    @Test
    void testFindByActiveTrue() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole("USER");
        entityManager.persist(user);

        Car car = new Car();
        car.setManufacture("Toyota");
        car.setModel("Camry");
        car.setRegistrationYear(2020);
        car.setPrice(new BigDecimal("20000"));
        car.setActive(true);
        car.setUser(user);
        entityManager.persist(car);

        List<Car> activeCars = carRepository.findByActiveTrue();
        assertEquals(1, activeCars.size());
        assertEquals("Toyota", activeCars.get(0).getManufacture());
    }

    @Test
    void testFindDistinctManufacturers() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole("USER");
        entityManager.persist(user);

        Car car1 = new Car();
        car1.setManufacture("Toyota");
        car1.setModel("Camry");
        car1.setRegistrationYear(2020);
        car1.setPrice(new BigDecimal("20000"));
        car1.setUser(user);
        entityManager.persist(car1);

        Car car2 = new Car();
        car2.setManufacture("Honda");
        car2.setModel("Civic");
        car2.setRegistrationYear(2021);
        car2.setPrice(new BigDecimal("22000"));
        car2.setUser(user);
        entityManager.persist(car2);

        List<String> manufacturers = carRepository.findDistinctManufacturers();
        assertEquals(2, manufacturers.size());
        assertTrue(manufacturers.contains("Toyota"));
        assertTrue(manufacturers.contains("Honda"));
    }
}