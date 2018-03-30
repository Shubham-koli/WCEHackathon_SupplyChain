import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;
import onepercent.tradechain.UserCredential;

public class BlockUser implements Serializable
{
	public static String FARMER="farmer";
	public static String WHOLESALER ="wholesaler";
	public static String RETAILER="retailer";
	public static String CONSUMER="consumer";
	
	public static String ID="id";
	public static String NAME ="name";
	public static String GST_NUMBER="gst";
	public static String ADDRESS="address";
	public static String USER_TYPE="type";
	
	private String user_id;
	private String user_name;
	private String type;
	private String mobile_no;
	private String gst_number;
	private String address;
	private String password;
	
	
	
	public HashMap<String,String> getUserInfo()
	{
		HashMap<String,String> h=new HashMap<String,String>();
		h.put(BlockUser.ID,user_id);
		h.put(BlockUser.USER_TYPE, type);
		h.put(BlockUser.NAME,user_name);
		h.put(BlockUser.GST_NUMBER, gst_number);
		h.put(BlockUser.ADDRESS,address);
		return h;
	}
	
	
	public UserCredential validateUser(String user_id,String password)
	{
		UserCredential uc=new UserCredential();
		if(user_id.equals(this.user_id) && password.equals(this.password))
		{
			uc.response=true;
			uc.user_id=this.user_id;
			uc.type=this.type;
			uc.owner_name=this.user_name;
		}
		else
		{
			uc.response=false;
		}
		return uc;
	}
	
}


