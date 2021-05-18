package proximity.persistenceServices.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.google.inject.servlet.RequestScoped;

import proximity.persistence.model.Category;
import proximity.persistence.model.Comment;
import proximity.persistence.model.Place;
import proximity.persistence.model.User;
import proximity.persistenceServices.PlacePersistenceService;
import proximity.utils.ObjectUtils;
import proximity.xml.model.HeatQuery;

@RequestScoped
public class JPAPlaceService implements PlacePersistenceService {

	@Inject
	private EntityManager entityManager;

	public JPAPlaceService() {
	}

	@Transactional
	public void postNewPlace(Place place, Comment comment, int categoryId)
			throws RuntimeException {

		Category category = entityManager.find(Category.class, categoryId);

		comment.setCategory(category);

		entityManager.persist(place);
		entityManager.persist(comment);
	}

	@Transactional
	public void commentPlace(Comment comment, int placeId, User user, int categoryId, long heatOverTime)
			throws RuntimeException {

		Place place = entityManager.find(Place.class, placeId);

		comment.setPlace(place);

		place.getComments().add(comment);
		place.setHeatOverTime(Math.max(place.getHeatOverTime(), heatOverTime));
		place.setCantRating(place.getCantRating()+1);
		place.setTotalRating(place.getTotalRating()+comment.getRating());

		comment.setUser(user);
		
		if(categoryId > 0){
			Category c = entityManager.find(Category.class, categoryId);
			comment.setCategory(c);
		}
		
		entityManager.persist(place);
		entityManager.persist(comment);

	}

	@Override
	public List<Place> whatIsHot(HeatQuery hq) throws RuntimeException {
		long time = new Date().getTime();
		List<Place> ret = (List<Place>)entityManager.createQuery("SELECT DISTINCT p FROM  Place p JOIN p.comments com JOIN com.category c " +
											  	"WHERE c.categoryId IN :categories AND " +
											  		"p.creationTime > :after AND " +
													"(p.heatOverTime > :time) AND " +
													"(p.latitude >= :minLat) AND (p.latitude < :maxLat) AND " +
													" (p.longitude >= :minLon) AND (p.longitude < :maxLon)")
				.setParameter("categories", ObjectUtils.arrayToCollection(hq.catId))
				.setParameter("time", time)
				.setParameter("after", hq.after)
				.setParameter("minLat", hq.minLat)
				.setParameter("maxLat", hq.maxLat)
				.setParameter("minLon", hq.minLon)
				.setParameter("maxLon", hq.maxLon)
				.getResultList();

		return ret;
	}

	// TODO what is hot
}
