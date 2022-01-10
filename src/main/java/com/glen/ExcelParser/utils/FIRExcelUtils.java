package com.glen.ExcelParser.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class FIRExcelUtils {
	public static List<Row> dropColumnsWithIndexes(List<Row> sheetRows,List<Integer> indecesToRemove) {
		List<Row> rowList = sheetRows;
		for(Row row:rowList) 
			for(int index:indecesToRemove) 
				if(row.getCell(index)!=null)
					row.removeCell(row.getCell(index));
	
		return rowList.size()==0?null:rowList;
	}


	public static List<Integer> getColumnIndecesToExclude(Row colHeaders,String[] columnsToInclude) {
		List<Integer> requiredIndeces = new ArrayList<>();
		List<String> cellValues = getCellValuesFromRow(colHeaders);
		for(int i=0;i<cellValues.size();i++) {
			if(!Arrays.asList(columnsToInclude).contains(cellValues.get(i))) 
				requiredIndeces.add(i);		
		}		
		return requiredIndeces.size()==0?null:requiredIndeces;
	}

	
	public static List<Row> removeEmptyRows(List<Row> sheetRows,int startCell,int stopCell) {
		List<Row> rowList = new ArrayList<Row>();
		
		for(Row row:sheetRows) 
			if(!isSpecifiedCellRangeNull(getCellValuesFromRow(row),startCell,stopCell))	
				rowList.add(row);
		
		return rowList.size()==0?null:rowList;
	}
	
	public static boolean isSpecifiedCellRangeNull(List<String> cellValuesFromRow, int startIndex, int stopIndex) {
			for(int i=startIndex;i<=stopIndex;i++) 
				if(cellValuesFromRow.get(i)!=null)
					return false;
			return true;	 
	}
	
	public static List<String> getCellValuesFromRow(Row row) {
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

	public static List<Row> getRowListFromSheet(XSSFSheet sheet) {
		List <Row> rowList = new ArrayList<Row>();
		Iterator<Row> rwIterator = sheet.iterator();
		while(rwIterator.hasNext()) 
			rowList.add(rwIterator.next());
		
		return rowList.size()==0?null:rowList;
	}
	
	public static int getFirstNonEmptyCell(List<String> cellValues) {
		for(int firstNonEmptyCell=0;firstNonEmptyCell<cellValues.size();firstNonEmptyCell++)
			if(cellValues.get(firstNonEmptyCell)!=null) {
				return firstNonEmptyCell;
			}
				
		return -1;
	}
}
