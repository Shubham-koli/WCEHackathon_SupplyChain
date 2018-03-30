import java.io.*;
import java.net.*;
import java.util.*;

public class ReceiveBlock extends Thread
{
	Database db;
	ReceiveBlock()
	{
		db=new Database();
		db.connect();
		this.start();
		System.out.print("\nBlock Receiver Service initialized...");
	}
	public void run()
	{
		Config.receiverLock=false;
		ServerSocket commandSocket;
		InputStream is;
		OutputStream os;
		DataInputStream din;
		DataOutputStream dos;
		try{
		    commandSocket=new ServerSocket(Config.commandPort);
			String command;
			Socket skt;
			while(true)
			{
				skt=commandSocket.accept();
				is=skt.getInputStream();
				din = new DataInputStream(is);	         
		        command=din.readUTF().trim();
		        os=skt.getOutputStream();
				dos=new DataOutputStream(os);
		    			
				if(!Config.receiverLock && command.equals(Config.INIT))
				{
					Config.receiverLock=true;
					dos.writeUTF(Config.READY);
					dos.flush();					
					new Receive(db).start();
				}
				else
				{
					dos.writeUTF(Config.BUSY);
					dos.flush();
				}
				
				 is.close();
			     din.close();
			     os.close();
			     dos.close();
			     skt.close();
			     
			}
			
		}
		catch(Exception e)
		{
			System.out.print("\n"+e.getLocalizedMessage());
		}
	}
	
}


class Receive extends Thread
{
	Database db;
		Receive(Database db)
		{
			this.db=db;
		}
						public void run()
						{
							ServerSocket ssocket=null;
							try 
							{
							  ssocket = new ServerSocket(Config.port);
							  Socket socket=ssocket.accept();
							  socket.setSoTimeout(1000);
							  //getting payload
							  InputStream is=socket.getInputStream();
							  ObjectInputStream oos = new ObjectInputStream(is);
							 
							  //extracting block object							  
							  Block b = (Block)oos.readObject();
							  System.out.print("\nBlock Received from:"+socket.getInetAddress().getHostAddress());
							 if(b.updateBlock)
							 {
								 db.update(b.BATCH_ID, b);
							 }
							 else
							 {
								 db.insertData(b);
							 }
							is.close();
							oos.close();
							socket.close();
							ssocket.close();
							 
							} 
							catch(Exception e)
							{
								e.printStackTrace();								
								Config.receiverLock=false;
								System.out.print("Transaction cancelled");
							}
							Config.receiverLock=false;
						}
					
}
	

