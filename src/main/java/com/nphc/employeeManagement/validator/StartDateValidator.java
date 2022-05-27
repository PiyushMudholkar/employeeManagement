package com.nphc.employeeManagement.validator;

import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.nphc.employeeManagement.util.DateUtil;

public class StartDateValidator implements ConstraintValidator<StartDate, String> {


	public boolean isValid(String value, ConstraintValidatorContext context) {

		try {
			DateUtil.dateTimeFormatter.parse(value);
			return true;
		} catch (DateTimeParseException ex) {
			return false;
		}

	}

}
