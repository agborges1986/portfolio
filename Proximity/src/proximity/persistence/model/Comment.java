package proximity.persistence.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id", unique = true, nullable = false)
	private int commentId;

	@Column(name = "creation_time", nullable = false)
	private long creationTime;

	@Column(name = "image", nullable = true, length = 255)
	private String image;

	@ManyToOne
	@JoinColumn(name = "place_id", nullable = false)
	private Place place;

	private byte rating;

	@Column(length = 255)
	private String text;

	@ManyToOne
	@JoinColumn(name = "author_id", nullable = false)
	private User user;

	public Comment() {
	}

	public Category getCategory() {
		return category;
	}

	public int getCommentId() {
		return this.commentId;
	}

	public long getCreationTime() {
		return this.creationTime;
	}

	public String getImage() {
		return image;
	}

	public Place getPlace() {
		return this.place;
	}

	public byte getRating() {
		return this.rating;
	}

	public String getText() {
		return this.text;
	}

	public User getUser() {
		return this.user;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public void setRating(byte rating) {
		this.rating = rating;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setUser(User user) {
		this.user = user;
	}

}