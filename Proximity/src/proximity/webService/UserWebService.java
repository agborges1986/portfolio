package proximity.webService;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;

import proximity.persistence.model.Category;
import proximity.persistence.model.User;
import proximity.persistenceServices.ImagePersistenceService;
import proximity.persistenceServices.PublicDataPersistenceService;
import proximity.utils.FormNames;
import proximity.utils.ObjectUtils;
import proximity.xml.model.UserData;
import static proximity.utils.SecurityUtils.*;

@Path(FormNames.userP)
public class UserWebService extends WebService{
	@Inject
	private ImagePersistenceService imageService;

	@Inject
	private PublicDataPersistenceService dataService;

	@POST
	@Path(FormNames.loginP)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response authenticateUser(@FormDataParam(FormNames.userName) String userName, @FormDataParam(FormNames.password) String password) {
		User user = userService.getUserByUserName(userName);
		if (user != null
				&& passwordMatch(password, user.getPasswordHash())) {
			UserData userData = new UserData();
			ObjectUtils.map(userData, user);
			userData.preferedCategories = new int[user.getCategories().size()];
			userData.sessionId = setAuthenticatedUser(user);
			int i = 0;
			for (Category c : user.getCategories()) {
				userData.preferedCategories[i++] = c.getCategoryId();
			}
			return buildResponse(userData);
		} else {
			return Response.status(Status.UNAUTHORIZED).build();
		}
	}

	@POST
	@Path(FormNames.logoutP)
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response logOut(@FormDataParam(FormNames.sessionId) String sessionId) {
		closeSession(sessionId);
		return buildResponse("true");
	}

	@POST
	@Path(FormNames.editP)
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response editUserFORM(
			@FormDataParam(FormNames.displayName) String displayName,
			@FormDataParam(FormNames.maximumDistance) int maximumDistance,
			@FormDataParam(FormNames.password) String password,
			@FormDataParam(FormNames.userName) String userName,
			@FormDataParam(FormNames.image) InputStream image,
			@FormDataParam(FormNames.categoriesBD) List<FormDataBodyPart> categoriesBD,
			@FormDataParam(FormNames.sessionId) String sessionId) {

		User user = getAuthenticatedUser(sessionId);
		if(user == null){
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
		int[] categories;
		if(categoriesBD == null){
			categories = new int[0];
		}else{
			categories = new int[categoriesBD.size()];
			
			int i = 0;
			for (FormDataBodyPart fdbp : categoriesBD) {
				categories[i++] = Integer.parseInt(fdbp.getValue());
			}
		}

		user.setDisplayName(displayName);
		user.setMaximumDistance(maximumDistance);
		user.setPasswordHash(md5Hash(password));
		user.getCategories().addAll(dataService.getCategories(categories));

		if (userName != null && !userName.equals(user.getUserName())
				&& userService.getUserByUserName(userName) != null) {
			return Response.status(Status.CONFLICT).build();
		}

		user.setUserName(userName);
		if(image != null){
			user.setImage(this.imageService.saveImage(image));
		}

		userService.updateUser(user);

		return buildResponse("true");
	}

	private boolean passwordMatch(String password, String hash) {
		return hash.equals(md5Hash(password));
	}

	@POST
	@Path(FormNames.registerP)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response registerUserFORM(
			@FormDataParam(FormNames.displayName) String displayName,
			@FormDataParam(FormNames.maximumDistance) int maximumDistance,
			@FormDataParam(FormNames.password) String password,
			@FormDataParam(FormNames.userName) String userName,
			@FormDataParam(FormNames.categoriesBD) List<FormDataBodyPart> categoriesBD, // TODO
																				// fix
																				// this
			@FormDataParam("image") InputStream image) {

		int[] categories;
		if(categoriesBD == null){
			categories = new int[0];
		}else{
			categories = new int[categoriesBD.size()];
			
			int i = 0;
			for (FormDataBodyPart fdbp : categoriesBD) {
				categories[i++] = Integer.parseInt(fdbp.getValue());
			}
		}
		
		if (userService.getUserByUserName(userName) != null) {
			return Response.status(Status.CONFLICT).build();
		}
		User u = new User();

		u.setDisplayName(displayName);
		u.setMaximumDistance(maximumDistance);
		u.setPasswordHash(md5Hash(password));
		u.setUserName(userName);
		u.getCategories().addAll(dataService.getCategories(categories));

		String img = imageService.saveImage(image);
		u.setImage(img);

		userService.addNewUser(u);

		UserData ret = new UserData();
		ObjectUtils.map(ret, u);
		ret.sessionId = setAuthenticatedUser(u);
		ret.preferedCategories = new int[u.getCategories().size()];
		int i = 0;
		for (Category c : u.getCategories()) {
			ret.preferedCategories[i++] = c.getCategoryId();
		}

		return buildResponse(ret);
	}
}
