package proximity.persistenceServices;

import java.util.List;

import proximity.persistence.model.Comment;
import proximity.persistence.model.Place;
import proximity.persistence.model.User;
import proximity.xml.model.HeatQuery;

public interface PlacePersistenceService {
	public void postNewPlace(Place place, Comment comment, int categoryId)
			throws RuntimeException;

	public void commentPlace(Comment comment, int placeId, User user, int categoryId, long heatOverTime)
			throws RuntimeException;

	public List<Place> whatIsHot(HeatQuery hq) throws RuntimeException;
}
