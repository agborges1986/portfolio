package proximity.xml.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HeatQuery {
	public double minLat;
	public double maxLat;
	public double minLon;
	public double maxLon;
	
	public long after;
	public int[] catId;
	
	public int id;
}
