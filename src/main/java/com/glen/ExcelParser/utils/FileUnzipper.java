package com.glen.ExcelParser.utils;

import net.lingala.zip4j.ZipFile;

public class FileUnzipper {
	
	private static final String DESTINATION_BASE_PATH = "C:/Users/Glen/Fulton-Hogan/WorkoutFiles/uploadTest/";
	private static final String FILE_EXTENSION = ".zip";
	
	public static String unzip(String sourceFile) {
		try {
			ZipFile zipFile=new ZipFile(sourceFile);
			String dirName = zipFile.getFile().getName()
					.replace(FILE_EXTENSION,"");
			String destinationPath =DESTINATION_BASE_PATH+dirName;
			zipFile.extractAll(destinationPath);
			return destinationPath;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
