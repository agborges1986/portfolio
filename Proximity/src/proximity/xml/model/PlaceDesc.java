package proximity.xml.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import proximity.persistence.model.Comment;
import proximity.persistence.model.Place;

@XmlRootElement
public class PlaceDesc {

	public int cantRating;

	public int[] categories;

	public CommentDesc[] comments;

	public long creationTime;

	public long heatOverTime;

	public double latitude;

	public double longitude;

	public String name;

	public int totalRating;

	public int id;
	
	public PlaceDesc(Place p, int[] allowedCategories){
		cantRating = p.getCantRating();
		
		//categories = new int[p.getCategories().size()];
		ArrayList<Integer> cats = new ArrayList<Integer>();
		
		ArrayList<CommentDesc> commDesc = new ArrayList<CommentDesc>();
		for(Comment c : p.getComments()){
			if(contains(allowedCategories, c.getCategory().getCategoryId())){
				commDesc.add(new CommentDesc(c));
				if(!cats.contains(c.getCategory().getCategoryId())){
					cats.add(c.getCategory().getCategoryId());
				}
			}
		}
		comments = commDesc.toArray(new CommentDesc[0]);
		
		creationTime = p.getCreationTime();
		heatOverTime = p.getHeatOverTime();
		latitude = p.getLatitude();
		longitude = p.getLongitude();
		name = p.getName();
		totalRating = p.getTotalRating();
		id = p.getPlaceId();
	}
	
	private static boolean contains(int[] arr, int val){
		for(int i = 0; i < arr.length; i++){
			if(arr[i] == val)
				return true;
		}
		return false;
	}
	
	public PlaceDesc(){}
}
