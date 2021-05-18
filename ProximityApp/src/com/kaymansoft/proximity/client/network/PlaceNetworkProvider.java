package com.kaymansoft.proximity.client.network;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.kaymansoft.proximity.model.HeatResponse;

public class PlaceNetworkProvider extends NetworkProvider implements
		PlaceProvider {

	public PlaceNetworkProvider(String url) {
		super(url);
	}

	public int postPlace(String name, String description, int heatDuration,
			InputStream image, double latitude, double longitude, int rating,
			int categoryId, String sessionId) throws RuntimeException {
		try {
			PostFormBuilder pfb = createPostForm()
					.addParam(FormNames.name, name)
					.addParam(FormNames.text, description)
					.addParam(FormNames.heatDuration, heatDuration)
					.addParam(FormNames.latitude, latitude)
					.addParam(FormNames.longitude, longitude)
					.addParam(FormNames.rating, rating)
					.addParam(FormNames.sessionId, sessionId)
					.addParam(FormNames.categoryId, categoryId);
			if (image != null) {
				pfb.addParam(FormNames.image, image);
			}

			HttpResponse response = pfb.getResponse(String.format("%s%s%s",uri, FormNames.placeP, FormNames.newP));

			int code = response.getStatusLine().getStatusCode();
			if(code == SC_UNAUTHORIZED){
				throw new Exception("User must be logged in to post new places");
			}
			ensureProperResponse(response);
			
			int ret = Integer.parseInt(inputStreamToString(response.getEntity().getContent()));
			response.getEntity().consumeContent();
			
			return ret;
		} catch (Exception e) {
			throw new RuntimeException("Errors ocurred", e);
		}
	}

	public boolean commentPlace(String text, int placeId, int rating,
			InputStream image, String sessionId, int categoryId, int heatDuration)
			throws RuntimeException {
		try {
			PostFormBuilder pfb = createPostForm()
					.addParam(FormNames.text, text)
					.addParam(FormNames.placeId, placeId)
					.addParam(FormNames.rating, rating)
					.addParam(FormNames.sessionId, sessionId)
					.addParam(FormNames.categoryId, categoryId)
					.addParam(FormNames.heatDuration, heatDuration);
			if (image != null) {
				pfb.addParam(FormNames.image, image);
			}
			HttpResponse response = pfb.getResponse(String.format("%s%s%s",uri, FormNames.placeP, FormNames.commentP));

			int code = response.getStatusLine().getStatusCode();
			if(code == SC_UNAUTHORIZED){
				throw new Exception("User must be logged in to comment on existing places");
			}
			ensureProperResponse(response);
			
			response.getEntity().consumeContent();
			
			return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
		} catch (Exception e) {
			throw new RuntimeException("Errors ocurred", e);
		}
	}

	public HeatResponse whatIsHot(long after, int[] catId, double maxLat, double maxLon, double minLat, double minLon) throws RuntimeException {
		try {
			HttpResponse response = createPostForm()
					.addParam(FormNames.after, after)
					.addParam(FormNames.categoriesBD, catId)
					.addParam(FormNames.maximumLatitude, maxLat)
					.addParam(FormNames.maximumLongitude, maxLon)
					.addParam(FormNames.minimumLatitude, minLat)
					.addParam(FormNames.minimumLongitude, minLon)
					.getResponse(String.format("%s%s%s",uri, FormNames.placeP, FormNames.whotP));
			String jsonString = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(jsonString);
			return HeatResponse.fromJSON(json);
		} catch (Exception e) {
			throw new RuntimeException("Errors ocurred", e);
		}
	}
	
	public static String inputStreamToString(InputStream is) throws IOException {

		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		// Read response until the end
		while ((line = rd.readLine()) != null) {
			total.append(line);
		}
		// Return full string
		return total.toString();
	}
}
