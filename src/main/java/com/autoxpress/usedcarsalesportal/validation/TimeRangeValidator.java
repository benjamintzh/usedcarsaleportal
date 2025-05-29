package com.autoxpress.usedcarsalesportal.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeRangeValidator implements ConstraintValidator<TimeRange, LocalDateTime> {
    private int startHour;
    private int endHour;

    @Override
    public void initialize(TimeRange constraintAnnotation) {
        this.startHour = constraintAnnotation.startHour();
        this.endHour = constraintAnnotation.endHour();
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let @NotNull handle null checks
        }
        LocalTime time = value.toLocalTime();
        LocalTime start = LocalTime.of(startHour, 0);
        LocalTime end = LocalTime.of(endHour, 0);
        return !time.isBefore(start) && !time.isAfter(end);
    }
}