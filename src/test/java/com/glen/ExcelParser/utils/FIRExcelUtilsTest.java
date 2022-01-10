package com.glen.ExcelParser.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;



class FIRExcelUtilsTest {
	
	Workbook workBook;
	XSSFSheet sheet;
	XSSFRow row;
	
	@BeforeEach
	public void setUp() {
		workBook = new XSSFWorkbook();
		sheet = (XSSFSheet) workBook.createSheet("Test");
	}
	

	@Nested
	class GetFirstNonEmptyCellIndexTests{
		@Test
		void shouldReturnFirstNonNullIndexWhenGivenRightParameter(){
			List<String> cell=new ArrayList<>();
			cell.add(null);
			cell.add(null);
			cell.add("test");
			cell.add(null);
			int expected = 2;
			int result=FIRExcelUtils.getFirstNonEmptyCell(cell);
			assertEquals(expected, result);
		}
		@Test
		void shouldReturnNeegativeIndexWhenGivenNullCellValues(){
			List<String> cell=new ArrayList<>();
			cell.add(null);
			cell.add(null);
			cell.add(null);
			cell.add(null);
			int expected = -1;
			int result=FIRExcelUtils.getFirstNonEmptyCell(cell);
			assertEquals(expected, result);
		}
	}
	
	@Nested
	class GetRoWListFromSheetTests{
			
		@Test
		
		void shouldReturnCorrectNumberOfRowsWhenGivenAValidSheet(){
			for(int i=0;i<10;i++)
				sheet.createRow(i);
			int expectedRowCount = 10;
			assertEquals(expectedRowCount,FIRExcelUtils.getRowListFromSheet(sheet).size());
			
		}
		@Test 
		void shouldReturnEmptyListWhenSuppliedWithAnEmptySheet(){
			assertEquals(new ArrayList<>(),FIRExcelUtils.getRowListFromSheet(sheet));
		}
		
		@Test
		void shouldReturnEmptyListWhenGivenNullValue() {
			assertEquals(new ArrayList<>(),FIRExcelUtils.getRowListFromSheet(null));
		}
	}
	
	@Nested
	class GetCellValuesFromRowTests{
		
		@Test
		void shouldReturnEmptyListWhenPassedNullRow() {
			assertEquals(new ArrayList<>(),FIRExcelUtils.getCellValuesFromRow(row));
		}
		
		@Test
		void shouldReturnStringListWhenGivenValidRow() {
			row = sheet.createRow(0);
			row.createCell(0).setCellValue("Testcell1");
			row.createCell(1).setCellValue(4.0);
			row.createCell(2).setCellValue(false);
			row.createCell(3,CellType.BLANK);
			
			List<String>expected=new ArrayList<>();
			expected.add("Testcell1");
			expected.add("4.0");
			expected.add("false");
			expected.add(null);
			
			List<String>result = FIRExcelUtils.getCellValuesFromRow(row);
			
			assertAll(
					()->assertEquals(expected.get(0), result.get(0)),
					()->assertEquals(expected.get(1), result.get(1)),
					()->assertEquals(expected.get(2), result.get(2)),
					()->assertEquals(expected.get(3), result.get(3))
			);	
		}
		
		@Nested
		class RemoveEmptyRowsTest{
			
			@Test
			void shouldReturnEmptyListWhenGivenNullAsSheetRows() {
				assertEquals(new ArrayList<>(), FIRExcelUtils.removeEmptyRows(null, 0, 0));
			}
			
			@Test
			void shouldReturnEmptyListWhenGivenEmptyRowAsSheetRows() {
				assertEquals(new ArrayList<>(), FIRExcelUtils.removeEmptyRows(new ArrayList<>(), 0, 0));
			}
			
			@Test
			void shouldReturnCorrectNumberOfNonEmptyRowsWhenGivenValidArguements() {
				List<Row> rows = new ArrayList<>();
				row = sheet.createRow(0);
				row.createCell(0,CellType.BLANK);
				row.createCell(1,CellType.BLANK);
				row.createCell(2,CellType.BLANK);
				row.createCell(3,CellType.BLANK);
				rows.add(row);
				
				row = sheet.createRow(1);
				row.createCell(0).setCellValue("Testcell1");
				row.createCell(1).setCellValue(4.0);
				row.createCell(2).setCellValue(false);
				row.createCell(3,CellType.BLANK);
				rows.add(row);
				
				int expected=1;
				assertEquals(expected, FIRExcelUtils.removeEmptyRows(rows, 0, 3).size());
			}
			
