package proximity.webService;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import proximity.persistenceServices.ImagePersistenceService;
import proximity.utils.FormNames;

import com.google.inject.Inject;

@Path(FormNames.imageP)
public class ImageWebService extends WebService{
	@Inject
	private ImagePersistenceService imageService;

	@GET
	@Path("/{image}")
	@Produces("image/*")
	public Response getImage(@PathParam("image") String image) {

		File f = imageService.getImage(image);

		if (!f.exists()) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}

		return buildResponse(f, "image/jpeg");
	}
}
