package com.glen.ExcelParser.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.glen.ExcelParser.pojo.SimpleInsertQuery;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.openxml4j.opc.OPCPackage;

public class ExcelFileParserService implements FileToDatabaseLoaderService{
	
	private String[] COLS_TO_INCLUDE_FROM_FIR= {
			"FIR Pit Reference Number",
			"latitude",
			"longitude",
			"nbn_system_identifier",
			"FIR Duct Reference Number",
			"material",
			"ownership",
			"LIC Reference Number",
			"Ref_ID",
			"MPS_ID"
	};
	
	public int[] SHEET_INDEXES_TO_USE= {2,3,4};
	public HashMap<Integer, String> SHEET_TO_TABLE = 
		new HashMap<Integer,String>(){{
		put(2, "fir_pits");
		put(3, "fir_duct");
		put(4, "fir_lics");
	}};
	
	@Override
	public void loadFilesToDB(String path) {
		OPCPackage pkg;
		XSSFWorkbook workbook;
		XSSFSheet sheet;
		try {
			pkg = OPCPackage.open(new File(path));
			//generate queries for each sheet
			for(int i=0;i<SHEET_INDEXES_TO_USE.length;i++) {
				workbook  = new XSSFWorkbook(pkg);
				sheet =  workbook.getSheetAt(SHEET_INDEXES_TO_USE[i]);
				String tableName = SHEET_TO_TABLE.get(SHEET_INDEXES_TO_USE[i]);
				List<String> queryList=processSheetAndGenerateQuery(sheet,tableName);
				System.out.println(queryList.get(100));
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
			
		
	}
	
	private List<String> processSheetAndGenerateQuery(XSSFSheet sheet, String tableName) {
		List<Row> sheetRows = getRowListFromSheet(sheet);
		List<Row> trimmedRows = removeEmptyRows(sheetRows);
		List<Integer> colIndexesToExclude = getColumnIndecesToExclude(trimmedRows.get(0));
		trimmedRows = dropColumnsWithIndexes(trimmedRows, colIndexesToExclude);
		List<String> columnHeaders = getCellValuesFromRow(trimmedRows.get(0));
		if(columnHeaders!=null && trimmedRows!=null && trimmedRows.size()>1)
		{			
			List<String> insertQueries=new ArrayList<>();
			for(int i=1;i<trimmedRows.size();i++)
				 insertQueries.add(new SimpleInsertQuery.Builder(tableName, columnHeaders)
				.setValues(getCellValuesFromRow(trimmedRows.get(i))).build().getSqlQuery());
			System.out.println("generated "+insertQueries.size()+" queries for "+tableName);
			return insertQueries;
		}
		return null;
	}

	private List<Row> dropColumnsWithIndexes(List<Row> sheetRows,List<Integer> indecesToRemove) {
		List<Row> rowList = sheetRows;
		for(Row row:rowList) 
			for(int index:indecesToRemove) 
				if(row.getCell(index)!=null)
					row.removeCell(row.getCell(index));
	
		return rowList.size()==0?null:rowList;
	}


	private List<Integer> getColumnIndecesToExclude(Row colHeaders) {
		List<Integer> requiredIndeces = new ArrayList<>();
		List<String> cellValues = getCellValuesFromRow(colHeaders);
		for(int i=0;i<cellValues.size();i++) {
			if(!Arrays.asList(COLS_TO_INCLUDE_FROM_FIR).contains(cellValues.get(i))) 
				requiredIndeces.add(i);		
		}		
		return requiredIndeces.size()==0?null:requiredIndeces;
	}

	//preprocessing step 1 remove empty rows
	private List<Row> removeEmptyRows(List<Row> sheetRows) {
		List<Row> rowList = new ArrayList<Row>();
		for(Row row:sheetRows) {
			//seperate as a validator
			if(isSpecifiedCellRangeNull(getCellValuesFromRow(row),0,5)){
				continue;
			}
			rowList.add(row);
		}
		return rowList.size()==0?null:rowList;
	}
	
	//Validator for preprocessing
	private boolean isSpecifiedCellRangeNull(List<String> cellValuesFromRow, int startIndex, int stopIndex) {
		for(int i=startIndex;i<=stopIndex;i++) 
			if(cellValuesFromRow.get(i)!=null)
				return false;
		
		return true; 
	}
	
	private List<String> getCellValuesFromRow(Row row) {
		List<String> cellValues = new ArrayList<String>();
		Iterator<Cell> cellsIterator = row.cellIterator();
		while(cellsIterator.hasNext()) {
			Cell cellData = cellsIterator.next();
			switch (cellData.getCellType()) {
	            case STRING:
	                cellValues.add(cellData.getStringCellValue());
	                break;
	            case BOOLEAN:
	            	cellValues.add(String.valueOf(cellData.getBooleanCellValue()));
	                break;
	            case NUMERIC:
	            	cellValues.add(String.valueOf(cellData.getNumericCellValue()));
	                break;
	            case BLANK:
	            	cellValues.add(null);
            	default:
            		break;
			}
		}
		return cellValues.size()==0?null:cellValues;
	}

	private List<Row> getRowListFromSheet(XSSFSheet sheet) {
		List <Row> rowList = new ArrayList<Row>();
		Iterator<Row> rwIterator = sheet.iterator();
		while(rwIterator.hasNext()) {
			rowList.add(rwIterator.next());
		}
		return rowList.size()==0?null:rowList;
	}
	
	

	private void iterateThroughSheet(XSSFSheet sheet) {
		Iterator<Row> iterator = sheet.iterator();
		while(iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cells = nextRow.cellIterator();
			while(cells.hasNext()) {
				Cell cell = cells.next();
				 switch (cell.getCellType()) {
	                 case STRING:
	                     System.out.print(cell.getStringCellValue());
	                     break;
	                 case BOOLEAN:
	                     System.out.print(cell.getBooleanCellValue());
	                     break;
	                 case NUMERIC:
	                     System.out.print(cell.getNumericCellValue());
	                     break;
				default:
					break;
             }
			System.out.print(" - ");	 
			}
			System.out.println();
		}
	}
}
