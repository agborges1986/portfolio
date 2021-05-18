package com.kaymansoft.proximity.client.network;

import java.io.InputStream;

import com.kaymansoft.proximity.model.UserData;

public interface UserProvider {
	/**
	 * Logs in the server
	 * 
	 * @param userName
	 *            The user's name
	 * @param password
	 *            The password
	 * @return a Session id identifying the current session
	 */
	public UserData login(String userName, String password)
			throws RuntimeException;

	public boolean logout(String session) throws RuntimeException;

	public boolean edit(String displayName, int maximumDistance,
			String password, String userName, InputStream image,
			int[] categoriesBD, String sessionId) throws RuntimeException;

	public UserData register(String displayName, int maximumDistance,
			String password, String userName, int[] categoriesBD,
			InputStream image) throws RuntimeException;
}
