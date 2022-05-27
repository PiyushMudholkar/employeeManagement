package com.nphc.employeeManagement.model;

public class CSVResponse {

	private boolean updateRecord; // will be set when any of the record is updated 
	private boolean skipRecord=true; // will be set when records are not inserted
	


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


	@Override
	public String toString() {
		return String.format("CSVResponse [updateRecord=%s, skipRecord=%s]", updateRecord, skipRecord);
	}
}
