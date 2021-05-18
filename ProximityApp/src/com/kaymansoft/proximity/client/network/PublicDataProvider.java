package com.kaymansoft.proximity.client.network;

import com.kaymansoft.proximity.model.CategoryDesc;

public interface PublicDataProvider {
	public CategoryDesc[] getCategoryList() throws RuntimeException;
}
