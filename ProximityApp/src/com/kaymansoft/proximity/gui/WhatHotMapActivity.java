package com.kaymansoft.proximity.gui;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.kaymansoft.proximity.R;
import com.kaymansoft.proximity.adapters.PlaceAdapter;
import com.kaymansoft.proximity.client.Reportable;
import com.kaymansoft.proximity.model.HeatResponse;

public class WhatHotMapActivity extends MapActivity {
	MapView mapView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_what_hot_map);

		mapView = (MapView) findViewById(R.id.map_view);
		mapView.setBuiltInZoomControls(true);
		
		seeWhatHot();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private void seeWhatHot() {
		// TODO Parametros de prueba
		CS.client.whatIsHot(new int[] { 1, 2, 3, 4 }, 100, 100, 0, 0,
				new Reportable<HeatResponse>() {

					public void setRemainingReportsNumber(int cant) {
						// do nothing    
					}

					public void report(HeatResponse report, String errorMessage) {
						if (report == null) {
							Toast.makeText(WhatHotMapActivity.this,
									errorMessage, Toast.LENGTH_LONG).show();
						} else {
							CS.places = report.places;
							CS.sortEachPlace(CS.places);
							
							Drawable marker = getResources().getDrawable(R.drawable.map_marker);
							CustomItemizedOverlay mapPlaces = new CustomItemizedOverlay(marker, WhatHotMapActivity.this);
							mapView.getOverlays().add(mapPlaces);
							GeoPoint center = mapPlaces.getCenterPoint();
							int latSpan = mapPlaces.getLatSpanE6();
							int lonSpan = mapPlaces.getLonSpanE6();
							Log.v("Overlays", "Lat span is " + latSpan);
							Log.v("Overlays", "Lon span is " + lonSpan);  
							MapController mc = mapView.getController();
							mc.setCenter(center);
							mc.zoomToSpan((int) (latSpan * 1.5), (int) (lonSpan * 1.5));
						}
					}
			});
	}
	
	class CustomItemizedOverlay extends ItemizedOverlay<OverlayItem> {
		private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();
		private Context context;
		private GeoPoint center = null;

	    public CustomItemizedOverlay(Drawable defaultMarker) {
	        super(boundCenterBottom(defaultMarker));
	        
			// create locations of interest	        
	        for (int i = 0; i < CS.places.length; i++) {
	        	GeoPoint geoPoint = new GeoPoint((int) (CS.places[i].latitude * 1000000), 
	        									 (int) (CS.places[i].longitude * 1000000));
	        	mapOverlays.add(new OverlayItem(geoPoint, CS.places[i].name, 
	        			"Latitude: " + CS.places[i].latitude + "\n" +
	        			"Longitude: " + CS.places[i].longitude + "\n" +
	        			"Comments: " + CS.places[i].comments.length + "\n"
	        			));
				
			}
			populate();
	    }
	    
	    public CustomItemizedOverlay(Drawable defaultMarker, Context context) {
	        this(defaultMarker);
	        this.context = context;
	    }
	    
	   /**
	    * We added this method to find the middle point of the cluster
		* Start each edge on its opposite side and move across with
		* each point. The top of the world is +90, the bottom -90,
		* the west edge is -180, the east +180
		* 
		* @return the <code>GeoPoint</code> that represent the center point
	    */
		public GeoPoint getCenterPoint() {
			if (center == null) {
				int northEdge = -90000000; // i.e., -90E6 microdegrees
				int southEdge = 90000000;
				int eastEdge = -180000000;
				int westEdge = 180000000;
				Iterator<OverlayItem> iter = mapOverlays.iterator();
				while (iter.hasNext()) {
					GeoPoint pt = iter.next().getPoint();
					if (pt.getLatitudeE6() > northEdge)
						northEdge = pt.getLatitudeE6();
					if (pt.getLatitudeE6() < southEdge)
						southEdge = pt.getLatitudeE6();
					if (pt.getLongitudeE6() > eastEdge)
						eastEdge = pt.getLongitudeE6();
					if (pt.getLongitudeE6() < westEdge)
						westEdge = pt.getLongitudeE6();
				}
				center = new GeoPoint((int) ((northEdge + southEdge) / 2),
						(int) ((westEdge + eastEdge) / 2));
			}
			return center;
		}

//		@Override
//		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
//			// Hide the shadow by setting shadow to false
//			shadow = false;
//			super.draw(canvas, mapView, shadow);
//		}

		@Override
		protected OverlayItem createItem(int i) {
			return mapOverlays.get(i);
		}

		@Override
		public int size() {
			return mapOverlays.size();
		}
		
	    @Override
	    protected boolean onTap(int index) {
	        OverlayItem item = mapOverlays.get(index);
	        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
	        dialog.setTitle(item.getTitle());
	        dialog.setMessage(item.getSnippet());
	        dialog.show();
	        return true;
	    }

	    public void addOverlay(OverlayItem overlay) {
	        mapOverlays.add(overlay);
	        this.populate();
	    }
	    
	    class DialogOnClickListener implements OnClickListener{
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub				
			}    	
	    }
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		boolean supRetVal = super.onCreateOptionsMenu(menu);
//		menu.add(Menu.NONE, 0, Menu.NONE, "Item1");
//		menu.add(Menu.NONE, 1, Menu.NONE, "Item2");
//		menu.add(Menu.NONE, 2, Menu.NONE, "Item3");
//
//		return supRetVal;
//	}
}
