package com.nphc.employeeManagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nphc.employeeManagement.exception.NPHCException;
import com.nphc.employeeManagement.model.CSVResponse;
import com.nphc.employeeManagement.model.EmployeeRequest;
import com.nphc.employeeManagement.model.EmployeeRequestParam;
import com.nphc.employeeManagement.model.EmployeesResponse;
import com.nphc.employeeManagement.repository.Employee;
import com.nphc.employeeManagement.service.EmployeeService;

@WebMvcTest
public class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@InjectMocks
	private EmployeeController employeeController;

	@MockBean
	private EmployeeService employeeService;

	private static ObjectMapper mapper = new ObjectMapper();

	private EmployeeRequest employeeRequest;
	private Employee employee;
	private CSVResponse csvResponse;
	private EmployeesResponse employeesResponse;

	@BeforeEach
	public void setup() {
		employeeRequest = new EmployeeRequest();
		employeeRequest.setId("e0001");
		employeeRequest.setLogin("Piyush");
		employeeRequest.setName("Piyush mudholkar");
		employeeRequest.setSalary(12345.23);
		employeeRequest.setStartDate("2020-04-25");

		employee = new Employee();
		employee.setId("e0001");
		employee.setLogin("Piyush");
		employee.setName("Piyush mudholkar");
		employee.setSalary(12345.23);
		employee.setStartDate(LocalDate.now());

		csvResponse = new CSVResponse();

		Employee employee1 = new Employee();
		employee1.setId("e0001");
		employee1.setLogin("Piyush");
		employee1.setName("Piyush mudholkar");
		employee1.setSalary(111.11);
		employee1.setStartDate(LocalDate.now());

		Employee employee2 = new Employee();
		employee2.setId("e0002");
		employee2.setLogin("Piyush2");
		employee2.setName("Piyush mudholkar3");
		employee2.setSalary(222.22);
		employee2.setStartDate(LocalDate.now());

		Employee employee3 = new Employee();
		employee3.setId("e0003");
		employee3.setLogin("Piyush3");
		employee3.setName("Piyush mudholkar3");
		employee3.setSalary(333.33);
		employee3.setStartDate(LocalDate.now());

		ArrayList<Employee> employeeList = new ArrayList();
		employeeList.add(employee1);
		employeeList.add(employee2);
		employeeList.add(employee3);
		employeesResponse = new EmployeesResponse();
		employeesResponse.setResults(employeeList);
	}

	@Test
	public void testGetUsers() throws Exception {
		Mockito.when(employeeService.getEmployee(any(String.class))).thenReturn(employee);

		mockMvc.perform(get("/users/e0001").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.login", Matchers.equalTo("Piyush")))
				.andExpect(jsonPath("$.id", Matchers.equalTo("e0001")));
	}

	@Test
	public void testGetUser() throws Exception {
		Mockito.when(employeeService.getEmployees(any(EmployeeRequestParam.class))).thenReturn(employeesResponse);

		mockMvc.perform(get("/users?limit=3").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.results.[0].login", Matchers.equalTo("Piyush")))
				.andExpect(jsonPath("$.results.[1].id", Matchers.equalTo("e0002")))
				.andExpect(jsonPath("$.results.[2].salary", Matchers.equalTo(333.33)));
	}

	@Test
	public void testDeleteUser() throws Exception {
		Mockito.doNothing().when(employeeService).deleteEmployee(any(String.class));

		mockMvc.perform(delete("/users/e0001").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Successfully deleted")));
	}

	@Test
	public void testUploadFile_NoDataUpdated() throws Exception {
		Mockito.when(employeeService.processFile(any(MultipartFile.class))).thenReturn(csvResponse);

		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "employee.csv", "multipart/form-data",
				"some xml".getBytes());
		mockMvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(mockMultipartFile)
				.contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.message", Matchers.equalTo("Success but no data updated")));

	}

	@Test
	public void testUploadFile_DataCreated() throws Exception {
		Mockito.when(employeeService.processFile(any(MultipartFile.class))).thenReturn(csvResponse);
		csvResponse.setSkipRecord(false);

		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "employee.csv", "multipart/form-data",
				"some xml".getBytes());

		mockMvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(mockMultipartFile)
				.contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(MockMvcResultMatchers.status().is(201))
				.andExpect(jsonPath("$.message", Matchers.equalTo("Data created or uploaded")));

	}

	@Test
	public void testUploadFile_DuplicateId() throws Exception {
		Mockito.when(employeeService.processFile(any(MultipartFile.class)))
				.thenThrow(new NPHCException("Duplicate id in the file e0001"));

		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "employee.csv", "multipart/form-data",
				"some xml".getBytes());
		mockMvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(mockMultipartFile)
				.contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(MockMvcResultMatchers.status().is(400))
				.andExpect(jsonPath("$.message", Matchers.equalTo("Duplicate id in the file e0001")));

	}

	@Test
	public void testUpdateUserPut() throws Exception {
		Mockito.doNothing().when(employeeService).updateEmployee(any(EmployeeRequest.class));

		String json = mapper.writeValueAsString(employeeRequest);
		mockMvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Successfully updated")));
	}

	@Test
	public void testUpdateUserPatch() throws Exception {
		Mockito.doNothing().when(employeeService).updateEmployee(any(EmployeeRequest.class));

		String json = mapper.writeValueAsString(employeeRequest);
		mockMvc.perform(patch("/users").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Successfully updated")));
	}

	@Test
	public void testCreateUser() throws Exception {

		Mockito.doNothing().when(employeeService).createEmployee(any(EmployeeRequest.class));

		String json = mapper.writeValueAsString(employeeRequest);
		mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Successfully created")));

	}

	@Test
	public void testCreateUser_ValidationFailureWithInvalidDate() throws Exception {

		Mockito.doNothing().when(employeeService).createEmployee(any(EmployeeRequest.class));

		// set invalid date
		employeeRequest.setStartDate("2020-22-25");

		String json = mapper.writeValueAsString(employeeRequest);
		mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Invalid date")));

	}

	@Test
	public void testCreateUser_ValidationFailureForUniqueID() throws Exception {

		Mockito.doThrow(new NPHCException("Employee ID already exists")).when(employeeService)
				.createEmployee(any(EmployeeRequest.class));

		String json = mapper.writeValueAsString(employeeRequest);
		mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Employee ID already exists")));

	}

	@Test
	public void testCreateUser_ValidationFailureForUniqueLogin() throws Exception {

		Mockito.doThrow(new NPHCException("Employee login not unique")).when(employeeService)
				.createEmployee(any(EmployeeRequest.class));

		String json = mapper.writeValueAsString(employeeRequest);
		mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Employee login not unique")));

	}

	@Test
	public void testCreateUser_ValidationFailureForSalary() throws Exception {

		Mockito.doThrow(new NPHCException("Invalid salary")).when(employeeService)
				.createEmployee(any(EmployeeRequest.class));

		// set invalid salary
		employeeRequest.setSalary(-1234.00);

		String json = mapper.writeValueAsString(employeeRequest);
		mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Invalid salary")));

	}

}
