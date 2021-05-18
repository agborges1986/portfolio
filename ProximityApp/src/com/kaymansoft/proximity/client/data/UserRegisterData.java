package com.kaymansoft.proximity.client.data;

import java.io.InputStream;

public class UserRegisterData {
	
	public UserRegisterData(
			String displayName,
			int maximumDistance,
			String password,
			String userName,
			int[] categoriesBD,
			InputStream image) {
		
		super();
		
		this.displayName = displayName;
		this.maximumDistance = maximumDistance;
		this.password = password;
		this.userName = userName;
		this.categoriesBD = categoriesBD;
		this.image = image;
	}

	public String displayName;

	public int maximumDistance;

	public String password;

	public String userName;

	public int[] categoriesBD;
	
	public InputStream image;
}
