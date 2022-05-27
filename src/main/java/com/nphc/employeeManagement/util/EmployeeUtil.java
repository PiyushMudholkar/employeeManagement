package com.nphc.employeeManagement.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.nphc.employeeManagement.model.EmployeeRequestParam;
import com.nphc.employeeManagement.repository.Employee;

public class EmployeeUtil {

//	public static Map<String,Comparator<Employee>> orderbyComparator = new HashMap<>();
//	
//	static {
//	
//	orderbyComparator.put("salary", Comparator.comparingDouble(Employee::getSalary));
//	orderbyComparator.put("login", Comparator.comparing(Employee::getLogin));
//	orderbyComparator.put("name", Comparator.comparing(Employee::getName));
//	orderbyComparator.put("id", Comparator.comparing(Employee::getId));
//	
//	}
//	public static Comparator<Employee> getComparator(EmployeeRequestParam employeeRequestParam) {
//		//assign default comparator by id 
//		Comparator<Employee> employeeComparator=orderbyComparator.get("id");
//		
//		// check if the comparator is there 
//		if(orderbyComparator.containsKey(employeeRequestParam.getSortBy().toLowerCase())) {
//		employeeComparator=orderbyComparator.get(employeeRequestParam.getSortBy().toLowerCase());
//		}
//		if(employeeRequestParam.getOrder().equalsIgnoreCase("dec"))
//			employeeComparator=employeeComparator.reversed();
//		
//		return employeeComparator;
//	}
//	
}
