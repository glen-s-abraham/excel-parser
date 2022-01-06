package com.glen.ExcelParser.utils;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.glen.ExcelParser.pojo.UnzippedFileInfo;

import net.lingala.zip4j.ZipFile;

public class FileUnzipper {
	
	private static final String DESTINATION_BASE_PATH = "C:/Users/Glen/Fulton-Hogan/WorkoutFiles/uploadTest/";
	private static final String FILE_EXTENSION = ".zip";
	
	public static UnzippedFileInfo unzip(String sourceFile) {
		try {
			ZipFile zipFile=new ZipFile(sourceFile);
			String dirName = zipFile.getFile().getName()
					.replace(FILE_EXTENSION,"");
			String destinationPath =DESTINATION_BASE_PATH+dirName;
			zipFile.extractAll(destinationPath);
					
			return new UnzippedFileInfo
					.Builder(zipFile.getFile().getName(), destinationPath)
					.setNoOfFiles(zipFile.getFileHeaders().size())
					.attachFiles((zipFile.getFileHeaders().stream()
							.map(el->el.getFileName()).
							collect(Collectors.toList()))				
					).build();
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
