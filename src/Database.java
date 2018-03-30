import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase; 
import com.mongodb.client.model.Projections;
import com.mongodb.util.JSON;
import com.mongodb.*; 

public class Database 
{ 
  public MongoCollection<Document> collection;
  MongoClient client;
  
  public void connect()
  {	
	    client = new MongoClient("localhost", 27017);
		MongoDatabase database = client.getDatabase("blockchain");
	    collection = database.getCollection("blocks");
		
  } 
   
   public void insertData(Object obj)
   {
	    String json =JavaJson.toJsonString(obj);
		BasicDBObject document = (BasicDBObject) JSON.parse(json);
		collection.insertOne(new Document(document));

   }
   public Block getData(String BATCH_ID)
   {
	   
	   BasicDBObject query = new BasicDBObject("BATCH_ID", BATCH_ID);
	  // FindIterable<Document> iterable = collection.find(Document.parse("{\"id\": \"10\"}"));
	  FindIterable<Document> iterable = collection.find(query).projection(Projections.excludeId());
		if(iterable.iterator().hasNext())
			return (Block) JavaJson.toJavaObject(iterable.first().toJson(),Block.class);
		return null;
   }
   
   public List<Block> getAllBlocks()
   {
	   List<Block> l=new ArrayList<Block>();
	   FindIterable<Document> cursor = collection.find().projection(Projections.excludeId());
	   MongoCursor<Document> i=cursor.iterator();
	   while (i.hasNext())
	   {
		   cursor.iterator().next().toJson();
	      l.add((Block) JavaJson.toJavaObject(i.next().toJson(),Block.class));
	      //do your thing
	   }
	   return l;
   }
   public void update(String BATCH_ID,Block obj)
   {
	   String json =JavaJson.toJsonString(obj);
	   delete(BATCH_ID);
	   insertData(obj);
   }
   public boolean delete(String BATCH_ID)
   {
	   if(collection.deleteOne(new Document("BATCH_ID", BATCH_ID)).getDeletedCount()>0)
		   return true;
	   else 
		   return false;
   }
   
   public void close()
   {
	   client.close();
   }
  
}