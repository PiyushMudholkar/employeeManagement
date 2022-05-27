package com.nphc.employeeManagement.model;

public class CSVResponse {

	private boolean updateRecord; // will be set when any of the record is updated 
	private boolean skipRecord=true; // will be set when records are not inserted
	
	//private String message;

	public CSVResponse() {
	}

	public boolean isUpdateRecord() {
		return updateRecord;
	}

	public void setUpdateRecord(boolean updateRecord) {
		this.updateRecord = updateRecord;
	}

	public boolean isSkipRecord() {
		return skipRecord;
	}

	public void setSkipRecord(boolean skipRecord) {
		this.skipRecord = skipRecord;
	}

	/*
	 * public String getMessage() { return message; }
	 * 
	 * public void setMessage(String message) { this.message = message; }
	 */

	
	
	
}
