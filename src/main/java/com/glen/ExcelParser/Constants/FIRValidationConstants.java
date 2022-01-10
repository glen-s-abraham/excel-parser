package com.glen.ExcelParser.Constants;

import java.util.HashMap;
import java.util.Map;

public interface FIRValidationConstants {
	
	public static final int REQUIRED_NO_OF_SHEETS = 8;
	public static final int CELL_NULL_RANGE_START_INDEX = 0;
	public static final int CELL_NULL_RANGE_STOP_INDEX = 1;
	@SuppressWarnings("serial")
	public static final Map<Integer, String> SHEET_INDEX_TO_NAMES_REQUIRED = 
			new HashMap<Integer,String>(){{
				put(0,"Cover Page");
				put(1,"Document Control");
				put(2,"Pits");
				put(3,"Ducts");
				put(4,"LICs");
				put(5,"ListValues");
				put(6,"Definitions");
				put(7,"System Impacts");
			}};
			
	public static final String[] COVER_SHEET_SLUGS= {
			"nbn-confidential-commercial",
			"fieldinspectionreport(n2p)",
			"documentnumber",
			"documentcategory",
			"author",
			"approver(owner)",
			"status",
			"issuedate",
			"revisionnumber",
			"start-date",
			"end-date"
	};
	
	public static final String VALIDATION_TYPE_ERROR="Error";
	public static final String VALIDATION_TYPE_WARNING="Warning";
	
	public static final String ERR_MSG_FILE_SHEETS_LESS_THAN_REQUIRED ="File doesn't have the required number of sheets";
	public static final String ERR_MSG_SHEET_INDEX_DOESNT_MATCH_TITLE_ORDER="The Sheet doesn't match the required order:";
	public static final String ERR_MSG_MISSING_FIELDS = "Cover Page is missing field:";
}
