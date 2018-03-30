import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase; 
import com.mongodb.client.model.Projections;
import com.mongodb.util.JSON;
import com.mongodb.*; 

public class AuthenticationDatabase 
{ 
  public MongoCollection<Document> collection;
  MongoClient client;
  
  public void connect()
  {	
	    client = new MongoClient("localhost", 27017);
		MongoDatabase database = client.getDatabase("blockchain");
	    collection = database.getCollection("users");
		
  } 
   
   public void insertUser(Object obj)
   {
	    String json =JavaJson.toJsonString(obj);
		BasicDBObject document = (BasicDBObject) JSON.parse(json);
		collection.insertOne(new Document(document));

   }
   public BlockUser getUser(String user_id)
   {
	   
	   BasicDBObject query = new BasicDBObject("user_id", user_id);
	   FindIterable<Document> iterable = collection.find(query).projection(Projections.excludeId());
		if(iterable.iterator().hasNext())
			return (BlockUser) JavaJson.toJavaObject(iterable.first().toJson(),BlockUser.class);
		return null;
   }
   
   
   public boolean deleteUser(String user_id)
   {
	   if(collection.deleteOne(new Document("user_id", user_id)).getDeletedCount()>0)
		   return true;
	   else 
		   return false;
   }
   
   public void close()
   {
	   client.close();
   }
  
}