package proximity.xml.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserData {
	public String sessionId;

	public String userName;

	public String displayName;

	public int maximumDistance;

	public String image;

	public int[] preferedCategories;
}
