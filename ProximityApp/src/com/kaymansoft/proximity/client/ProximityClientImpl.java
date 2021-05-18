package com.kaymansoft.proximity.client;

import java.io.InputStream;

import android.graphics.drawable.Drawable;

import com.kaymansoft.proximity.client.cache.CacheProvider;
import com.kaymansoft.proximity.client.cache.CacheProviderStub;
import com.kaymansoft.proximity.client.data.CacheHeatResponse;
import com.kaymansoft.proximity.client.data.CommentPlaceData;
import com.kaymansoft.proximity.client.data.HeatQueryData;
import com.kaymansoft.proximity.client.data.PostPlaceData;
import com.kaymansoft.proximity.client.data.UserEditData;
import com.kaymansoft.proximity.client.data.UserRegisterData;
import com.kaymansoft.proximity.client.network.ImageNetworkProvider;
import com.kaymansoft.proximity.client.network.ImageProvider;
import com.kaymansoft.proximity.client.network.PlaceNetworkProvider;
import com.kaymansoft.proximity.client.network.PlaceProvider;
import com.kaymansoft.proximity.client.network.PublicDataNetworkProvider;
import com.kaymansoft.proximity.client.network.PublicDataProvider;
import com.kaymansoft.proximity.client.network.UserNetworkProvider;
import com.kaymansoft.proximity.client.network.UserProvider;
import com.kaymansoft.proximity.model.CategoryDesc;
import com.kaymansoft.proximity.model.HeatResponse;
import com.kaymansoft.proximity.model.UserData;

public class ProximityClientImpl implements ProximityClient{
	
	private CacheProvider cacheProvider;
	
	private ImageProvider imageProvider;
	
	private PublicDataProvider publicDataProvider;
	
	private UserProvider userProvider;
	
	private PlaceProvider placeProvider;
	
	public ProximityClientImpl(String url){
		cacheProvider = new CacheProviderStub();
		imageProvider = new ImageNetworkProvider(url);
		publicDataProvider = new PublicDataNetworkProvider(url);
		userProvider = new UserNetworkProvider(url);
		placeProvider = new PlaceNetworkProvider(url);
	}

	/* (non-Javadoc)
	 * @see com.kaymansoft.proximity.client.ProximityClient#postPlace(java.lang.String, java.lang.String, int, java.io.InputStream, double, double, int, int, java.lang.String, com.kaymansoft.proximity.client.Reportable)
	 */
	public void postPlace(
			String name,
			String description,
			int heatDuration,
			InputStream image,
			double latitude,
			double longitude,
			int rating,
			int categoryId,
			String sessionId,
			Reportable<Integer> reportable) {
		
		new NetworkAsyncTask<PostPlaceData, Integer>() {

			@Override
			public Integer doBack(PostPlaceData... params) throws RuntimeException {
				
				return placeProvider.postPlace(
						params[0].name,
						params[0].description,
						params[0].heatDuration,
						params[0].image,
						params[0].latitude,
						params[0].longitude,
						params[0].rating,
						params[0].categoryId,
						params[0].sessionId);
			}
		}.setReportable(reportable).execute(new PostPlaceData(
																name,
																description,
																heatDuration,
																image,
																latitude,
																longitude,
																rating,
																categoryId,
																sessionId));
	}

	/* (non-Javadoc)
	 * @see com.kaymansoft.proximity.client.ProximityClient#commentPlace(java.lang.String, int, int, java.io.InputStream, java.lang.String, int, int, com.kaymansoft.proximity.client.Reportable)
	 */
	public void commentPlace(
			String text,
			int placeId,
			int rating,
			InputStream image,
			String sessionId,
			int categoryId,
			int heatDuration,
			Reportable<Object> reportable) {
		
		new NetworkAsyncTask<CommentPlaceData, Object>() {

			@Override
			public Object doBack(CommentPlaceData... params) throws RuntimeException {
				
				return placeProvider.commentPlace(
						params[0].text,
						params[0].placeId,
						params[0].rating,
						params[0].image,
						params[0].sessionId,
						params[0].categoryId,
						params[0].heatDuration);
			}
		}.setReportable(reportable)
		.execute(new CommentPlaceData(
										text,
										placeId,
										rating,
										image,
										sessionId,
										categoryId,
										heatDuration));
	}

