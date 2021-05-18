package proximity.xml.model;

import javax.xml.bind.annotation.XmlRootElement;

import proximity.persistence.model.Comment;

@XmlRootElement
public class CommentDesc {
	public long creationTime;

	public byte rating;

	public String text;

	public String user;

	public String image;
	
	public int categoryId;
	
	public CommentDesc(Comment c){
		creationTime = c.getCreationTime();
		rating = c.getRating();
		text = c.getText();
		user = c.getUser().getDisplayName();
		image = c.getImage();
		categoryId = c.getCategory().getCategoryId();
	}
	
	public CommentDesc(){}
}
