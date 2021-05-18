package proximity.xml.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HeatResponse {
	public int id;
	public PlaceDesc[] places; 
}
