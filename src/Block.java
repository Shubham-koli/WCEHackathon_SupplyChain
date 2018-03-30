import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.io.Serializable;

import onepercent.tradechain.Info;


class Block implements Serializable
{
	public String BATCH_ID; 
	private int BLOCK_STATUS;
	private int BLOCK_NUMBER;
	private String PREVIOUS_HASH;
	private String TIMESTAMP;
	public boolean updateBlock;
	private HashMap<String,BlockData> DATA;


	
	Block(String batch_id,int block_number,String previous_hash,long timestamp)
	{
		BATCH_ID=batch_id;
		BLOCK_NUMBER=block_number;
		DATA=new HashMap<String,BlockData>();
		TIMESTAMP=Long.toString(timestamp);
		PREVIOUS_HASH="null";
		
	}
	
	private String getCurrentDate()
	{
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/M/yyyy");  
		 LocalDateTime now = LocalDateTime.now();  
		 return dtf.format(now);
	}
	
	HashMap<String,String> getSubBlockData(String id)   //block id and data
	{
				if(DATA.containsKey(id))
					return DATA.get(id).getData();
				return null;
	}
	
	void initBatch(String owner_id,String owner_name,String product_name,String owner_type,int quantity,String produce_date,String expiry_date,String location,String gst_number,String comment)
	{
			DATA.put(owner_id,new BlockData(owner_id,"null",owner_name,product_name,owner_type,quantity,"null",produce_date,expiry_date,location,gst_number,comment));
	}
	
	boolean sell(String current_owner_id,String new_owner_id,String new_owner_name,String new_owner_type,String product_name,int quantity,String buy_date,String produce_date,String expiry_date,String location,String gst_number,String comment)
	{
		if(DATA.get(current_owner_id)!=null)
		{
			int available_quantity=DATA.get(current_owner_id).getAvailableQuantity();
		    if(available_quantity>=quantity)
			{
				updateSubBlockQuantity(current_owner_id,quantity);
				DATA.put(new_owner_id,new BlockData(new_owner_id,current_owner_id,new_owner_name,new_owner_type,product_name,quantity,buy_date,produce_date,expiry_date,location,gst_number,comment));
				return true;
			}
		}
			return false;
	}
			
	public void getRetailerInfo(String location,String product,Info inf)
	{
		Collection l=DATA.values();
		Iterator<BlockData> i=l.iterator();
		while(i.hasNext())
		{
			BlockData b=(BlockData)i.next();
			if(b.getProduct().equalsIgnoreCase(product) && b.getLocation().equalsIgnoreCase(location) && b.getUserType().equals(BlockUser.RETAILER))
			{
				if(b.getAvailableQuantity()>0)
				{
					inf.quantity=inf.quantity+b.getAvailableQuantity();
					if(inf.max_price < b.getPrise())
					{
						inf.max_price=b.getPrise();
					}
				}
			}
		}

	}
	
	public void getWholesalerInfo(String location,String product,Info inf)
	{
		Collection l=DATA.values();
		Iterator<BlockData> i=l.iterator();
		while(i.hasNext())
		{
			BlockData b=(BlockData)i.next();
			if(b.getProduct().equalsIgnoreCase(product) && b.getLocation().equalsIgnoreCase(location) && b.getUserType().equals(BlockUser.WHOLESALER))
			{
				if(b.getAvailableQuantity()>0)
				{
					inf.quantity=inf.quantity+b.getAvailableQuantity();
					if( inf.max_price < b.getPrise())
					{
						inf.max_price=b.getPrise();
					}
				}
			}
		}
	}
	
	private void updateSubBlockQuantity(String user_id,int item_selled)
	{
		DATA.get(user_id).updateQuantity(item_selled);		
	}
	
}