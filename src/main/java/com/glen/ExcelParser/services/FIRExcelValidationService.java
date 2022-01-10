package com.glen.ExcelParser.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.glen.ExcelParser.pojo.ValidationReport;
import com.glen.ExcelParser.utils.FIRExcelUtils;
/*
 * Currently Implemented validations
 * - Check for total number of sheets
 * - Checks the Names and Order of sheets.
 * - Check for word slug's presence in Coverpage 
 */

public class FIRExcelValidationService {
	
	private final int REQUIRED_NO_OF_SHEETS = 8;
	private final int CELL_NULL_RANGE_START_INDEX = 0;
	private final int CELL_NULL_RANGE_STOP_INDEX = 1;
	private final Map<Integer, String> SHEET_INDEX_TO_NAMES_REQUIRED = 
			new HashMap<Integer,String>(){{
				put(0,"Cover Page");
				put(1,"Document Control");
				put(2,"Pits");
				put(3,"Ducts");
				put(4,"LICs");
				put(5,"ListValues");
				put(6,"Definitions");
				put(7,"System Impacts");
			}};
			
	private final String[] COVER_SHEET_SLUGS= {
			"nbn-confidential-commercial",
			"fieldinspectionreport(n2p)",
			"documentnumber",
			"documentcategory",
			"author",
			"approver(owner)",
			"status",
			"issuedate",
			"revisionnumber",
			"start-date",
			"end-date"
	};
	
	private final String VALIDATION_TYPE_ERROR="Error";
	private final String VALIDATION_TYPE_WARNING="Warning";
	
	private final String ERR_MSG_FILE_SHEETS_LESS_THAN_REQUIRED ="File doesn't have the required number of sheets";
	private final String ERR_MSG_SHEET_INDEX_DOESNT_MATCH_TITLE_ORDER="The Sheet doesn't match the required order:";
	private final String ERR_MSG_MISSING_FIELDS = "Cover Page is missing field:";
	
	private List<ValidationReport> validation_errors = new ArrayList<>();
	
	public List<ValidationReport> validate(String path){
		try {
			OPCPackage pkg = OPCPackage.open(new File(path));
			XSSFWorkbook workbook  = new XSSFWorkbook(pkg);
			
			//Does file have required number of sheets
			validateFileForRequiredNumberOfSheets(workbook,REQUIRED_NO_OF_SHEETS);
			
			//Does Index and Sheet names are in order
			validateIfIndexAndSheetNamesAreInRequiredOrder(workbook,SHEET_INDEX_TO_NAMES_REQUIRED);
			
			validateIfCoverPageContainsSpecifiedSlugs(workbook.getSheetAt(1),COVER_SHEET_SLUGS);
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return validation_errors;
	}
	
	private void validateIfCoverPageContainsSpecifiedSlugs(XSSFSheet sheet, String[] coverShetSlugs) {
		List<Row> rows = FIRExcelUtils.getRowListFromSheet(sheet);
		rows = FIRExcelUtils.removeEmptyRows(rows,CELL_NULL_RANGE_START_INDEX,CELL_NULL_RANGE_STOP_INDEX);
		List<String> valueToBeChecked = new ArrayList<>();
		
		for(Row row:rows) {
			List<String> cellValues = FIRExcelUtils.getCellValuesFromRow(row);
			int  firstNonEmptyCell = FIRExcelUtils.getFirstNonEmptyCell(cellValues);
			valueToBeChecked.add(parseToLowerandRemoveSpace(cellValues.get(firstNonEmptyCell)));
		}
		for(String slug:coverShetSlugs)
			if(!valueToBeChecked.contains(slug))
				validation_errors.add(
						new ValidationReport(VALIDATION_TYPE_WARNING,ERR_MSG_MISSING_FIELDS+slug)
				);
	}

	private void validateIfIndexAndSheetNamesAreInRequiredOrder(XSSFWorkbook workbook,
			Map<Integer, String> sHEET_INDEX_TO_NAMES_REQUIRED2) {
		for(int i=0;i<workbook.getNumberOfSheets();i++) {
			String nameFromSheet = workbook.getSheetName(i);
			String userSpecifiedName = SHEET_INDEX_TO_NAMES_REQUIRED.get(i);
			if(!parseToLowerandRemoveSpace(nameFromSheet).equals(parseToLowerandRemoveSpace(userSpecifiedName)))
					validation_errors.add(
							new ValidationReport(
									VALIDATION_TYPE_ERROR,ERR_MSG_SHEET_INDEX_DOESNT_MATCH_TITLE_ORDER
									+ " expected "+userSpecifiedName + " as sheet " + (i+1)
									+" but recieved "+nameFromSheet+" as sheet "+(i+1))  
					);
		}	
	}

	private String parseToLowerandRemoveSpace(String str) {
		return str.toLowerCase().replaceAll(" ","");
	}

	private void validateFileForRequiredNumberOfSheets(XSSFWorkbook workbook, int requiredSheets) {
		if(!(workbook.getNumberOfSheets()==requiredSheets))
			validation_errors.add(
					new ValidationReport(VALIDATION_TYPE_ERROR, ERR_MSG_FILE_SHEETS_LESS_THAN_REQUIRED)
			);
	}
}
