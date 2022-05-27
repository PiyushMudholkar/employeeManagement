package com.nphc.employeeManagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import com.nphc.employeeManagement.exception.NPHCException;
import com.nphc.employeeManagement.model.CSVResponse;
import com.nphc.employeeManagement.model.EmployeeRequest;
import com.nphc.employeeManagement.model.EmployeeRequestParam;
import com.nphc.employeeManagement.model.EmployeesResponse;
import com.nphc.employeeManagement.repository.Employee;
import com.nphc.employeeManagement.repository.EmployeeRepository;
import com.nphc.employeeManagement.util.EmployeeHelperUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration
public class EmployeeServiceImplTest {

	@TestConfiguration
	static class TestConfig {

		@Bean
		public Map<String, Comparator<Employee>> getSortByComparator() {
			Map<String, Comparator<Employee>> sortByComparator = new HashMap<>();
			;
			sortByComparator.put("salary", Comparator.comparingDouble(Employee::getSalary));
			sortByComparator.put("login", Comparator.comparing(Employee::getLogin));
			sortByComparator.put("name", Comparator.comparing(Employee::getName));
			sortByComparator.put("id", Comparator.comparing(Employee::getId));
			return sortByComparator;
		}

	}

	@Mock
	private EmployeeRepository employeeRepository;

	@Autowired
	private Map<String, Comparator<Employee>> sortByComparator;

	@Mock
	private EmployeeHelperUtil employeeHelperUtil;

	@InjectMocks
	// @MockBean
	private EmployeeServiceImpl employeeService;

	private EmployeeRequest employeeRequest;
	private Employee employee;
	private CSVResponse csvResponse;
	private EmployeesResponse employeesResponse;
	ArrayList<Employee> employeeList;

	private Double minSalary;
	private Double maxSalary;
	private int offset;
	private int limit;
	private String sortBy;
	private String order;
	private String filter;

	private EmployeeRequestParam employeeRequestParam;

	@Before
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

		employeeList = new ArrayList();
		employeeList.add(employee1);
		employeeList.add(employee2);
		employeeList.add(employee3);
		employeesResponse = new EmployeesResponse();
		employeesResponse.setResults(employeeList);

