package com.kaymansoft.proximity.gui;

import java.util.Arrays;
import java.util.Comparator;

import com.kaymansoft.proximity.R;
import com.kaymansoft.proximity.client.ProximityClient;
import com.kaymansoft.proximity.client.ProximityClientImpl;
import com.kaymansoft.proximity.model.CategoryDesc;
import com.kaymansoft.proximity.model.CommentDesc;
import com.kaymansoft.proximity.model.PlaceDesc;

/**
 * (Client Side) Clase que contiene los campos estaticos que definen en la app(el cliente) la comunicacion con el servidor.
 * 
 * @author Denis
 * 
 */
public class CS {
	// Variables relacionadas con datos del servidor.
	public static ProximityClient client = new ProximityClientImpl("http://10.0.2.2:8080/proximity");
	public static String sessionId = "";
	public static int tile = -1;
	public static CategoryDesc[] categories = null;
	public static PlaceDesc[] places = null;

	// Constantes
	public static final String PREF_FILE = "preferences_file";
	
	// Variables de la app
	public static int[] tiles =   { R.drawable.user_tile_01,
						            R.drawable.user_tile_02,
						            R.drawable.user_tile_03,
						            R.drawable.user_tile_04,
						            R.drawable.user_tile_05,
						            R.drawable.user_tile_06,
						            R.drawable.user_tile_07,
						            R.drawable.user_tile_08,
						            R.drawable.user_tile_09,
						            R.drawable.user_tile_10,
						            R.drawable.user_tile_11,
						            R.drawable.user_tile_12,
						            R.drawable.user_tile_13,
						            R.drawable.user_tile_14,
						            R.drawable.user_tile_15,
						            R.drawable.user_tile_16,
						            R.drawable.user_tile_17,
						            R.drawable.user_tile_18,
						            R.drawable.user_tile_19,
						            R.drawable.user_tile_20	};

	// Funciones estáticas auxiliares
	public static void sortPlacesByTime(CommentDesc[] comments) {
		Arrays.sort(comments, new Comparator<CommentDesc>() {
			public int compare(CommentDesc lhs, CommentDesc rhs) {
				if (lhs.creationTime < rhs.creationTime)
					return 1;
				else
					return -1;
			}
		});
	}
	
	public static void sortEachPlace(PlaceDesc[] places) {
		for (int i = 0; i < places.length; i++)
			sortPlacesByTime(places[i].comments);
	}
}
