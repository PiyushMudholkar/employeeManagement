package com.nphc.employeeManagement.controller;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nphc.employeeManagement.exception.NPHCException;
import com.nphc.employeeManagement.model.EmployeeResponse;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { NPHCException.class })
	public ResponseEntity<EmployeeResponse> handleMethodArgumentNotValid(NPHCException ex) {
		System.out.println("NPHCException is " + ex.getMessage());
		return new ResponseEntity<>(new EmployeeResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
	}

	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println("handleMethodArgumentNotValid is " + ex.getMessage());

		return new ResponseEntity<>(new EmployeeResponse(ex.getBindingResult().getFieldError().getDefaultMessage()),
				headers, status);
	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
		System.out.println("ConstraintViolationException is " + e.getMessage());
		return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.CONFLICT);
	}

}
