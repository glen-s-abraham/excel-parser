package com.glen.ExcelParser.Constants;

import java.util.HashMap;

public interface FIRDatabaseConstants {
	public static final int[] SHEET_INDEXES_TO_USE= {2,3,4};
	public static final int CELL_NULL_RANGE_START_INDEX = 0;
	public static final int CELL_NULL_RANGE_STOP_INDEX = 5;
	
	public static final HashMap<Integer, String[]> COLS_TO_INCLUDE_FROM_SHEETS =
	new HashMap<Integer,String[]>(){{
		put(2, new String[] {
			"FIR Pit Reference Number",
			"tls_id",
		});
		put(3,new String[] {
			"FIR Duct Reference Number",
			"tls_id_route",
			"tls_id_conduit"
		});
		put(4,new String[] {
				"LIC Reference Number",
				"LOC_ID",
				"Ref_ID"	
		});	
	}};
	
	public static final HashMap<Integer, String> SHEET_TO_TABLE = 
		new HashMap<Integer,String>(){{
		put(2, "fir_pits");
		put(3, "fir_duct");
		put(4, "fir_lics");
	}};
	
}
