package proximity.persistence.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "places")
public class Place implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "cant_rating")
	private int cantRating;

	@OneToMany(mappedBy = "place", fetch = FetchType.EAGER)
	private Set<Comment> comments;

	@Column(name = "creation_time", nullable = false)
	private long creationTime;

	@Column(name = "heat_duration")
	private long heatOverTime;

	@Column(nullable = false)
	private double latitude;

	@Column(nullable = false)
	private double longitude;

	@Column(nullable = false, length = 255)
	private String name;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "place_id", unique = true, nullable = false)
	private int placeId;

	@Column(name = "total_rating")
	private int totalRating;

	public Place() {
		comments = new HashSet<Comment>();
	}

	public int getCantRating() {
		return this.cantRating;
	}

	public Set<Comment> getComments() {
		return this.comments;
	}

	public long getCreationTime() {
		return this.creationTime;
	}

	public long getHeatOverTime() {
		return this.heatOverTime;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getName() {
		return this.name;
	}

	public int getPlaceId() {
		return this.placeId;
	}

	public int getTotalRating() {
		return this.totalRating;
	}

	public void setCantRating(int cantRating) {
		this.cantRating = cantRating;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public void setHeatOverTime(long heatOverTime) {
		this.heatOverTime = heatOverTime;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}

	public void setTotalRating(int totalRating) {
		this.totalRating = totalRating;
	}

}