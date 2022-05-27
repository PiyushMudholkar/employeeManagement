package com.nphc.employeeManagement.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartDateValidator.class)
@Documented
public @interface StartDate {

    String message() default "Invalid date";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
