package com.glen.ExcelParser;



import java.util.ArrayList;
import java.util.List;

import com.glen.ExcelParser.pojo.FileToDbArguements;
import com.glen.ExcelParser.pojo.ValidationReport;
import com.glen.ExcelParser.services.FIRExcelToDatabaseService;
import com.glen.ExcelParser.services.FIRExcelValidationService;
import com.glen.ExcelParser.services.FileToDatabaseLoaderService;
import com.glen.ExcelParser.services.FileValidationService;
import com.glen.ExcelParser.utils.FIRExcelUtils;
import com.glen.ExcelParser.utils.FileUnzipper;

/**
 * Hello world!
 *
 */
public class App 
{
	
	private static FileToDatabaseLoaderService excelFirLoader=new FIRExcelToDatabaseService();
	private static FileValidationService excelFirValidation = new FIRExcelValidationService();
	
    public static void main( String[] args )
    {
    	 
    	 String path="testfiles/TLS10.1.2_FIR_5GUG-20_N50_1.1.xlsx";
    	
//    	 for(ValidationReport error:excelFirValidation.validate(path))
//    		 System.out.println(error.getType()+" "+error.getMessage());
    	 System.out.println();
    	 FileToDbArguements fdargs = new FileToDbArguements.Builder(path)
    			 .addAdditionalDbEntries("ada", "5GUG-1-10")
    			 .build();
	     excelFirLoader.loadFilesToDB(fdargs);

   }
}
