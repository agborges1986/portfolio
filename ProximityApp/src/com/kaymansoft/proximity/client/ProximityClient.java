package com.kaymansoft.proximity.client;

import java.io.InputStream;
import java.io.Serializable;

import android.graphics.drawable.Drawable;

import com.kaymansoft.proximity.model.CategoryDesc;
import com.kaymansoft.proximity.model.HeatResponse;
import com.kaymansoft.proximity.model.UserData;

/**
 * @author
 *
 * This interface defines the communication operations available to the app. It provides a unifying element
 * to the network and cache operations. All operations are asynchronous: the network code runs in independent
 * threads and results are reported back in the main thread.
 * 
 */
public interface ProximityClient{

	/**
	 * This method  posts a new place to the server
	 * 
	 * @param name The name of the place
	 * @param description The description of the place (Actually it is the text of a generated comment for
	 * the place)
	 * @param heatDuration The time, in milliseconds, that the place is expected to remain hot for.
	 * @param image A picture of the place (Optional, can be null)
	 * @param latitude The latitude
	 * @param longitude The longitude
	 * @param rating A number between 1 and 4 indicating how hot is the place
	 * @param categoryId The id of the category for which the place is being posted (This id should be
	 * obtained by calling <code>getCategoryList</code>)
	 * @param sessionId The id of the current user's session (should be obtained from a call to <code>login
	 * </code> or <code>register</code>)
	 * @param reportable The object implementing the <code>Reportable</code> interface to which report the 
	 * results when ready
	 */
	public void postPlace(String name, String description, int heatDuration,
			InputStream image, double latitude, double longitude, int rating,
			int categoryId, String sessionId, Reportable<Integer> reportable);

	/**
	 * This method comments about an existing place in the server
	 * 
	 * @param text The comment
	 * @param placeId The id of the place
	 * @param rating The rating
	 * @param image An image of the place (can be null)
	 * @param sessionId The current user's session id
	 * @param categoryId The id of the category the existing place is being commented for
	 * @param heatDuration The time in milliseconds the place is expected to remain hot for. It may extend
	 * the life of the place in the database servers
	 * @param reportable The object implementing the <code>Reportable</code> interface to which report the 
	 * results when ready
	 */
	public void commentPlace(String text, int placeId, int rating,
			InputStream image, String sessionId, int categoryId,
			int heatDuration, Reportable<Object> reportable);

	/**
	 * This method queries the server for places of some categories in a rectangular area
	 * 
	 * @param catId The ids of the categories being queried
	 * @param maxLat The maximum allowed latitude 
	 * @param maxLon The maximum allowed longitude
	 * @param minLat The minimum allowed latitude
	 * @param minLon The minimum allowed longitude
	 * @param reportable The object implementing the <code>Reportable</code> interface to which report the 
	 * results when ready
	 */
	public void whatIsHot(int[] catId, double maxLat, double maxLon,
			double minLat, double minLon, Reportable<HeatResponse> reportable);

	/**
	 * This method fetches an image from the server or the cache
	 * 
	 * @param imageName The identifier of the image being requested
	 * @param reportable The object implementing the <code>Reportable</code> interface to which report the 
	 * results when ready
	 */
	public void getImage(String imageName, Reportable<Drawable> reportable);

	/**
	 * This method obtains the list of categories being used by the server at the time
	 * 
	 * @param reportable The object implementing the <code>Reportable</code> interface to which report the 
	 * results when ready
	 */
	public void getCategoryList(Reportable<CategoryDesc[]> reportable);

	/**
	 * Logs in to the web service
	 * 
	 * @param userName The user name
	 * @param password The user password
	 * @param reportable The object implementing the <code>Reportable</code> interface to which report the 
	 * results when ready
	 */
	public void login(String userName, String password,
			Reportable<UserData> reportable);

	/**
	 * Logs out of the web service
	 * 
	 * @param sessionId The current user's session id
	 * @param reportable The object implementing the <code>Reportable</code> interface to which report the 
	 * results when ready
	 */
	public void logout(String sessionId, Reportable<Object> reportable);

	/**
	 * Edits the current user's data
	 * 
	 * @param displayName
	 * @param maximumDistance
	 * @param password
	 * @param userName
	 * @param image
	 * @param categoriesBD
	 * @param sessionId
	 * @param reportable The object implementing the <code>Reportable</code> interface to which report the 
	 * results when ready
	 */
	public void edit(String displayName, int maximumDistance, String password,
			String userName, InputStream image, int[] categoriesBD,
			String sessionId, Reportable<Object> reportable);

	/**
	 * Registers a new user in the servers
	 * 
	 * @param displayName The display name off the user
	 * @param maximumDistance Half the size in km of the square in which heat queries are made 
	 * @param password
	 * @param userName
	 * @param categoriesBD The categories that the user asks of by default (its ids)
	 * @param image The user's avatar (can be null)
	 * @param reportable The object implementing the <code>Reportable</code> interface to which report the 
	 * results when ready
	 */
	public void register(String displayName, int maximumDistance,
			String password, String userName, int[] categoriesBD,
			InputStream image, Reportable<UserData> reportable);

}