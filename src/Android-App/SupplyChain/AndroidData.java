package onepercent.tradechain;

import java.io.Serializable;

public class AndroidData implements Serializable
{
	private static final long serialVersionUID = -8014937669975554490L;
	//function types
	public static String INIT_BATCH="initBatch";
	public static String SELL="sell";
	public static String GET_CHAIN_DATA="getchaindata";
	public static String GET_LOCATION_DATA="getlocationdata";
	
	//data fields
	public String function;
	public String batchId;
	public int quantity;         
	public int selled_quantity;
	public String owner_id;
	public String owner_name;
	public String owner_type;
	public String new_owner_id;
	public String new_owner_name;
	public String new_owner_type;
	public String product_name;
	public String sell_date;
	public String produce_date;
	public String expiry_date;	
	public String location;
	public String gst_number;
	public String comment;
}