			@ParameterizedTest
			@CsvSource({
				"-1,-1",
				"1,0",
				"0,5",
				"5,2"
			})
			void shouldThrowExceptionwhenPassedWrongParameters(int startCell,int stopCell) {
				List<Row> rows = new ArrayList<>();
				row = sheet.createRow(0);
				row.createCell(0,CellType.BLANK);
				row.createCell(1,CellType.BLANK);
				row.createCell(2,CellType.BLANK);
				row.createCell(3,CellType.BLANK);
				rows.add(row);
				
				row = sheet.createRow(1);
				row.createCell(0).setCellValue("Testcell1");
				row.createCell(1).setCellValue(4.0);
				row.createCell(2).setCellValue(false);
				row.createCell(3,CellType.BLANK);
				rows.add(row);
				
				assertThrows(IllegalArgumentException.class,
						()->FIRExcelUtils.removeEmptyRows(rows, startCell, stopCell)
				);
			}
		}
	}
	
	@Nested
	class ColunIndexesToExcludeTest{
		
		@Test
		void shouldReturnEmpltyListWhenGivenNUllColumnHeader() {
			assertEquals(new ArrayList<>(), FIRExcelUtils.getColumnIndecesToExclude(null,null));
		}
		
		@Test
		void shouldReturnValuesForExclusionWhenGivenCorrectParameters() {
			row = sheet.createRow(0);
			row.createCell(0).setCellValue("Testcell1");
			row.createCell(1).setCellValue("Testcell2");
			row.createCell(2).setCellValue("Testcell3");
			row.createCell(3).setCellValue("Testcell4");
			
			String[] notToRemove = {"Testcell2","Testcell4"};
			
			List<Integer> expected = new ArrayList<>();
			expected.add(0);
			expected.add(2);
			List<Integer> result = FIRExcelUtils.getColumnIndecesToExclude(row, notToRemove);
			assertEquals(expected, result);
			
		}
		@Test
		void shouldReturnAllIndecesWhenGivenNullOrEmptyColumnsToIncludeParameter() {
			row = sheet.createRow(0);
			row.createCell(0).setCellValue("Testcell1");
			row.createCell(1).setCellValue("Testcell2");
			row.createCell(2).setCellValue("Testcell3");
			row.createCell(3).setCellValue("Testcell4");
			
			List<Integer> expected=new ArrayList<>();
			expected.add(0);
			expected.add(1);
			expected.add(2);
			expected.add(3);
			
			String[] array= {};
			
			assertEquals(expected,FIRExcelUtils.getColumnIndecesToExclude(row,array));
			assertEquals(expected,FIRExcelUtils.getColumnIndecesToExclude(row,null));
			
		}
	}
	
	@Nested
	class DropColumnsWithSpecificIndecesTests{
		
		@Test
		void shouldReturnEmptyListWhenGivenNullRow() {
			assertEquals(new ArrayList<>(), FIRExcelUtils.dropColumnsWithIndexes(null, null));
		}
		
		@Test
		void shouldReturnCorrectColumnsWhenGivenRightArguements() {
			
			List<Row>rows = new ArrayList<>();
			row = sheet.createRow(0);
			row.createCell(0).setCellValue("Testcell1");
			row.createCell(1).setCellValue("Testcell2");
			row.createCell(2).setCellValue("Testcell3");
			row.createCell(3).setCellValue("Testcell4");
			rows.add(row);
			
			
			
			List<Integer> indecesToRemove=new ArrayList<>();
			indecesToRemove.add(0);
			indecesToRemove.add(1);
			
			List<Row>expected = new ArrayList<>();
			row = sheet.createRow(1);
			row.createCell(0).setCellValue("Testcell3");
			row.createCell(1).setCellValue("Testcell4");
			expected.add(row);

			List<Row>results = FIRExcelUtils.dropColumnsWithIndexes(rows, indecesToRemove);
			assertEquals(FIRExcelUtils.getCellValuesFromRow(expected.get(0)),
					FIRExcelUtils.getCellValuesFromRow(results.get(0))
			);
			
		}
		
		@Test
		void shouldReturnTheRowListtAsItIsWhenGivenEmptyOrNUllIndecesToRemove() {
			List<Row>rows = new ArrayList<>();
			row = sheet.createRow(0);
			row.createCell(0).setCellValue("Testcell1");
			row.createCell(1).setCellValue("Testcell2");
			row.createCell(2).setCellValue("Testcell3");
			row.createCell(3).setCellValue("Testcell4");
			rows.add(row);
			List<Row>resultsWhenNull = FIRExcelUtils.dropColumnsWithIndexes(rows, null);
			List<Row>resultsWhenEmpty = FIRExcelUtils.dropColumnsWithIndexes(rows, new ArrayList<>());
			
			assertEquals(FIRExcelUtils.getCellValuesFromRow(rows.get(0)),
					FIRExcelUtils.getCellValuesFromRow(resultsWhenNull.get(0)));
			
			assertEquals(FIRExcelUtils.getCellValuesFromRow(rows.get(0)),
					FIRExcelUtils.getCellValuesFromRow(resultsWhenEmpty.get(0)));
			
		}
	}
	
}
