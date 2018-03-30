package onepercent.tradechain;

import java.io.Serializable;


public class UserCredential implements Serializable
{
    private static final long serialVersionUID = 6893725863937426763L;
    public String user_id;
    public String password;
    public String type;
    public String owner_name;
    public boolean response;

}
