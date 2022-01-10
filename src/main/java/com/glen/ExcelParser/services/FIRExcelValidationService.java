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

import com.glen.ExcelParser.Constants.FIRValidationConstants;
import com.glen.ExcelParser.pojo.ValidationReport;
import com.glen.ExcelParser.utils.FIRExcelUtils;
/*
 * Currently Implemented validations
 * - Check for total number of sheets
 * - Checks the Names and Order of sheets.
 * - Check for slug's presence in Coverpage 
 */

public class FIRExcelValidationService{
	
	private List<ValidationReport> validationErrors = new ArrayList<>();
	
	public List<ValidationReport> validate(String path){
		try {
			OPCPackage pkg = OPCPackage.open(new File(path));
			XSSFWorkbook workbook  = new XSSFWorkbook(pkg);
			
			//Does file have required number of sheets
			validateFileForRequiredNumberOfSheets(workbook, FIRValidationConstants.REQUIRED_NO_OF_SHEETS);
			
			//Does Index and Sheet names are in order
			validateIfIndexAndSheetNamesAreInRequiredOrder(
					workbook,
					FIRValidationConstants.SHEET_INDEX_TO_NAMES_REQUIRED
			);
			
			validateIfCoverPageContainsSpecifiedSlugs(
					workbook.getSheet(FIRValidationConstants.SHEET_INDEX_TO_NAMES_REQUIRED.get(0)),
					FIRValidationConstants.COVER_SHEET_SLUGS
			);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return validationErrors;
	}
	
	private void validateIfCoverPageContainsSpecifiedSlugs(XSSFSheet sheet, String[] coverShetSlugs) {
		List<Row> rows = FIRExcelUtils.getRowListFromSheet(sheet);
		rows = FIRExcelUtils.removeEmptyRows(
				rows,
				FIRValidationConstants.CELL_NULL_RANGE_START_INDEX,
				FIRValidationConstants.CELL_NULL_RANGE_STOP_INDEX
		);
		List<String> valueToBeChecked = new ArrayList<>();
		
		for(Row row:rows) {
			List<String> cellValues = FIRExcelUtils.getCellValuesFromRow(row);
			int  firstNonEmptyCell = FIRExcelUtils.getFirstNonEmptyCell(cellValues);
			if(firstNonEmptyCell!=-1)
				valueToBeChecked.add(parseToLowerandRemoveSpace(cellValues.get(firstNonEmptyCell)));
		}
		for(String slug:coverShetSlugs)
			if(!valueToBeChecked.contains(slug))
				validationErrors.add(
						new ValidationReport(
								FIRValidationConstants.VALIDATION_TYPE_WARNING,
								FIRValidationConstants.ERR_MSG_MISSING_FIELDS+slug
				));
	}

	private void validateIfIndexAndSheetNamesAreInRequiredOrder(XSSFWorkbook workbook,
			Map<Integer, String> sHEET_INDEX_TO_NAMES_REQUIRED2) {
		for(int i=0;i<workbook.getNumberOfSheets();i++) {
			String nameFromSheet = workbook.getSheetName(i);
			String userSpecifiedName = FIRValidationConstants.SHEET_INDEX_TO_NAMES_REQUIRED.get(i);
			if(!parseToLowerandRemoveSpace(nameFromSheet).equals(parseToLowerandRemoveSpace(userSpecifiedName)))
					validationErrors.add(
							new ValidationReport(
									FIRValidationConstants.VALIDATION_TYPE_ERROR,
									FIRValidationConstants.ERR_MSG_SHEET_INDEX_DOESNT_MATCH_TITLE_ORDER
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
			validationErrors.add(
					new ValidationReport(
							FIRValidationConstants.VALIDATION_TYPE_ERROR, 
							FIRValidationConstants.ERR_MSG_FILE_SHEETS_LESS_THAN_REQUIRED
			));
	}
}
