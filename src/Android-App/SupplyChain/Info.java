package onepercent.tradechain;
import java.io.Serializable;


public class Info implements Serializable
{
	private static final long serialVersionUID = 7816173151353920112L;
	public int max_price;
	public int quantity;
	public Info()
	{
		max_price=0;
		quantity=0;
	}
}
