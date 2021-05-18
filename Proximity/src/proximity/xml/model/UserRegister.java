package proximity.xml.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserRegister {
	public int userId;

	public String displayName;

	public int maximumDistance;

	public String password;

	public String userName;

	public int[] categories;
}
