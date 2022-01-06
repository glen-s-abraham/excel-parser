package com.glen.ExcelParser.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;



public class ExcelFileParserService {

	public void loadExcelFiles(String path) {
		//Generate file stream with path
		//POIFSFileSystem file = new POIFSFileSystem(new File(path));
		OPCPackage pkg;
		XSSFWorkbook workbook;
		XSSFSheet sheet;
		try {
			pkg = OPCPackage.open(new File(path));
			workbook  = new XSSFWorkbook(pkg);
			sheet =  workbook.getSheetAt(3);
		
			//iterateThroughSheet(sheet);
			
			List<Row> sheetRows = getRowListFromSheet(sheet);
			List<Row> trimmedRows = removeEmptyRows(sheetRows);
			List<String> cellValues = getCellValuesFromRow(trimmedRows.get(0));
			System.out.println(cellValues);
			cellValues = getCellValuesFromRow(trimmedRows.get(1));
			System.out.println(cellValues);

			
		}catch (Exception e) {
			e.printStackTrace();
		}
			
		
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
