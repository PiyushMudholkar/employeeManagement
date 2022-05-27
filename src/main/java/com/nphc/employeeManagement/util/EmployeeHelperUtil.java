package com.nphc.employeeManagement.util;

import java.util.Comparator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nphc.employeeManagement.exception.NPHCException;
import com.nphc.employeeManagement.model.EmployeeRequestParam;
import com.nphc.employeeManagement.repository.Employee;

@Component
public class EmployeeHelperUtil {
	
    @Autowired
	private Map<String,Comparator<Employee>> sortByComparator;
	
	public Comparator<Employee> getComparator(EmployeeRequestParam employeeRequestParam) {
		// assign default comparator by id
		Comparator<Employee> employeeComparator = sortByComparator.get("id");
		// check if the comparator is there
		if (sortByComparator.containsKey(employeeRequestParam.getSortBy().toLowerCase())) {
			employeeComparator = sortByComparator.get(employeeRequestParam.getSortBy().toLowerCase());
		}
		if (employeeRequestParam.getOrder().equalsIgnoreCase(NPHCConstants.ORDER_DEC))
			employeeComparator = employeeComparator.reversed();

		return employeeComparator;

	}
	public Boolean filter(Employee e,EmployeeRequestParam employeeRequestParam) {
        
		String data=employeeRequestParam.getFilter().toLowerCase();
		try {
			if (data.trim().isBlank() || verifyContains(e.getName(),data) || verifyContains(e.getLogin(), data) || verifyContains(e.getId(), data)
			   || verifyContains(e.getSalary().toString(),data) || 
			    verifyContains(DateUtil.convertDateToString(e.getStartDate()), data))
				return true;
			
		} catch (NPHCException e1) {
			System.out.println("In exception " +data);
		}
		return false;

	}
	
	private boolean verifyContains(String sourceData , String providedData) {
		
		return sourceData.toLowerCase().contains(providedData);
		
	}
	
	public void verifySortBy(EmployeeRequestParam employeeRequestParam) throws NPHCException {
		if(!sortByComparator.containsKey(employeeRequestParam.getSortBy().toLowerCase())) {
		    throw new NPHCException(NPHCConstants.INVALIID_SORTBY);
		}
	}
}
