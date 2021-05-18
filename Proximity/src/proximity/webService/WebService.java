package proximity.webService;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import proximity.persistence.model.User;
import proximity.persistenceServices.UserPersistenceService;

import com.google.inject.Inject;

public abstract class WebService {

	@Inject
	protected UserPersistenceService userService;

	@Inject
	protected SessionHolder sessions;

	protected Response buildResponse(Object entity){
		return Response.ok(entity).build();
	}

	protected Response buildResponse(Object entity, MediaType mediaType){
		return Response.ok(entity, mediaType).build();
	}

	protected Response buildResponse(Object entity, String mediaType){
		return Response.ok(entity, mediaType).build();
	}

	protected User getAuthenticatedUser(String sessionId) {
		int userId = sessions.retrieve(sessionId);
		if(userId > 0){
			return userService.getUserById(userId);
		}
		return null;
	}

	protected String setAuthenticatedUser(User user) {
		return sessions.store(user.getUserId());
	}

	protected void closeSession(String sessionId) {
		sessions.remove(sessionId);
	}

}
