package com.kaymansoft.proximity.client.network;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import android.graphics.drawable.Drawable;


public class ImageNetworkProvider extends NetworkProvider implements
		ImageProvider {

	public ImageNetworkProvider(String url) {
		super(url);
	}

	public Drawable getImage(String imageName) throws RuntimeException {
		try{
			HttpResponse response = MakeGETRequest(String.format("%s%s/%s", uri, FormNames.imageP, imageName));
			
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND){
				throw new Exception("Image not found on sever");
			}
			ensureProperResponse(response);
			
			Drawable ret = Drawable.createFromStream(response.getEntity().getContent(), "img");
			response.getEntity().consumeContent();
			return ret;
		}catch(Exception e){
			throw new RuntimeException("Errors ocurred", e);
		}
	}
}
