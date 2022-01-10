package com.glen.ExcelParser.services;

import java.util.List;

import com.glen.ExcelParser.pojo.ValidationReport;

public interface FileValidationService {
	public List<ValidationReport> validate(String path);
}
