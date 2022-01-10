package com.glen.ExcelParser;



import java.util.ArrayList;
import java.util.List;

import com.glen.ExcelParser.pojo.ValidationReport;
import com.glen.ExcelParser.services.FIRExcelToDatabaseService;
import com.glen.ExcelParser.services.FIRExcelValidationService;
import com.glen.ExcelParser.services.FileToDatabaseLoaderService;
import com.glen.ExcelParser.utils.FIRExcelUtils;
import com.glen.ExcelParser.utils.FileUnzipper;

/**
 * Hello world!
 *
 */
public class App 
{
	
	private static FileToDatabaseLoaderService excelParser=new FIRExcelToDatabaseService();
	private static FIRExcelValidationService excelValidation = new FIRExcelValidationService();
	
    public static void main( String[] args )
    {
//    	 String path = "C:/Users/Glen/Downloads/sampledatainsurance/sampledatainsurance.xlsx";
//    	 path="C:/Users/Glen/Fulton-Hogan/WorkoutFiles/OneDrive_1_1-4-2022/TLS10.1.2_FIR_5GUG-20_N50_1.1.xlsx";
//    	 for(ValidationReport error:excelValidation.validate(path))
//    		 System.out.println(error.getType()+" "+error.getMessage());
//    	
//	     path="C:/Users/Glen/Fulton-Hogan/WorkoutFiles/OneDrive_1_1-4-2022/TLS10.1.2_FIR_5GUG-20_N50_1.1.xlsx";
//	     excelParser.loadFilesToDB(path);
//	    	
	    String mapInfoPath="C:/Users/Glen/Fulton-Hogan/WorkoutFiles/OneDrive_1_1-4-2022/5GUG-20_IDD_S18_1.1.zip";
//	    System.out.println(FileUnzipper.unzip(mapInfoPath));
    	
    	FileUnzipper.unzip(mapInfoPath);
   }
}
