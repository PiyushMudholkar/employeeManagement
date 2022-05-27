package com.nphc.employeeManagement.model;

import java.util.List;

import com.nphc.employeeManagement.repository.Employee;

public class EmployeesResponse {

	private List<Employee> results;

	public List<Employee> getResults() {
		return results;
	}

	public void setResults(List<Employee> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return String.format("EmployeesResponse [results=%s]", results);
	}
	
	
}
