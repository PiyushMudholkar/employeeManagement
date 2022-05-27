package com.nphc.employeeManagement.service;

import static com.nphc.employeeManagement.util.CsvFunctionUtil.verifyAllDataPresent;
import static com.nphc.employeeManagement.util.CsvFunctionUtil.verifyAndReturnDate;
import static com.nphc.employeeManagement.util.CsvFunctionUtil.verifyAndReturnSalary;
import static com.nphc.employeeManagement.util.CsvFunctionUtil.verifyDuplicateEmployeeByID;
import static com.nphc.employeeManagement.util.CsvFunctionUtil.verifyDuplicateEmployeeByLogin;
import static com.nphc.employeeManagement.util.CsvFunctionUtil.verifyFileNaming;
import static com.nphc.employeeManagement.util.CsvFunctionUtil.verifyHeaderLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nphc.employeeManagement.exception.NPHCException;
import com.nphc.employeeManagement.model.CSVResponse;
import com.nphc.employeeManagement.model.EmployeeRequest;
import com.nphc.employeeManagement.model.EmployeeRequestParam;
import com.nphc.employeeManagement.model.EmployeesResponse;
import com.nphc.employeeManagement.repository.Employee;
import com.nphc.employeeManagement.repository.EmployeeRepository;
import com.nphc.employeeManagement.util.DateUtil;
import com.nphc.employeeManagement.util.EmployeeHelperUtil;
import com.nphc.employeeManagement.util.NPHCConstants;
import com.nphc.employeeManagement.validator.FindEmployee;
import com.nphc.employeeManagement.validator.UniqueEmployee;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	@Autowired
	private EmployeeHelperUtil employeeHelperUtil;

	@Autowired
	private EmployeeRepository Employeerepository;

	Predicate<String> isEmployeeIdExists = id -> Employeerepository.findById(id).isPresent();

	Predicate<String> isEmployeeLoginExists = login -> Employeerepository.findByLogin(login).isPresent();

	UniqueEmployee isEmployeeIdUnique = id -> {
		if (Employeerepository.findById(id).isPresent()) {
			throw new NPHCException("Employee ID already exists");
		} else {
			return true;
		}
	};

	UniqueEmployee isEmployeeLoginUnique = login -> {
		if (Employeerepository.findByLogin(login).isPresent()) {
			throw new NPHCException("Employee login not unique");
		} else {
			return true;
		}
	};

	FindEmployee findByIdElseThrowException = (id, errorMessage) -> {
		Optional<Employee> employeedb = Employeerepository.findById(id);
		if (employeedb.isPresent()) {
			return employeedb.get();
		} else {
			throw new NPHCException(errorMessage);
		}
	};


	@Override
	public EmployeesResponse getEmployees(EmployeeRequestParam employeeRequestParam) throws NPHCException {
		logger.info("start of getEmployees finding with parameters {} ", employeeRequestParam);

		employeeHelperUtil.verifySortBy(employeeRequestParam);
		EmployeesResponse employeesResponse = new EmployeesResponse();
		List<Employee> employeeList = Employeerepository.findAll();

		employeeList = employeeList.stream()
				.filter(e -> e.getSalary() >= employeeRequestParam.getMinSalary()
						&& e.getSalary() < employeeRequestParam.getMaxSalary())
				.filter(e -> employeeHelperUtil.filter(e, employeeRequestParam))
				.sorted(employeeHelperUtil.getComparator(employeeRequestParam)).skip(employeeRequestParam.getOffset())
				.limit(employeeRequestParam.getLimit()).collect(Collectors.toList());
		employeesResponse.setResults(employeeList);

		logger.info("After appling all filter rules employees are {} ", employeeList);
		return employeesResponse;
	}

	@Override
	public Employee getEmployee(String id) throws NPHCException {
		logger.info("start of getEmployee");
		return findByIdElseThrowException.find(id, NPHCConstants.EMPLOYEE_NOT_PRESENT);
	}

	@Override
	public void createEmployee(EmployeeRequest employeeRequest) throws NPHCException {
		logger.info("start of createEmployee");
		isEmployeeIdUnique.validate(employeeRequest.getId());
		isEmployeeLoginUnique.validate(employeeRequest.getLogin());
		Employee employee = new Employee();
		mapAndstoreEmployee(employee, employeeRequest);
	}

	@Override
	public void deleteEmployee(String id) throws NPHCException {
		logger.info("start of deleteEmployee");

		Employeerepository.delete(findByIdElseThrowException.find(id, NPHCConstants.NO_SUCH_EMPLOYEE));
	}

	@Override
	public void updateEmployee(EmployeeRequest employeeRequest) throws NPHCException {
		logger.info("start of updateEmployee");
		Employee employee = findByIdElseThrowException.find(employeeRequest.getId(), NPHCConstants.NO_SUCH_EMPLOYEE);
		isEmployeeLoginUnique.validate(employeeRequest.getLogin());

		mapAndstoreEmployee(employee, employeeRequest);

	}

	@Override
	public CSVResponse processFile(MultipartFile file) throws NPHCException, IOException {
		logger.info("start of processFile");

		verifyFileNaming(file);

		InputStream inputFS = file.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
		String line = "";
		String splitBy = ",";
		Set<String> employeeIdSet = new HashSet<>();
		Set<String> employeeLoginSet = new HashSet<>();
		// verify header line
		verifyHeaderLine(br.readLine());
		List<Employee> employeeList = new ArrayList<>();
		// start reading the file
		while ((line = br.readLine()) != null) {
			if (!line.startsWith("#")) {
				String[] eRow = line.split(splitBy);

				verifyAllDataPresent(eRow, line);

				Employee employee = new Employee();
				// validation
				verifyDuplicateEmployeeByID(eRow[0], employeeIdSet);
				verifyDuplicateEmployeeByLogin(eRow[1], employeeLoginSet);

				// assign values to employee
				employee.setId(eRow[0]);
				employee.setLogin(eRow[1]);
				employee.setName(eRow[2]);
				employee.setSalary(verifyAndReturnSalary(eRow[3]));
				employee.setStartDate(verifyAndReturnDate(eRow[4]));
				employeeList.add(employee);
			}
		}
		logger.info("end of processFile");
		return persistEmployeeInDB(employeeList);

	}

	private CSVResponse persistEmployeeInDB(List<Employee> employeeList) {
		List<Employee> nonPersistedEmployee = new ArrayList<Employee>();
		logger.info("start of persistEmployeeInDB");
		CSVResponse csvResponse = new CSVResponse();
		for (Employee e : employeeList) {
			// if login already exists then no need to update data
			if (isEmployeeLoginExists.test(e.getLogin())) {
				csvResponse.setSkipRecord(true);
				nonPersistedEmployee.add(e);
				continue;
			}
			;
			csvResponse.setUpdateRecord(true); // if any of data is created or updated setting it to true
			logger.info("Saving data entry to DB {} ", e);
			Employeerepository.save(e);
		}
		logger.info("end of persistEmployeeInDB");
		return csvResponse;

	}

	private void mapAndstoreEmployee(Employee employee, EmployeeRequest employeeRequest) throws NPHCException {
		employee.setId(employeeRequest.getId());
		employee.setLogin(employeeRequest.getLogin());
		employee.setName(employeeRequest.getName());
		employee.setSalary(employeeRequest.getSalary());
		employee.setStartDate(DateUtil.convertStringToDate(employeeRequest.getStartDate()));

		Employeerepository.save(employee);
	}

}
