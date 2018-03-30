import com.google.gson.Gson;
public class JavaJson
{

	
	public static String toJsonString(Object obj)
	{
		return new Gson().toJson(obj);
	}
	
	public static Object toJavaObject(String jsonString,Class c)
	{
		return new Gson().fromJson(jsonString,c);
	}

}