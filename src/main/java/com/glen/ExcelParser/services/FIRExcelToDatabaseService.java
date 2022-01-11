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

import com.glen.ExcelParser.Constants.FIRDatabaseConstants;
import com.glen.ExcelParser.pojo.FileToDbArguements;
import com.glen.ExcelParser.pojo.SimpleInsertQuery;
import com.glen.ExcelParser.utils.FIRExcelUtils;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.openxml4j.opc.OPCPackage;

public class FIRExcelToDatabaseService implements FileToDatabaseLoaderService{
	
	
	@Override
	public void loadFilesToDB(FileToDbArguements fileToDbArguements) {
		OPCPackage pkg;
		XSSFWorkbook workbook;
		XSSFSheet sheet;
		try {
			pkg = OPCPackage.open(new File(fileToDbArguements.getPath()));
			workbook  = new XSSFWorkbook(pkg);
			Map<String,String>additionalDbEntries = fileToDbArguements.getAdditionalDbEntries();
			for(int i=0;i<FIRDatabaseConstants.SHEET_INDEXES_TO_USE.length;i++) {			
				sheet =  workbook.getSheetAt(FIRDatabaseConstants.SHEET_INDEXES_TO_USE[i]);
				String[] columnsToInclude = FIRDatabaseConstants.COLS_TO_INCLUDE_FROM_SHEETS.get(FIRDatabaseConstants.SHEET_INDEXES_TO_USE[i]);
				String tableName = FIRDatabaseConstants.SHEET_TO_TABLE.get(FIRDatabaseConstants.SHEET_INDEXES_TO_USE[i]);
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
		System.out.println("Sample Query: "+queryList.get(0));
	}

	private List<String> processSheetAndGenerateQuery(XSSFSheet sheet,String[] columnsToInclude, String tableName, Map<String, String> additionalDbEntries) {
		List<Row> sheetRows = FIRExcelUtils.getRowListFromSheet(sheet);
		sheetRows = FIRExcelUtils.removeEmptyRows(
				sheetRows,
				FIRDatabaseConstants.CELL_NULL_RANGE_START_INDEX,
				FIRDatabaseConstants.CELL_NULL_RANGE_STOP_INDEX);
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

