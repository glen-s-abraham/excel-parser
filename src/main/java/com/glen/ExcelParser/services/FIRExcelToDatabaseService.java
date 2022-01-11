package com.glen.ExcelParser.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
				String[] columnsToInclude = FIRDatabaseConstants.COLS_TO_INCLUDE_FROM_SHEETS
						.get(FIRDatabaseConstants.SHEET_INDEXES_TO_USE[i]);
				String tableName = FIRDatabaseConstants.SHEET_TO_TABLE.get(FIRDatabaseConstants.SHEET_INDEXES_TO_USE[i]);
				List<String> queryList=processSheetAndGenerateQuery(sheet,columnsToInclude,tableName,additionalDbEntries);
				if(queryList.size()!=0)
					executeQueries(queryList);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void executeQueries(List<String> queryList) {
		//TODO implement jdbc query insertion logic
		System.out.println("-----------------------------");
		System.out.println("Execute preload scripts......");
		System.out.println("Batch execute queries........");
		System.out.println("Sample Query: "+queryList.get(0));
		System.out.println("-----------------------------");
	}

	private List<String> processSheetAndGenerateQuery(
			XSSFSheet sheet,
			String[] columnsToInclude,
			String tableName,
			Map<String, String> additionalDbEntries
	) {
		
		List<Row> sheetRows = FIRExcelUtils.getRowListFromSheet(sheet);
		sheetRows = FIRExcelUtils.removeEmptyRows(
				sheetRows,
				FIRDatabaseConstants.CELL_NULL_RANGE_START_INDEX,
				FIRDatabaseConstants.CELL_NULL_RANGE_STOP_INDEX
		);
		
		List<Integer> colIndexesToExclude = FIRExcelUtils.getColumnIndecesToExclude(sheetRows.get(0),columnsToInclude);
		sheetRows = FIRExcelUtils.dropColumnsWithIndexes(sheetRows, colIndexesToExclude);
		List<String> columnHeaders = getDbColumnNamesFromSheetColumnNames(columnsToInclude);
		
		//Add Additional Entries to the Column header
		for(String columnName:additionalDbEntries.keySet())
			columnHeaders.add(columnName);
		
		if(columnHeaders!=null && columnHeaders.size()!=0 && sheetRows!=null && sheetRows.size()>1)
		{			
			List<String> insertQueries=new ArrayList<>();
			for(int i=1;i<sheetRows.size();i++) {
				List<String> values = FIRExcelUtils.getCellValuesFromRow(sheetRows.get(i));
				
				for(String value:additionalDbEntries.values())
					values.add(value);
				
				insertQueries.add(new SimpleInsertQuery.Builder(tableName, columnHeaders)
						.setValues(values)
						.build()
						.getSqlQuery()
				);
			}
			
			System.out.println("GENERATED: "+insertQueries.size()+" QUERIES FOR TABLE: "+tableName);
			return insertQueries;
		}
		return new ArrayList<>();
	}

	private List<String> getDbColumnNamesFromSheetColumnNames(String[] columnsToInclude) {
		if(columnsToInclude==null || columnsToInclude.length==0) return new ArrayList<>();
		List<String> dbCols = new ArrayList<>();
		for(String sheetCol:columnsToInclude) {
			String colName=FIRDatabaseConstants.SHEET_COLS_TO_DB_COLS.get(sheetCol);
			if(colName==null)throw new RuntimeException(
					"Mapping for col: '"
					+sheetCol
					+"' Not Configured in FIRDatabaseConstants.SHEET_COLS_TO_DB_COLS"
			);
			dbCols.add(colName);
		}
		return dbCols;
	}
}

