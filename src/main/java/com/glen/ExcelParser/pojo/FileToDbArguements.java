package com.glen.ExcelParser.pojo;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

public class FileToDbArguements {
	private String path;
	private Map<String, String>additionalDbEntries;

	public FileToDbArguements(Builder builder) {
		super();
		this.path = builder.path;
		this.additionalDbEntries = builder.additionalDbEntries;
	}


	public static class Builder{
		private final String path;
		private Map<String, String>additionalDbEntries;
		public Builder(String path) {
			super();
			this.path = path;
			additionalDbEntries = new HashedMap<String, String>();
		}
		public Builder addAdditionalDbEntries(String field,String value) {
			additionalDbEntries.put(field, value);
			return this;
		}
		public FileToDbArguements build() {
			return new FileToDbArguements(this);
		}
		
	}


	public String getPath() {
		return path;
	}


	public Map<String, String> getAdditionalDbEntries() {
		return additionalDbEntries;
	}
	
	
}
