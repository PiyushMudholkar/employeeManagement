package com.nphc.employeeManagement.validator;

import com.nphc.employeeManagement.exception.NPHCException;
import com.nphc.employeeManagement.repository.Employee;

@FunctionalInterface
public interface FindEmployee {

	public Employee find(String id,String errorMessage) throws NPHCException;
}
