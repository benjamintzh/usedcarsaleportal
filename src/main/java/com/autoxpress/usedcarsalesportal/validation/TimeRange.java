package com.autoxpress.usedcarsalesportal.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TimeRangeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeRange {
    String message() default "Appointment time must be between 9:00 AM and 5:00 PM";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int startHour() default 9;
    int endHour() default 17;
}