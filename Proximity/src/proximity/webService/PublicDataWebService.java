package proximity.webService;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import proximity.persistence.model.Category;
import proximity.persistenceServices.PublicDataPersistenceService;
import proximity.utils.FormNames;
import proximity.xml.model.CategoryDesc;

@Path(FormNames.publicdataP)
public class PublicDataWebService extends WebService{
	@Inject
	private PublicDataPersistenceService dataService;

	@GET
	@Path(FormNames.categoriesP)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategories() {
		List<Category> list = dataService.getCategories();
		CategoryDesc[] ret = new CategoryDesc[list.size()];
		int i = 0;
		for (Category c : list) {
			CategoryDesc tmp = new CategoryDesc();
			tmp.id = c.getCategoryId();
			tmp.name = c.getName();
			tmp.description = c.getDescription();
			ret[i++] = tmp;
		}
		return buildResponse(ret);
	}
}
