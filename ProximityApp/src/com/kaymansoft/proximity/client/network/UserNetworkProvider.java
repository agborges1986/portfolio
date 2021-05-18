package com.kaymansoft.proximity.client.network;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import static org.apache.http.HttpStatus.*;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.kaymansoft.proximity.model.UserData;

public class UserNetworkProvider extends NetworkProvider implements
		UserProvider {

	public UserNetworkProvider(String url) {
		super(url);
	}

	public UserData login(String userName, String password)
			throws RuntimeException {
		try {
			HttpResponse response = createPostForm()
					.addParam(FormNames.userName, userName)
					.addParam(FormNames.password, password)
					.getResponse(String.format("%s%s%s",uri, FormNames.userP, FormNames.loginP));
			
			int code = response.getStatusLine().getStatusCode();
			if(code == SC_UNAUTHORIZED){
				throw new Exception("Wrong username or password");
			}
			ensureProperResponse(response);
			
			String jsonString = EntityUtils.toString(response.getEntity());
			JSONObject jsonObject = new JSONObject(jsonString);
			UserData ret = UserData.fromJSON(jsonObject);
			return ret;
		} catch (Exception e) {
			throw new RuntimeException("Error", e);
		}
	}

	public boolean logout(String session) throws RuntimeException{
		try {
			HttpResponse response = createPostForm().addParam(FormNames.sessionId, session).getResponse(String.format("%s%s%s",uri, FormNames.userP, FormNames.logoutP));
			response.getEntity().consumeContent();
			return response.getStatusLine().getStatusCode() == SC_OK;
		} catch (Exception e) {
			throw new RuntimeException("Error", e);
		}
	}

	public boolean edit(String displayName, int maximumDistance,
			String password, String userName, InputStream image,
			int[] categoriesBD, String sessionId) throws RuntimeException {
		try {
			PostFormBuilder pfb = createPostForm()
					.addParam(FormNames.displayName, displayName)
					.addParam(FormNames.maximumDistance,
							Integer.toString(maximumDistance))
					.addParam(FormNames.password, password)
					.addParam(FormNames.userName, userName)
					.addParam(FormNames.categoriesBD, categoriesBD)
					.addParam(FormNames.sessionId, sessionId);
			if (image != null) {
				pfb = pfb.addParam(FormNames.image, image);
			}

			HttpResponse response = pfb.getResponse(String.format("%s%s%s",uri, FormNames.userP, FormNames.editP));
			
			int code = response.getStatusLine().getStatusCode();
			if(code == SC_UNAUTHORIZED){
				throw new Exception("No user loged in to the application");
			} else if(code == SC_CONFLICT){
				throw new Exception("There is another user with the same username");
			}
			ensureProperResponse(response);
			
			return response.getStatusLine().getStatusCode() == SC_OK;
		} catch (Exception e) {
			throw new RuntimeException("Error", e);
		}
	}

	public UserData register(String displayName, int maximumDistance,
			String password, String userName, int[] categoriesBD,
			InputStream image) throws RuntimeException {
		try {
			PostFormBuilder pfb = createPostForm()
					.addParam(FormNames.displayName, displayName)
					.addParam(FormNames.maximumDistance,
							Integer.toString(maximumDistance))
					.addParam(FormNames.password, password)
					.addParam(FormNames.userName, userName)
					.addParam(FormNames.categoriesBD, categoriesBD);
			if (image != null) {
				pfb = pfb.addParam(FormNames.image, image);
			}

			HttpResponse response = pfb.getResponse(String.format("%s%s%s",uri, FormNames.userP, FormNames.registerP));
			
			int code = response.getStatusLine().getStatusCode();
			if(code == SC_CONFLICT){
				throw new Exception("There is another user with the same username");
			}
			ensureProperResponse(response);
			
			String tmp = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(tmp);
			return UserData.fromJSON(json);
		} catch (Exception e) {
			throw new RuntimeException("Error", e);
		}
	}

}
