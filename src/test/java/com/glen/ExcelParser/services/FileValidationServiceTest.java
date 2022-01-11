package com.glen.ExcelParser.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.glen.ExcelParser.Constants.FIRValidationConstants;
import com.glen.ExcelParser.pojo.ValidationReport;

class FileValidationServiceTest {
	
	FileValidationService firValidationService;
	
	@BeforeEach
	void setUp() {
		firValidationService = new FIRExcelValidationService();
	}
	
	@Test
	void shouldReturnNonEmptyValidationReportWithTypeErrorsWhenGivenEmptyFile() {
		String emptyFilePath = "testfiles/empty.xlsx";
		List<ValidationReport> vReport = firValidationService.validate(emptyFilePath);
		
		//Count number of error types
		int errorTypeCount = (int) vReport.stream()
				.filter(el->el.getType().equalsIgnoreCase(FIRValidationConstants.VALIDATION_TYPE_ERROR))
				.count();
		assertTrue(errorTypeCount>0);
	}
	
	@Test
	void shouldReturnNonEmptyValidationReportWithTypeErrorsWhenGivenIncorrectFile() {
		String incorrectFilePath = "testfiles/sampledatainsurance.xlsx";
		List<ValidationReport> vReport = firValidationService.validate(incorrectFilePath);
		
		//Count number of error types
		int errorTypeCount = (int) vReport.stream()
				.filter(el->el.getType().equalsIgnoreCase(FIRValidationConstants.VALIDATION_TYPE_ERROR))
				.count();
		assertTrue(errorTypeCount>0);
	}
	
	@Test
	void shouldReturnEmptyValidationReportWithTypeErrorsWhenGivenCorrectFile() {
		String incorrectFilePath = "testfiles/TLS10.1.2_FIR_5GUG-20_N50_1.1.xlsx";
		List<ValidationReport> vReport = firValidationService.validate(incorrectFilePath);
		
		//Count number of error types
		int errorTypeCount = (int) vReport.stream()
				.filter(el->el.getType().equalsIgnoreCase(FIRValidationConstants.VALIDATION_TYPE_ERROR))
				.count();
		assertTrue(errorTypeCount==0);
	}

}
