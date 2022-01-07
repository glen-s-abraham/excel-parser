package com.glen.ExcelParser;



import com.glen.ExcelParser.services.FIRExcelToDatabaseService;
import com.glen.ExcelParser.services.FileToDatabaseLoaderService;
import com.glen.ExcelParser.utils.FileUnzipper;

/**
 * Hello world!
 *
 */
public class App 
{
	
	private static FileToDatabaseLoaderService excelParser=new FIRExcelToDatabaseService();
	
    public static void main( String[] args )
    {
        String path="C:/Users/Glen/Fulton-Hogan/WorkoutFiles/OneDrive_1_1-4-2022/TLS10.1.2_FIR_5GUG-20_N50_1.1.xlsx";
        excelParser.loadFilesToDB(path);
        String mapInfoPath="C:/Users/Glen/Fulton-Hogan/WorkoutFiles/OneDrive_1_1-4-2022/5GUG-20_IDD_S18_1.1.zip";
        String compressPath="C:/Users/Glen/Fulton-Hogan/WorkoutFiles/uploadTest/";
        //System.out.println(FileUnzipper.unzip(mapInfoPath));
    }
}
