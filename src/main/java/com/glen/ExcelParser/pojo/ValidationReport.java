package com.glen.ExcelParser.pojo;

public class ValidationReport {
	private final String type;
	private final String message;
	public ValidationReport(String type, String message) {
		this.type = type;
		this.message = message;
	}
	public String getType() {
		return type;
	}
	public String getMessage() {
		return message;
	}
	
	
}
