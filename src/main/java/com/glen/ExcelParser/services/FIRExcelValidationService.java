package com.glen.ExcelParser.services;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FIRExcelValidationService {
	
	private int REQUIRED_NO_OF_SHEETS = 8;
	private HashMap<Integer, String> SHEET_INDEX_TO_NAMES_REQUIRED = 
			new HashMap<Integer,String>(){{
				put(0,"coverpage");
				put(1,"documentcontrol");
				put(2,"pits");
				put(3,"ducts");
				put(4,"lics");
				put(5,"listvalues");
				put(6,"definitions");
				put(7,"systemimpacts");
			}};
	
	
	
	private String ERR_MSG_FILE_SIZE_BELOW_ZERO="File is is below zero kb";
	private String ERR_MSG_FILE_SHEETS_LESS_THAN_REQUIRED ="File doesn't have the required number of sheets";
	private String ERR_MSG_SHEET_INDEX_DOESNT_MATCH_TITLE_ORDER="The Sheet doesn't match the required order:";
	
	private List<String> validation_errors = new ArrayList<>();
	public List<String> validate(String path){
		try {
			OPCPackage pkg = OPCPackage.open(new File(path));
			XSSFWorkbook workbook  = new XSSFWorkbook(pkg);
			
			//Does file have required number of sheets
			validateFileForRequiredNumberOfSheets(workbook,REQUIRED_NO_OF_SHEETS);
			
			//Does Index and Sheet names are in order
			validateIfIndexAndSheetNamesAreInRequiredOrder(workbook,SHEET_INDEX_TO_NAMES_REQUIRED);
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return validation_errors;
	}
	
	private void validateIfIndexAndSheetNamesAreInRequiredOrder(XSSFWorkbook workbook,
			HashMap<Integer, String>sheetIndexToNames) {
		for(int i=0;i<workbook.getNumberOfSheets();i++) {
			String nameFromSheet = workbook.getSheetName(i).toLowerCase().replaceAll(" ","");
			if(!nameFromSheet.equals(SHEET_INDEX_TO_NAMES_REQUIRED.get(i)))
					validation_errors.add(ERR_MSG_SHEET_INDEX_DOESNT_MATCH_TITLE_ORDER
							+ " expected "+SHEET_INDEX_TO_NAMES_REQUIRED.get(i) + " as sheet " + (i+1)
							+" but recieved "+nameFromSheet+" as sheet "+(i+1)  
					);
		}
			
		
	}

	private void validateFileForRequiredNumberOfSheets(XSSFWorkbook workbook, int requiredSheets) {
		if(!(workbook.getNumberOfSheets()==requiredSheets))
			validation_errors.add(ERR_MSG_FILE_SHEETS_LESS_THAN_REQUIRED);
	}
}
