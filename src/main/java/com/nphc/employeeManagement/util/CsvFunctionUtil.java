package com.nphc.employeeManagement.util;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nphc.employeeManagement.exception.NPHCException;

public class CsvFunctionUtil {

	static Logger logger = LoggerFactory.getLogger(CsvFunctionUtil.class);
	// predicates for csv file
	public static Predicate<String[]> checkAllFiveElements = row -> (row.length == 5);
	public static Predicate<String> checkNotNullOrBlank = data -> null != data && !data.isBlank() && !data.isEmpty();

	public static void verifyAllDataPresent(String[] employeeRow, String line) throws NPHCException {
		logger.info("row data is {} " , line);
		if (!checkAllFiveElements.test(employeeRow)) {
			throw new NPHCException("row must contain all 5 records " + line);
		}
		if (checkNotNullOrBlank.test(employeeRow[0]) && checkNotNullOrBlank.test(employeeRow[1])
				&& checkNotNullOrBlank.test(employeeRow[2]) && checkNotNullOrBlank.test(employeeRow[3])
				&& checkNotNullOrBlank.test(employeeRow[4])) {

		} else {
			throw new NPHCException("Row must contain all 5 non empty records " + line);
		}

	}

	public static void verifyDuplicateEmployeeByID(String id, Set<String> employeeIdSet) throws NPHCException {
		if (!employeeIdSet.add(id)) {
			throw new NPHCException("Duplicate id in the file " + id);
		}

	}

	public static void verifyDuplicateEmployeeByLogin(String login, Set<String> employeeLoginSet) throws NPHCException {
		if (!employeeLoginSet.add(login)) {
			throw new NPHCException("Duplicate login in the file " + login);
		}
	}

	public static Double verifyAndReturnSalary(String inputSalary) throws NPHCException {
		Double salary;
		try {
			salary = Double.parseDouble(inputSalary);
			if (!(salary >= 0.0)) {
				throw new NPHCException(String.format("Salary %s is not valid, salary must be >= 0.0 ", inputSalary));
			}
		} catch (NumberFormatException e) {
			throw new NPHCException("Salary is not valid, please enter valid salary " + inputSalary);
		}
		return salary;
	}

	public static LocalDate verifyAndReturnDate(String inputDate) throws NPHCException {
		return DateUtil.convertStringToDate(inputDate);
	}

	public static void verifyFileNaming(MultipartFile file) throws NPHCException {
		String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
		String fileExtension = "";
		System.out.println("originalFileName :" + originalFileName);
		// Check if the file's name contains invalid characters
		if (originalFileName.contains(".."))
			throw new NPHCException(
					"Sorry! Filename contains invalid path sequence uploaded file is : " + originalFileName);

		fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
		
		System.out.println("fileExtension :" + fileExtension);
		if (!".csv".equalsIgnoreCase(fileExtension))
			throw new NPHCException("Please upload file with .csv extension " + originalFileName);

	}

	public static void verifyHeaderLine(String headerLine) throws NPHCException {
		if (null != headerLine) {
			try {
				String[] headerdata = headerLine.split(",");
				verifyAllDataPresent(headerdata, headerLine);
				// id,login,name,salary,startDate
				if (!(headerdata[0].equalsIgnoreCase("id") && headerdata[1].equalsIgnoreCase("login")
						&& headerdata[2].equalsIgnoreCase("name") && headerdata[3].equalsIgnoreCase("salary")
						&& headerdata[4].equalsIgnoreCase("startDate"))) {
					throw new NPHCException(
							"Please upload csv file with corect headers in given sequence id,login,name,salary,startDate");
				}
			} catch (NPHCException e) {
				throw new NPHCException(
						"Please upload csv file with all 5 headers in given sequence id,login,name,salary,startDate");
			}
		}

	}
}
