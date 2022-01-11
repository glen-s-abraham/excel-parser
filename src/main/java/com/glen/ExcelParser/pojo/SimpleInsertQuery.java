package com.glen.ExcelParser.pojo;

import java.util.List;

public class SimpleInsertQuery {
	
	private String sqlQuery;
	
	public SimpleInsertQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public static class Builder{
		private final String tableName;
		private final List<String> columns;
		private List<String> values;
		
		public Builder(String tableName, List<String> columns) {
			this.tableName = tableName;
			this.columns = columns;
		}

		public Builder setValues(List<String> values) {
			this.values = values;
			return this;
		}
		
		public SimpleInsertQuery build() {
			String query = "INSERT INTO";
			query+=" "+tableName+"(";
			query+=parseColValuesToString(this.columns);
			query+=") values (";
			query+=parseColValuesToString(this.values);
			query+=")";
			return new SimpleInsertQuery(query);
		}

		private String parseColValuesToString(List<String> colValues) {
			String colStirng="";
			if(colValues==null||colValues.size()==0) {
				for(int i=0;i<columns.size();i++) {
					colStirng+="?";
					if(i!=columns.size()-1)
						colStirng+=",";
				}
			}
			else {
				for(int i=0;i<colValues.size();i++) {
					colStirng+=colValues.get(i);
					if(i!=colValues.size()-1)
						colStirng+=",";
				}
			}		
			return colStirng;
		}

		private String parseColValuestoStringWithSnakeLowerCase() {
			String colStirng="";
			for(int i=0;i<columns.size();i++) {
				String str=columns.get(i).replaceAll(" ","_");
				str = str.toLowerCase();
				colStirng+=str;
				if(i!=columns.size()-1)
					colStirng+=",";
			}
			return colStirng;
		}
		
	}

	public String getSqlQuery() {
		return sqlQuery;
	}
	
	
	
	
}
