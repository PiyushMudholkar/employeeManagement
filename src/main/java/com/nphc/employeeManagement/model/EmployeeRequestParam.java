package com.nphc.employeeManagement.model;

public class EmployeeRequestParam {

	private Double minSalary;
	private Double maxSalary;
	private int offset;
	private int limit;
	private String sortBy;
	private String order;
	private String filter;
	
	public EmployeeRequestParam(Double minSalary, Double maxSalary, int offset, int limit, String sortBy, String order,String filter) {
		super();
		this.minSalary = minSalary;
		this.maxSalary = maxSalary;
		this.offset = offset;
		this.limit = limit;
		this.sortBy = sortBy;
		this.order = order;
		this.filter = filter;
		
	}
	public Double getMinSalary() {
		return minSalary;
	}
	public void setMinSalary(Double minSalary) {
		this.minSalary = minSalary;
	}
	public Double getMaxSalary() {
		return maxSalary;
	}
	public void setMaxSalary(Double maxSalary) {
		this.maxSalary = maxSalary;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
	
	@Override
	public String toString() {
		return String.format(
				"EmployeeRequestParam [minSalary=%s, maxSalary=%s, offset=%s, limit=%s, sortBy=%s, filter=%s, order=%s]",
				minSalary, maxSalary, offset, limit, sortBy, filter, order);
	}
	
}