	/* (non-Javadoc)
	 * @see com.kaymansoft.proximity.client.ProximityClient#whatIsHot(int[], double, double, double, double, com.kaymansoft.proximity.client.Reportable)
	 */
	public void whatIsHot(
			int[] catId,
			double maxLat,
			double maxLon,
			double minLat,
			double minLon,
			Reportable<HeatResponse> reportable) {
		
		CacheHeatResponse chr = cacheProvider.getWhot(catId, maxLat, maxLon, minLat, minLon);
		
		reportable.setRemainingReportsNumber(chr.queries.size() + chr.responses.size());
		
		for(HeatQueryData hq: chr.queries){
			new NetworkAsyncTask<HeatQueryData, HeatResponse>() {

				@Override
				public HeatResponse doBack(HeatQueryData... params) throws RuntimeException {
					
					return placeProvider.whatIsHot(
							params[0].after,
							params[0].catId,
							params[0].maxLat,
							params[0].maxLon,
							params[0].minLat,
							params[0].minLon);
				}
			}.setReportable(reportable)
			.execute(hq);
		}
		
		for(HeatResponse hr: chr.responses){
			reportable.report(hr, "OK");
		}
		
	}

	/* (non-Javadoc)
	 * @see com.kaymansoft.proximity.client.ProximityClient#getImage(java.lang.String, com.kaymansoft.proximity.client.Reportable)
	 */
	public void getImage(String imageName, Reportable<Drawable> reportable) {
		
		Drawable drw = cacheProvider.getImage(imageName);
		
		if(drw != null){
			reportable.report(drw, "OK");
		}else{
			new NetworkAsyncTask<String, Drawable>() {
				@Override
				public Drawable doBack(String... params) throws RuntimeException {
					return imageProvider.getImage(params[0]);
				}
			}.setReportable(reportable).execute(imageName);
		}
	}

	/* (non-Javadoc)
	 * @see com.kaymansoft.proximity.client.ProximityClient#getCategoryList(com.kaymansoft.proximity.client.Reportable)
	 */
	public void getCategoryList(Reportable<CategoryDesc[]> reportable) {
		
		new NetworkAsyncTask<Void, CategoryDesc[]>() {
			@Override
			public CategoryDesc[] doBack(Void... params) throws RuntimeException {
				CategoryDesc[] ret = publicDataProvider.getCategoryList();
				return ret;
			}
		}.setReportable(reportable).execute();
	}

	/* (non-Javadoc)
	 * @see com.kaymansoft.proximity.client.ProximityClient#login(java.lang.String, java.lang.String, com.kaymansoft.proximity.client.Reportable)
	 */
	public void login(String userName, String password, Reportable<UserData> reportable) {
		
		new NetworkAsyncTask<String, UserData>() {

			@Override
			public UserData doBack(String... params) throws RuntimeException {
				
				return userProvider.login(params[0], params[1]);
			}
		}.setReportable(reportable).execute(userName, password);
	}

	/* (non-Javadoc)
	 * @see com.kaymansoft.proximity.client.ProximityClient#logout(java.lang.String, com.kaymansoft.proximity.client.Reportable)
	 */
	public void logout(String sessionId, Reportable<Object> reportable) {
		
		new NetworkAsyncTask<String, Object>() {

			@Override
			public Object doBack(String... params) throws RuntimeException {
				
				return userProvider.logout(params[0]);
			}
		}.setReportable(reportable).execute(sessionId);
	}

	/* (non-Javadoc)
	 * @see com.kaymansoft.proximity.client.ProximityClient#edit(java.lang.String, int, java.lang.String, java.lang.String, java.io.InputStream, int[], java.lang.String, com.kaymansoft.proximity.client.Reportable)
	 */
	public void edit(
			String displayName,
			int maximumDistance,
			String password,
			String userName,
			InputStream image,
			int[] categoriesBD,
			String sessionId,
			Reportable<Object> reportable) {
		
		new NetworkAsyncTask<UserEditData, Object>() {

			@Override
			public Object doBack(UserEditData... params) throws RuntimeException {
		
				return userProvider.edit(
						params[0].displayName,
						params[0].maximumDistance,
						params[0].password,
						params[0].userName,
						params[0].image,
						params[0].categoriesBD,
						params[0].sessionId);
			}
		}.setReportable(reportable).execute(new UserEditData(
															sessionId,
															displayName,
															maximumDistance,
															password,
															userName,
															categoriesBD,
															image));
	}

	/* (non-Javadoc)
	 * @see com.kaymansoft.proximity.client.ProximityClient#register(java.lang.String, int, java.lang.String, java.lang.String, int[], java.io.InputStream, com.kaymansoft.proximity.client.Reportable)
	 */
	public void register(
			String displayName,
			int maximumDistance,
			String password,
			String userName,
			int[] categoriesBD,
			InputStream image,
			Reportable<UserData> reportable) {
		
		new NetworkAsyncTask<UserRegisterData, UserData>() {

			@Override
			public UserData doBack(UserRegisterData... params) throws RuntimeException {
				
				return userProvider.register(
						params[0].displayName,
						params[0].maximumDistance,
						params[0].password,
						params[0].userName,
						params[0].categoriesBD,
						params[0].image);
			}
		}.setReportable(reportable).execute(new UserRegisterData(
																displayName,
																maximumDistance,
																password,
																userName,
																categoriesBD,
																image));
	}
}