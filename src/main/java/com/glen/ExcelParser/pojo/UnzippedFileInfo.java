package com.glen.ExcelParser.pojo;

import java.util.List;

public class UnzippedFileInfo {
	private String fileName;
	private String path;
	private int noOfFiles;
	private List<String> files;
	
	public UnzippedFileInfo(Builder builder) {
		this.fileName = builder.fileName;
		this.path = builder.path;
		this.noOfFiles = builder.noOfFiles;
		this.files = builder.files;
	}
	
	public static class Builder{
		private final String fileName;
		private final String path;
		private int noOfFiles;
		private List<String> files;
		public Builder(String fileName, String path) {
			this.fileName = fileName;
			this.path = path;
		}
		public Builder setNoOfFiles(int noOfFiles) {
			this.noOfFiles = noOfFiles;
			return this;
		}
		public Builder attachFiles(List<String> files) {
			this.files = files;
			return this;
		}
		public  UnzippedFileInfo build() {
			return new UnzippedFileInfo(this);
		}
		
	}
	public String getFileName() {
		return fileName;
	}
	public String getPath() {
		return path;
	}
	public int getNoOfFiles() {
		return noOfFiles;
	}
	public List<String> getFiles() {
		return files;
	}
	@Override
	public String toString() {
		return "Builder [fileName=" + fileName + ", path=" + path + ", noOfFiles=" + noOfFiles + ", files=" + files
				+ "]";
	}
	
	
	
}
