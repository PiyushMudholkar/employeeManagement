package com.nphc.employeeManagement.model;

public class EmployeeResponse {

	private String message;

	public EmployeeResponse() {
	}
	
	public EmployeeResponse(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return String.format("EmployeeResponse [message=%s]", message);
	}
	
}