		minSalary = 0.0;
		maxSalary = 4000.0;
		offset = 0;
		limit = 2;
		sortBy = "id";
		order = "asc";
		filter = "";
		employeeRequestParam = new EmployeeRequestParam(minSalary, maxSalary, offset, limit, sortBy, order, filter);
	}

	@Test
	public void testGetEmployees_withLimit2() throws NPHCException {

		doNothing().when(employeeHelperUtil).verifySortBy(any(EmployeeRequestParam.class));
		when(employeeHelperUtil.filter(any(Employee.class), any(EmployeeRequestParam.class))).thenCallRealMethod();
		when(employeeHelperUtil.getComparator(any(EmployeeRequestParam.class)))
				.thenReturn(sortByComparator.get(employeeRequestParam.getSortBy()));

		when(employeeRepository.findAll()).thenReturn(employeeList);

		EmployeesResponse employeesResponse = employeeService.getEmployees(employeeRequestParam);

		assertThat(employeesResponse.getResults().size()).isEqualTo(2);

	}

	@Test
	public void testGetEmployees_withFilter() throws NPHCException {
		employeeRequestParam.setFilter("111");
		doNothing().when(employeeHelperUtil).verifySortBy(any(EmployeeRequestParam.class));
		when(employeeHelperUtil.filter(any(Employee.class), any(EmployeeRequestParam.class))).thenCallRealMethod();
		when(employeeHelperUtil.getComparator(any(EmployeeRequestParam.class)))
				.thenReturn(sortByComparator.get(employeeRequestParam.getSortBy()));

		when(employeeRepository.findAll()).thenReturn(employeeList);

		EmployeesResponse employeesResponse = employeeService.getEmployees(employeeRequestParam);

		assertThat(employeesResponse.getResults().size()).isEqualTo(1);
		assertThat(employeesResponse.getResults().get(0).getSalary()).isEqualTo(111.11);

	}

	@Test
	public void testGetEmployees_withSortBylogin() throws NPHCException {
		employeeRequestParam.setLimit(3);
		employeeRequestParam.setSortBy("login");
		doNothing().when(employeeHelperUtil).verifySortBy(any(EmployeeRequestParam.class));
		when(employeeHelperUtil.filter(any(Employee.class), any(EmployeeRequestParam.class))).thenCallRealMethod();
		when(employeeHelperUtil.getComparator(any(EmployeeRequestParam.class)))
				.thenReturn(sortByComparator.get(employeeRequestParam.getSortBy()));

		when(employeeRepository.findAll()).thenReturn(employeeList);

		EmployeesResponse employeesResponse = employeeService.getEmployees(employeeRequestParam);

		assertThat(employeesResponse.getResults().size()).isEqualTo(3);
		assertThat(employeesResponse.getResults().get(0).getLogin()).isEqualTo("Piyush");
		assertThat(employeesResponse.getResults().get(1).getLogin()).isEqualTo("Piyush2");
		assertThat(employeesResponse.getResults().get(2).getLogin()).isEqualTo("Piyush3");

	}

	@Test
	public void testGetEmployee() throws NPHCException {
		Optional<Employee> eOptional = Optional.of(employee);
		when(employeeRepository.findById(any(String.class))).thenReturn(eOptional);
		Employee returnedEmployee = employeeService.getEmployee("e0001");
		assertThat(returnedEmployee.getId()).isEqualTo("e0001");
	}

	@Test(expected = NPHCException.class)
	public void testGetEmployee_throwException() throws NPHCException {
		Optional<Employee> eOptional = Optional.empty();
		when(employeeRepository.findById(any(String.class))).thenReturn(eOptional);
		Employee returnedEmployee = employeeService.getEmployee("e0001");
	}

	@Test(expected = NPHCException.class)
	public void testCreateEmployee_throwException() throws NPHCException {
		// employee with same id present in DB so will throw exception
		Optional<Employee> eOptional = Optional.of(employee);
		when(employeeRepository.findById(any(String.class))).thenReturn(eOptional);
		when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
		employeeService.createEmployee(employeeRequest);

	}

	@Test
	public void testCreateEmployee() throws NPHCException {
		// employee with same id not present in DB so set empty
		Optional<Employee> eOptional = Optional.empty();
		when(employeeRepository.findById(any(String.class))).thenReturn(eOptional);
		when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
		employeeService.createEmployee(employeeRequest);

		verify(employeeRepository, times(1)).findById(any(String.class));
		verify(employeeRepository, times(1)).save(any(Employee.class));

	}

	@Test
	public void testDeleteEmployee() throws NPHCException {
		// employee with same id present in DB so will delete the employee
		Optional<Employee> eOptional = Optional.of(employee);
		when(employeeRepository.findById(any(String.class))).thenReturn(eOptional);
		doNothing().when(employeeRepository).delete(any(Employee.class));
		employeeService.deleteEmployee("e0001");

		verify(employeeRepository, times(1)).findById(any(String.class));
		verify(employeeRepository, times(1)).delete(any(Employee.class));
	}

	@Test(expected = NPHCException.class)
	public void testDeleteEmployee_throwsException() throws NPHCException {
		// employee with same id not present in DB so will throw exception
		Optional<Employee> eOptional = Optional.empty();
		when(employeeRepository.findById(any(String.class))).thenReturn(eOptional);
		doNothing().when(employeeRepository).delete(any(Employee.class));
		employeeService.deleteEmployee("e0001");

	}

	@Test
	public void testUpdateEmployee() throws NPHCException {
		// employee with same id present in DB so will update the employee
		Optional<Employee> eOptional = Optional.of(employee);
		when(employeeRepository.findById(any(String.class))).thenReturn(eOptional);
		when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
		employeeService.updateEmployee(employeeRequest);

		verify(employeeRepository, times(1)).findById(any(String.class));
		verify(employeeRepository, times(1)).save(any(Employee.class));
	}

	@Test(expected = NPHCException.class)
	public void testUpdateEmployee_throwException() throws NPHCException {
		// employee with same id not present in DB so will throw exception
		Optional<Employee> eOptional = Optional.empty();
		when(employeeRepository.findById(any(String.class))).thenReturn(eOptional);
		when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
		employeeService.updateEmployee(employeeRequest);

	}

	@Test
	public void testProcessFile() throws NPHCException, IOException {
		when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
		// String filePath = "C:\\\\Users\\\\Piyush
		// Mudholkar\\\\eclipse-workspace\\\\employeeManagement\\\\src\\\\test\\\\resources\\\\employee.csv";
		Path resourcePath = Paths.get("src", "test", "resources", "employee.csv");
		String absolutePath = resourcePath.toFile().getAbsolutePath();
		byte[] bytes = Files.readAllBytes(Paths.get(absolutePath));
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "employee.csv", "multipart/form-data",
				bytes);

		CSVResponse csvResponse = employeeService.processFile(mockMultipartFile);

		assertThat(csvResponse.isUpdateRecord()).isEqualTo(true);
		assertThat(csvResponse.isSkipRecord()).isEqualTo(true);

	}

	@Test
	public void testProcessFile_DuplicateId() throws  IOException {
		when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
		Path resourcePath = Paths.get("src", "test", "resources", "employee_DuplicateID.csv");
		String absolutePath = resourcePath.toFile().getAbsolutePath();
		byte[] bytes = Files.readAllBytes(Paths.get(absolutePath));
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "employee_DuplicateID.csv",
				"multipart/form-data", bytes);
		try {
			CSVResponse csvResponse = employeeService.processFile(mockMultipartFile);
		} catch (NPHCException e) {
			assertThat(e.getMessage()).isEqualTo("Duplicate id in the file e0001");
		}


	}

	@Test
	public void testProcessFile_DuplicateLogin() throws  IOException {
		when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
		Path resourcePath = Paths.get("src", "test", "resources", "employee_DuplicateLogin.csv");
		String absolutePath = resourcePath.toFile().getAbsolutePath();
		byte[] bytes = Files.readAllBytes(Paths.get(absolutePath));
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "employee_DuplicateLogin.csv",
				"multipart/form-data", bytes);
		try {
			CSVResponse csvResponse = employeeService.processFile(mockMultipartFile);
		} catch (NPHCException e) {
			assertThat(e.getMessage()).isEqualTo("Duplicate login in the file hpotter");
		}

	}

	@Test
	public void testProcessFile_InvalidData() throws  IOException {
		when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
		Path resourcePath = Paths.get("src", "test", "resources", "employee_InvalidData.csv");
		String absolutePath = resourcePath.toFile().getAbsolutePath();
		byte[] bytes = Files.readAllBytes(Paths.get(absolutePath));
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "employee_InvalidData.csv",
				"multipart/form-data", bytes);
		try {
			CSVResponse csvResponse = employeeService.processFile(mockMultipartFile);
		} catch (NPHCException e) {
			assertThat(e.getMessage()).isEqualTo("row must contain all 5 records e0002,rwesley,");
		}

	}

}
