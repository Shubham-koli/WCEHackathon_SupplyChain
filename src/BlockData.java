import java.io.Serializable;
import java.util.HashMap;
public class BlockData implements Serializable
{	
	
	
	//Data frield Names
	public static String OWNER_ID="owner_id";
	public static String LAST_OWNER_ID="last_owner_id";
	public static String OWNER_NAME="owner_name";
	public static String OWNER_TYPE="owner_type";
	public static String PRODUCT_NAME="product_name";
	public static String BUYED_QUANTITY="buyed_quantity";
	public static String REMAINING_QUANTITY="remaining_quantity";
	public static String BUY_DATE="buy_date";
	public static String PRODUCE_DATE="produce_date";
	public static String EXPIRY_DATE="expiry_date";	
	public static String LOCATION="location";
	public static String GST_NUMBER="gst_number";
	public static String COMMENT="comment";
	
	//Actual Data Fields
	public String owner_id;
	public String last_owner_id;
	private String owner_name;
	private String owner_type;
	private String product_name;
	private int    buyed_quantity;
	private int    remaining_quantity;
	private String buy_date;
	private String produce_date;
	private String expiry_date;	
	private String location;
	private String gst_number;
	private String comment;
	
	
	BlockData(String owner_id,String last_owner_id,String owner_name,String owner_type,String product_name,int buyed_quantity,String buy_date,String produce_date,String expiry_date,String location,String gst_number, String comment)
	{
		
		this.owner_id=owner_id;
		this.last_owner_id=last_owner_id;
		this.owner_name=owner_name;
		this.owner_type=owner_type;
		this.product_name=product_name;
		this.buyed_quantity=buyed_quantity;
		this.remaining_quantity=buyed_quantity;
		this.buy_date=buy_date;
		this.produce_date=produce_date;
		this.expiry_date=expiry_date;
		this.location=location;
		this.gst_number=gst_number;
		this.comment=comment;		
	}
	
	HashMap<String,String> getData()
	{	
		HashMap<String,String> data=new HashMap<String,String>();
		data.put(BlockData.OWNER_ID,owner_id);
		data.put(BlockData.PRODUCT_NAME, product_name);
		data.put(BlockData.OWNER_NAME,owner_name);
		data.put(BlockData.OWNER_TYPE,owner_type);
		data.put(BlockData.LAST_OWNER_ID, last_owner_id);
		data.put(BlockData.BUYED_QUANTITY,Integer.toString(buyed_quantity));
		data.put(BlockData.REMAINING_QUANTITY,Integer.toString(remaining_quantity));
		data.put(BlockData.PRODUCE_DATE, produce_date);
		data.put(BlockData.BUY_DATE, buy_date);
		data.put(BlockData.EXPIRY_DATE, expiry_date);
		data.put(BlockData.LOCATION, location);
		data.put(BlockData.GST_NUMBER,gst_number);
		data.put(BlockData.COMMENT, comment);
		return data;
	}
	
	String getProduct()
	{
		return product_name;
	}
	
	String getLocation()
	{
		return location;
	}
	String getId()
	{
		return owner_id;
	}
	
	String getLastOwnerID()
	{
		return last_owner_id;
	}
	
	String getUserType()
	{
		return owner_type;
	}
	int getAvailableQuantity()
	{
		return remaining_quantity;
	}
	
	int getPrise()
	{
		return Integer.parseInt(comment);
	}
	void updateQuantity(int item_selled)
	{
		remaining_quantity=remaining_quantity-item_selled;
	}
}
