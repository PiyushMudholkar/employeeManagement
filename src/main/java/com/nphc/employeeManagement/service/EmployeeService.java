package com.nphc.employeeManagement.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.nphc.employeeManagement.exception.NPHCException;
import com.nphc.employeeManagement.model.CSVResponse;
import com.nphc.employeeManagement.model.EmployeeRequest;
import com.nphc.employeeManagement.model.EmployeeRequestParam;
import com.nphc.employeeManagement.model.EmployeesResponse;
import com.nphc.employeeManagement.repository.Employee;

public interface EmployeeService {

	public EmployeesResponse getEmployees(EmployeeRequestParam employeeRequestParam) throws NPHCException;

	public Employee getEmployee(String id) throws NPHCException;

	public void createEmployee(EmployeeRequest employeeRequest) throws NPHCException;

	public void deleteEmployee(String id) throws NPHCException;

	public void updateEmployee(EmployeeRequest employeeRequest) throws NPHCException;

	public CSVResponse processFile(MultipartFile file) throws NPHCException, IOException;

}
