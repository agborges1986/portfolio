package proximity.webService;

import java.io.InputStream;
import java.util.Date;
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

import proximity.persistence.model.Comment;
import proximity.persistence.model.Place;
import proximity.persistence.model.User;
import proximity.persistenceServices.ImagePersistenceService;
import proximity.persistenceServices.PlacePersistenceService;
import proximity.utils.FormNames;
import proximity.xml.model.HeatQuery;
import proximity.xml.model.HeatResponse;
import proximity.xml.model.PlaceDesc;

@Path(FormNames.placeP)
public class PlaceWebService extends WebService{
	@Inject
	private PlacePersistenceService placeService;

	@Inject
	private ImagePersistenceService imageService;

	@POST
	@Path(FormNames.newP)
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response postNewPlace(@FormDataParam(FormNames.name) String name,
			@FormDataParam(FormNames.text) String text, // TODO not for place
			@FormDataParam(FormNames.heatDuration) long heatDuration,
			@FormDataParam(FormNames.image) InputStream image,
			@FormDataParam(FormNames.latitude) double latitude,
			@FormDataParam(FormNames.longitude) double longitude,
			@FormDataParam(FormNames.rating) byte rating,
			@FormDataParam(FormNames.categoryId) int categoryId,
			@FormDataParam(FormNames.sessionId) String sessionId) {

		User user = getAuthenticatedUser(sessionId);
		if(user == null){
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
		Place place = new Place();
		place.setCantRating(1);
		place.setTotalRating(rating);
		place.setCreationTime(new Date().getTime());
		place.setHeatOverTime(new Date().getTime()+heatDuration);
		place.setLatitude(latitude);
		place.setLongitude(longitude);
		place.setName(name);

		Comment comment = new Comment();
		comment.setCreationTime(new Date().getTime());
		comment.setImage(imageService.saveImage(image));
		comment.setPlace(place);
		comment.setRating(rating);
		comment.setText(text);
		comment.setUser(user);
		place.getComments().add(comment);
		
		try {
			placeService.postNewPlace(place, comment, categoryId);
		} catch (RuntimeException re) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		return buildResponse(place.getPlaceId()+"");
	}

	@POST
	@Path(FormNames.commentP)
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response commentPlace(@FormDataParam(FormNames.text) String text,
			@FormDataParam(FormNames.placeId) int placeId,
			@FormDataParam(FormNames.heatDuration) long heatDuration,
			@FormDataParam(FormNames.rating) byte rating,
			@FormDataParam(FormNames.image) InputStream image,
			@FormDataParam(FormNames.sessionId) String sessionId,
			@FormDataParam(FormNames.categoryId) int categoryId) {

		User user = getAuthenticatedUser(sessionId);
		if(user == null){
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
		Comment comment = new Comment();
		comment.setCreationTime(new Date().getTime());
		comment.setImage(imageService.saveImage(image));
		comment.setRating(rating);
		comment.setText(text);
		
		try {
			placeService.commentPlace(comment, placeId, user, categoryId, new Date().getTime() + heatDuration);
		} catch (RuntimeException re) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		return buildResponse(comment.getCommentId()+"");
	}
	

	@POST
	@Path(FormNames.whotP)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response whatIsHot(HeatQuery[] hqs) {
		HeatResponse[] ret = new HeatResponse[hqs.length];
		for(int i = 0; i < ret.length; i++){
			ret[i] = new HeatResponse();
			ret[i].id = hqs[i].id;
			List<Place> places = placeService.whatIsHot(hqs[i]);
			ret[i].places = new PlaceDesc[places.size()];
			int j = 0;
			for(Place p : places){
				ret[i].places[j++] = new PlaceDesc(p, hqs[i].catId);
			}
		}
		
		return buildResponse(ret);
	}

	@POST
	@Path(FormNames.whotP)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response whatIsHot(@FormDataParam(FormNames.after) long after,
			@FormDataParam(FormNames.categoriesBD) List<FormDataBodyPart> catIds,
			@FormDataParam(FormNames.maximumLatitude) double maxLat,
			@FormDataParam(FormNames.maximumLongitude) double maxLon,
			@FormDataParam(FormNames.minimumLatitude) double minLat,
			@FormDataParam(FormNames.minimumLongitude) double minLon) {
		int[] catId = new int[catIds.size()];
		for(int i = 0; i < catId.length; i++){
			catId[i] = Integer.parseInt(catIds.get(i).getValue());
		}
		
		HeatQuery hq = new HeatQuery();
		hq.id = 1;
		hq.after = after;
		hq.catId = catId;
		hq.maxLat = maxLat;
		hq.maxLon = maxLon;
		hq.minLat = minLat;
		hq.minLon = minLon;
		
		List<Place> places = placeService.whatIsHot(hq);
		
		HeatResponse ret = new HeatResponse();
		ret.id = 1;
		ret.places = new PlaceDesc[places.size()];
		int j = 0;
		for(Place p : places){
			ret.places[j++] = new PlaceDesc(p, hq.catId);
		}
		
		return buildResponse(ret);
	}
	
}
