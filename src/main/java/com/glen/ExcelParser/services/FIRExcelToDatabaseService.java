package com.glen.ExcelParser.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.glen.ExcelParser.pojo.FileToDbArguements;
import com.glen.ExcelParser.pojo.SimpleInsertQuery;
import com.glen.ExcelParser.utils.FIRExcelUtils;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.openxml4j.opc.OPCPackage;

public class FIRExcelToDatabaseService implements FileToDatabaseLoaderService{
	
	private int[] SHEET_INDEXES_TO_USE= {2,3,4};
	private int CELL_NULL_RANGE_START_INDEX = 0;
	private int CELL_NULL_RANGE_STOP_INDEX = 5;
	
	public HashMap<Integer, String[]> COLS_TO_INCLUDE_FROM_SHEETS =
	new HashMap<Integer,String[]>(){{
		put(2, new String[] {
			"FIR Pit Reference Number",
			"latitude",
			"longitude",
			"nbn_system_identifier"	
		});
		put(3,new String[] {
			"FIR Duct Reference Number",
			"material",
			"ownership",
		});
		put(4,new String[] {
				"LIC Reference Number",
				"Ref_ID",
				"MPS_ID"	
		});	
	}};
	
	public HashMap<Integer, String> SHEET_TO_TABLE = 
		new HashMap<Integer,String>(){{
		put(2, "fir_pits");
		put(3, "fir_duct");
		put(4, "fir_lics");
	}};
	
	@Override
	public void loadFilesToDB(FileToDbArguements fileToDbArguements) {
		OPCPackage pkg;
		XSSFWorkbook workbook;
		XSSFSheet sheet;
		try {
			pkg = OPCPackage.open(new File(fileToDbArguements.getPath()));
			workbook  = new XSSFWorkbook(pkg);
			Map<String,String>additionalDbEntries = fileToDbArguements.getAdditionalDbEntries();
			for(int i=0;i<SHEET_INDEXES_TO_USE.length;i++) {			
				sheet =  workbook.getSheetAt(SHEET_INDEXES_TO_USE[i]);
				String[] columnsToInclude = COLS_TO_INCLUDE_FROM_SHEETS.get(SHEET_INDEXES_TO_USE[i]);
				String tableName = SHEET_TO_TABLE.get(SHEET_INDEXES_TO_USE[i]);
				List<String> queryList=processSheetAndGenerateQuery(sheet,columnsToInclude,tableName,additionalDbEntries);
				if(queryList!=null)
					executeQueries(queryList);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void executeQueries(List<String> queryList) {
		//TODO implement jdbc query insertion logic
		System.out.println(queryList.get(0));
	}

	private List<String> processSheetAndGenerateQuery(XSSFSheet sheet,String[] columnsToInclude, String tableName, Map<String, String> additionalDbEntries) {
		List<Row> sheetRows = FIRExcelUtils.getRowListFromSheet(sheet);
		sheetRows = FIRExcelUtils.removeEmptyRows(sheetRows,CELL_NULL_RANGE_START_INDEX,CELL_NULL_RANGE_STOP_INDEX);
		List<Integer> colIndexesToExclude = FIRExcelUtils.getColumnIndecesToExclude(sheetRows.get(0),columnsToInclude);
		sheetRows = FIRExcelUtils.dropColumnsWithIndexes(sheetRows, colIndexesToExclude);
		List<String> columnHeaders = FIRExcelUtils.getCellValuesFromRow(sheetRows.get(0));
		
		//Add Additional Entries to the Column header
		for(String columnName:additionalDbEntries.keySet())
			columnHeaders.add(columnName);
		
		if(columnHeaders!=null && sheetRows!=null && sheetRows.size()>1)
		{			
			List<String> insertQueries=new ArrayList<>();
			for(int i=1;i<sheetRows.size();i++) {
				List<String> values = FIRExcelUtils.getCellValuesFromRow(sheetRows.get(i));
				for(String value:additionalDbEntries.values())
					values.add(value);
				insertQueries.add(new SimpleInsertQuery.Builder(tableName, columnHeaders)
				.setValues(values).build().getSqlQuery());
			}
			System.out.println("generated "+insertQueries.size()+" queries for "+tableName);
			return insertQueries;
		}
		return null;
	}
}

