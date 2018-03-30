import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.io.*;

import onepercent.tradechain.*;

public class DeployBlock extends Thread
{
	public static Vector<String> active_nodes;
	public static int flag;  //checks whether transction successfull
	public static int flag2;  //check for valid transaction
	public static Vector<InetAddress> myIpList;
	public Database db;
	public DeployBlock()
	{
		Config.SenderLock=0; 
		db=new Database();
		db.connect();
		this.start();
		System.out.print("\nBlock Deployment Service initialized...");
	}

	//android data fetch
	public void run()
	{
		try
		{
			ServerSocket serversocket=new ServerSocket(Config.androidPort);
			Socket socket;
		while(true)
		{
			socket=serversocket.accept();
			InputStream is=socket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);			  							  
			AndroidData androidData = (AndroidData)ois.readObject();
			OutputStream os=socket.getOutputStream();
		
			if(androidData.function.equals(AndroidData.GET_CHAIN_DATA))
			{
				ObjectOutputStream oos=new ObjectOutputStream(os);
				Vector<HashMap<String,String>> v=new Vector<HashMap<String,String>>();
				Block b=db.getData(androidData.batchId);
				if(b!=null)
				{
					HashMap<String,String> data=b.getSubBlockData(androidData.owner_id);
					data=b.getSubBlockData(data.get(BlockData.LAST_OWNER_ID));
					if(data!=null)
					{
					
						v.add(data);
						do{
							data=b.getSubBlockData(data.get(BlockData.LAST_OWNER_ID));
							v.add(data);
						}while(!data.get(BlockData.LAST_OWNER_ID).equals("null"));
					}
					
				}
				if(!v.isEmpty())
				{
					oos.writeObject(v);
				}
				else
				{
					HashMap<String,String> replay=new HashMap<String,String>();
					replay.put("ISEMPTY", "YES");
					v.add(replay);
					oos.writeObject(v);
				}
				
				oos.close();
			}
			else if(androidData.function.equals(AndroidData.GET_LOCATION_DATA))
			{
			
				Iterator i=db.getAllBlocks().iterator();
				Block b=null;
				Info inf1=new Info();
				if(androidData.owner_type.equals(BlockUser.RETAILER))
				{
					while(i.hasNext())
					{
						b=(Block) i.next();
						b.getRetailerInfo(androidData.location, androidData.product_name, inf1);
					}
				}
				else
				{
					while(i.hasNext())
					{
						b=(Block) i.next();
						b.getWholesalerInfo(androidData.location, androidData.product_name, inf1);
					}
				}
				ObjectOutputStream oos=new ObjectOutputStream(os);
				oos.writeObject(inf1);
			}
			else
			{
				DataOutputStream dos=new DataOutputStream(os);
				dos.writeUTF(Boolean.toString(send(androidData,1)));	//deploy to nodes and response to android
				Config.SenderLock=0;			
				dos.close();
			}
			is.close();
			ois.close();
			os.close();
			socket.close();
		}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	

	
	public static void getLocalIps()
	{
		myIpList=new Vector<InetAddress>();
		try{
	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        while (interfaces.hasMoreElements())
	        {
	            NetworkInterface networkInterface = interfaces.nextElement();
	            // drop inactive
	            if (!networkInterface.isUp())
	                continue;
	
	            // smth we can explore
	            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
	            while(addresses.hasMoreElements())
	            {
		             myIpList.add(addresses.nextElement());
	            }
	        }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
	
	
	
	public boolean send(AndroidData data,int blockNumber)
	{
		getLocalIps(); //for excluding to block transmmition over localhost
		flag=0;
		flag2=0;
		active_nodes=new Vector<String>();
	
	     if(Config.SenderLock==0) //check if same node is sending block
			{
	    	 Config.SenderLock=1;
	    	 
	    	 //getting response from all nodes in network
	    	
	    	 
	    	 //send block to alive nodes
	    	 Block b = null;
				if(data.owner_type.equals(BlockUser.FARMER) && data.function.equals(AndroidData.INIT_BATCH) && db.getData(data.batchId)==null)
				{
					b=new Block(data.batchId,blockNumber,"ds4f5sd4fsdfds",System.currentTimeMillis());
					b.updateBlock=false;
					
					b.initBatch(data.owner_id,
							data.owner_name,
							data.owner_type,
							data.product_name,						
							data.quantity,
							data.produce_date,
							data.expiry_date,
							data.location,
							data.gst_number,
							data.comment);				
					db.insertData(b);
					flag2=1;
					
				}
				if(db.getData(data.batchId)!=null && !data.function.equals(AndroidData.INIT_BATCH))
				{
					b=db.getData(data.batchId);
					b.updateBlock=true;
					if(b.sell(data.owner_id,
							data.new_owner_id,
							data.new_owner_name,
							data.new_owner_type,
							data.product_name,
							data.selled_quantity,
							data.sell_date,
							data.produce_date,
							data.expiry_date,
							data.location,
							data.gst_number,
							data.comment))
					{
						flag2=1;	
					db.update(data.batchId, b);
					}
					
		
				}
				if(flag2==1)
				{
				
					 getResposeFromAllNodes();
					 System.out.print("\nNo of nodes active:"+active_nodes.size());
			    	 pushBlock(data,blockNumber,b);		
			    	 if(flag==1 && flag2==1)
				    	 return true;
				 	 else
						 return false;
				}
				return false;
			}
	     
	     return false;
	
	}
	
	
	//get available nodes
	void getResposeFromAllNodes()
	{
		int i;
		 try{
			  ThreadGroup tg1 = new ThreadGroup("responseNodeThreadGroup");
				for(i=1;i<255;i++) 
				{
				  final int x=i;
			    		                  
							
			    		 Runnable runnable=new Runnable(){
												
								public void run()
								{
								try{
								InetAddress host = InetAddress.getByName(Config.networkId+"."+Integer.toString(x));
								if(!myIpList.contains(host) && host.isReachable(5000) && !host.equals(InetAddress.getByName("192.168.100.10")))
								{
								
								 Socket client = new Socket(host,Config.commandPort);
								
						         OutputStream os = client.getOutputStream();
						         DataOutputStream dos = new DataOutputStream(os);	         
						         dos.writeUTF(Config.INIT);								 
						         InputStream is = client.getInputStream();
						         DataInputStream dis = new DataInputStream(is);	
								 String response=dis.readUTF().trim();
								 System.out.print("\nAvailable : "+host.getHostAddress()+" : "+response);   //prints response of host
						         if(response.equals(Config.READY))
						         {
						        	 active_nodes.add(host.getHostAddress());
						         }
								 is.close();
						         dis.close();
								 os.close();
						         dos.close();
						         client.close();
								}
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								}
								
							};
							 new Thread(tg1,runnable,"").start();  
						
				}
				while(tg1.activeCount()>0)
				{
					Thread.sleep(1000);
				}
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
	}
	
	//deploy block on available nodes
	void pushBlock(AndroidData data,int blockNumber,Block b)
	{
		final int i=active_nodes.size();
		ThreadGroup tg1 = new ThreadGroup("Block send Thread Group");
		     //Block marshling
			
			 //finalize block to transmmit
		     final Block block=b; 
			 try{
					for(int j=0;j<i;j++)
					{
					  final String node_address=active_nodes.get(j);
					 
					  Runnable runnable=new Runnable(){							
							public void run()
							{
								try
								{
								InetAddress host = InetAddress.getByName(node_address);
								
									if(host.isReachable(5000))
									{
									   Socket client = new Socket(host,Config.port);
									   if(flag2!=0)
									   {
										//send block to node
										 client.setSoTimeout(5000);
								         OutputStream os = client.getOutputStream();
								         ObjectOutputStream oos=new ObjectOutputStream(os);
								         oos.writeObject(block);
								         client.close();
								         os.close();
								         oos.close();
								         flag=1;
									   }
							       
							         
							         
									}
									else
									{
										 System.out.print("\nBlock Not Sent:"+node_address);
										 if(active_nodes.size()==1)
											 flag=0;
									}
								}
								catch(Exception e)
								{
									e.printStackTrace();
									System.out.print("\nBlock Not Sent:"+node_address);
									flag=0;
								}
							}
					  };
						 new Thread(tg1,runnable,"").start(); 
					}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Config.SenderLock=0;
			}
			Config.SenderLock=0;
		
		while(tg1.activeCount()>0)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}//class end





