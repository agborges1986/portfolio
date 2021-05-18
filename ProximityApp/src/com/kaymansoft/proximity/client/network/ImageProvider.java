package com.kaymansoft.proximity.client.network;

import android.graphics.drawable.Drawable;

public interface ImageProvider {
	public Drawable getImage(String imageName) throws RuntimeException;
}
