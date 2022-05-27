package com.nphc.employeeManagement.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.nphc.employeeManagement.validator.StartDate;

public class EmployeeRequest {

	private String id;
	private String login;
	@NotBlank(message = "Name cannot be blank")
	private String name;
	
	@NotNull(message = "Invalid salary")
    @DecimalMin(value = "0.0", inclusive = true ,message="Invalid salary")
	private Double salary;
	
	//@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	//@JsonFormat(pattern = "yyyy-MM-dd")
	//private LocalDate startDate;
	
	@StartDate(message = "Invalid date")
	private String startDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getSalary() {
		return salary;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	@Override
	public String toString() {
		return String.format("EmployeeRequest [id=%s, login=%s, name=%s, salary=%s, startDate=%s]", id, login, name, salary,
				startDate);
	}
	
	
	
	
}
