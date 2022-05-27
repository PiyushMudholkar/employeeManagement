package com.nphc.employeeManagement;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.nphc.employeeManagement.repository.Employee;

@SpringBootApplication
public class EmployeeManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagementApplication.class, args);
	}

	
	@Bean
	public Map<String,Comparator<Employee>> getSortByComparator() {
		Map<String,Comparator<Employee>> sortByComparator= new HashMap<>();;
		sortByComparator.put("salary", Comparator.comparingDouble(Employee::getSalary));
		sortByComparator.put("login", Comparator.comparing(Employee::getLogin));
		sortByComparator.put("name", Comparator.comparing(Employee::getName));
		sortByComparator.put("id", Comparator.comparing(Employee::getId));
		return sortByComparator;
	}
}
