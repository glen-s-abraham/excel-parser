package com.glen.ExcelParser;



import com.glen.ExcelParser.services.ExcelFileParserService;

/**
 * Hello world!
 *
 */
public class App 
{
	
	private static ExcelFileParserService excelParser=new ExcelFileParserService();
	
    public static void main( String[] args )
    {
        String path="C:/Users/Glen/Fulton-Hogan/WorkoutFiles/OneDrive_1_1-4-2022/TLS10.1.2_FIR_5GUG-20_N50_1.1.xlsx";
        excelParser.loadExcelFiles(path);
    }
}
