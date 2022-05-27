package com.nphc.employeeManagement.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nphc.employeeManagement.exception.NPHCException;
import com.nphc.employeeManagement.model.CSVResponse;
import com.nphc.employeeManagement.model.EmployeeRequest;
import com.nphc.employeeManagement.model.EmployeeRequestParam;
import com.nphc.employeeManagement.model.EmployeeResponse;
import com.nphc.employeeManagement.model.EmployeesResponse;
import com.nphc.employeeManagement.repository.Employee;
import com.nphc.employeeManagement.service.EmployeeService;

@Validated
@RestController
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	@GetMapping("/users")
	public EmployeesResponse getUsers(@RequestParam(defaultValue = "0") Double minSalary,
			@RequestParam(defaultValue = "4000") Double maxSalary, @RequestParam(defaultValue = "0") int offset,
			@RequestParam(defaultValue = "0") int limit, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String order, @RequestParam(defaultValue = "") String filter)
			throws NPHCException {
		logger.info("In getUsers minSalary:{} maxSalary:{} , offset:{} , limit:{}, sortBy:{} , order:{}, filter:{}",
				minSalary, maxSalary, offset, limit, sortBy, order, filter);
		EmployeeRequestParam employeeRequestParam = new EmployeeRequestParam(minSalary, maxSalary, offset, limit,
				sortBy, order, filter);
		return employeeService.getEmployees(employeeRequestParam);

	}

	@GetMapping("/users/{id}")
	public Employee getUser(@PathVariable("id") String id) throws NPHCException {
		logger.info("In getUsers");
		return employeeService.getEmployee(id);

	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<EmployeeResponse> deleteUser(@PathVariable("id") String id) throws NPHCException {
		logger.info("In deleteUser");
		employeeService.deleteEmployee(id);
		return new ResponseEntity<>(new EmployeeResponse("Successfully deleted"), HttpStatus.OK);

	}

	@PostMapping("/users/upload")
	public ResponseEntity<EmployeeResponse> uploadFile(@RequestParam("file") MultipartFile file)
			throws NPHCException, IOException {
		logger.info("In uploadFile");
		CSVResponse csvResponse = employeeService.processFile(file);

		// No records updated case
		if (csvResponse.isSkipRecord() && !csvResponse.isUpdateRecord()) {
			return new ResponseEntity<>(new EmployeeResponse("Success but no data updated"), HttpStatus.OK);
		}
		return new ResponseEntity<>(new EmployeeResponse("Data created or uploaded"), HttpStatus.CREATED);

	}

	@PutMapping("/users")
	public ResponseEntity<EmployeeResponse> updateUser(@RequestBody EmployeeRequest employeeRequest)
			throws NPHCException {
		logger.info("In updateUser");
		employeeService.updateEmployee(employeeRequest);
		return new ResponseEntity<>(new EmployeeResponse("Successfully updated"), HttpStatus.CREATED);
	}

	@PatchMapping("/users")
	public ResponseEntity<EmployeeResponse> updateUserPatch(@RequestBody EmployeeRequest employeeRequest)
			throws NPHCException {
		logger.info("In updateUserPatch");
		employeeService.updateEmployee(employeeRequest);
		return new ResponseEntity<>(new EmployeeResponse("Successfully updated"), HttpStatus.CREATED);
	}
	
	@PostMapping("/users")
	public ResponseEntity<EmployeeResponse> createUser(@RequestBody @Valid EmployeeRequest employeeRequest)
			throws NPHCException {
		logger.info("In createUser");
		employeeService.createEmployee(employeeRequest);
		return new ResponseEntity<>(new EmployeeResponse("Successfully created"), HttpStatus.CREATED);
	}

}
