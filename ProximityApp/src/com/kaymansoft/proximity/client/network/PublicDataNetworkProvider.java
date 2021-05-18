package com.kaymansoft.proximity.client.network;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.kaymansoft.proximity.model.CategoryDesc;

public class PublicDataNetworkProvider extends NetworkProvider implements
		PublicDataProvider {

	public PublicDataNetworkProvider(String url) {
		super(url);
	}

	public CategoryDesc[] getCategoryList() throws RuntimeException {
		try {
			HttpResponse httpResponse = MakeGETRequest(String.format("%s%s%s",uri, FormNames.publicdataP, FormNames.categoriesP));
			
			ensureProperResponse(httpResponse);
			
			String jsonString = EntityUtils.toString(httpResponse.getEntity()).trim();
			JSONObject json = new JSONObject(jsonString);
			JSONObject jsonO = json.optJSONObject("categoryDesc");
			if (jsonO != null) {
				return new CategoryDesc[] { CategoryDesc.fromJSON(jsonO) };
			} else {
				return CategoryDesc.fromJSON(json.getJSONArray("categoryDesc"));
			}
		} catch (Exception e) {
			throw new RuntimeException("Errors ocurred", e);
		}
	}

}
