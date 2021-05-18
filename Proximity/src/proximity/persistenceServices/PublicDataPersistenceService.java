package proximity.persistenceServices;

import java.util.List;
import java.util.Set;

import proximity.persistence.model.Category;

public interface PublicDataPersistenceService {
	public Set<Category> getCategories(int[] categories) throws RuntimeException;

	public List<Category> getCategories() throws RuntimeException;
}
