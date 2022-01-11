package com.glen.ExcelParser.Constants;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public interface FIRDatabaseConstants {
	public static final int[] SHEET_INDEXES_TO_USE= {2,3,4};
	public static final int CELL_NULL_RANGE_START_INDEX = 0;
	public static final int CELL_NULL_RANGE_STOP_INDEX = 5;
	
	/*TODO-Remember to populate the corresponding database table name in SHEET_COLS_TO_DB_COLS
	 * For each entry in COLS_TO_INCLUDE_FROM_SHEETS
	 */
	public static final Map<Integer, String[]> COLS_TO_INCLUDE_FROM_SHEETS =
	new HashMap<Integer,String[]>(){{
		put(2, new String[] {
			"FIR Pit Reference Number",
			"tls_id",
			"type"
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
	
	public static final Map<String,String> SHEET_COLS_TO_DB_COLS = 
			new HashMap<String,String>() {{
				put("FIR Pit Reference Number","fir_pit_reference_number");
				put("tls_id","tls_id");
				put("type","type");
				put("FIR Duct Reference Number","fir_duct_reference_number");
				put("tls_id_route","tls_id_route");
				put("tls_id_conduit","tls_id_conduit");
				put("LIC Reference Number","lic_reference_number");
				put("LOC_ID","loc_id");
				put("Ref_ID","ref_id");
			}};
	
	public static final Map<Integer, String> SHEET_TO_TABLE = 
		new HashMap<Integer,String>(){{
		put(2, "fir_pits");
		put(3, "fir_duct");
		put(4, "fir_lics");
	}};
	
	
			
}
