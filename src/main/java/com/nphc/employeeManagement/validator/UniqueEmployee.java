package com.nphc.employeeManagement.validator;

import com.nphc.employeeManagement.exception.NPHCException;

@FunctionalInterface
public interface UniqueEmployee {

	public boolean validate(String id) throws NPHCException;
}
