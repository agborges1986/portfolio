package proximity.persistenceServices.jpa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import proximity.persistence.model.Category;
import proximity.persistenceServices.PublicDataPersistenceService;
import proximity.utils.ObjectUtils;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

@RequestScoped
public class JPAPublicDataPersistenceService implements
		PublicDataPersistenceService {

	@Inject
	EntityManager entityManager;

	public JPAPublicDataPersistenceService() {
	}

	@Override
	public Set<Category> getCategories(int[] categories) throws RuntimeException{
		if (categories == null)
			return new HashSet<Category>();

		HashSet<Category> ret = new HashSet<Category>(categories.length);

		ret.addAll((List<Category>) entityManager
				.createQuery(
						"select c from Category c where c.categoryId in :list")
				.setParameter("list", ObjectUtils.arrayToCollection(categories))
				.getResultList());

		return ret;
	}

	@Override
	public List<Category> getCategories()  throws RuntimeException{
		return (List<Category>) entityManager.createQuery(
				"select c from Category c").getResultList();
	}
}
