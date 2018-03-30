import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import onepercent.tradechain.UserCredential;


public class UserLogin extends Thread
{
	public static boolean loginLock;
	public void run()
	{
		int port=61111;
		AuthenticationDatabase adb=new AuthenticationDatabase();
		adb.connect();
		loginLock=false;
		try{
			System.out.print("Login service started\n");
			ServerSocket ss=new ServerSocket(port);
			while(true)
			{
				if(!loginLock)
				{
					 Socket s=ss.accept();
					// s.setSoTimeout(10000);
					 InputStream is=s.getInputStream();
					 ObjectInputStream ois=new ObjectInputStream(is);
					 UserCredential c=(UserCredential)ois.readObject();
					 OutputStream os=s.getOutputStream();
					 ObjectOutputStream oos=new ObjectOutputStream(os);
					 BlockUser b=adb.getUser(c.user_id);
					 if(b!=null)
					 {
						 oos.writeObject(b.validateUser(c.user_id, c.password));
					 }
					 else
					 {
						 UserCredential uc=new UserCredential();
						 uc.response=false;
						 oos.writeObject(uc);
					 }
					 ois.close();
					 is.close();
					 oos.close();
					 os.close();
					 s.close();
					 loginLock=false;
				}
			}
		}
		catch(Exception e)
		{
			loginLock=false;
			e.printStackTrace();
		}
	}
	
	public static void main(String s[])
	{
		new UserLogin().start();
	}

}
	

