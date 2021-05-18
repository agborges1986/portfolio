package com.kaymansoft.proximity;

import com.kaymansoft.proximity.client.ProximityClientImpl;
import com.kaymansoft.proximity.client.ProximityClient;
import com.kaymansoft.proximity.client.Reportable;
import com.kaymansoft.proximity.model.CategoryDesc;
import com.kaymansoft.proximity.model.HeatResponse;
import com.kaymansoft.proximity.model.UserData;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity{

	private ProximityClient client;
	private String sessionId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		client = new ProximityClientImpl("http://10.0.2.2:8080/proximity");
		sessionId = "";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void testRegister(View view){
		
		client.register("Jorge E. Moreira", 2, "123456", "jmoreira", new int[]{1, 2}, null, new Reportable<UserData>() {
			
			public void setRemainingReportsNumber(int cant) {
				// do nothing
			}
			
			public void report(UserData report, String errorMessage) {
				TextView textView = (TextView) findViewById(R.id.textView00);
				if(report == null){
					textView.setText(errorMessage);
				}else{
					sessionId = report.sessionId;
					textView.setText(report.sessionId);
				}
			}
		});
		
		TextView textView = (TextView) findViewById(R.id.textView00);
		textView.setText("processing...");
	}

	public void testLogin(View view){
		
		client.login("jmoreira", "123456", new Reportable<UserData>() {
			
			public void setRemainingReportsNumber(int cant) {
				// do nothing
			}
			
			public void report(UserData report, String errorMessage) {
				TextView textView = (TextView) findViewById(R.id.TextView01);
				if(report == null){
					textView.setText(errorMessage);
				}else{
					sessionId = report.sessionId;
					textView.setText(report.sessionId);
				}
			}
		});
		
		TextView textView = (TextView) findViewById(R.id.TextView01);
		textView.setText("processing...");
	}

	public void testEdit(View view){
		
		client.edit("Jorge Enrique Moreira", 4, "123456", "jmoreira", null, new int[]{1, 2, 3}, sessionId, new Reportable<Object>() {
			
			public void setRemainingReportsNumber(int cant) {
				// do nothing
			}
			
			public void report(Object report, String errorMessage) {
				TextView textView = (TextView) findViewById(R.id.TextView02);
				if(report == null){
					textView.setText(errorMessage);
				}else
					textView.setText(report.toString());
			}
		});
		
		TextView textView = (TextView) findViewById(R.id.TextView02);
		textView.setText("processing...");
	}

	public void testCategories(View view){
		
		client.getCategoryList(new Reportable<CategoryDesc[]>() {
			
			public void setRemainingReportsNumber(int cant) {
				// do nothing
			}
			
			public void report(CategoryDesc[] report, String errorMessage) {
				TextView textView = (TextView) findViewById(R.id.TextView03);
				if(report == null){
					textView.setText(errorMessage);
				}else
					textView.setText(report.length+"");
			}
		});
		
		TextView textView = (TextView) findViewById(R.id.TextView03);
		textView.setText("processing...");
	}

	public void testPost(View view){
		
		client.postPlace("UCLV", "university", 1000000000, null, 2, 4, 4, 1, sessionId, new Reportable<Integer>() {
			
			public void setRemainingReportsNumber(int cant) {
				// do nothing
			}
			
			public void report(Integer report, String errorMessage) {
				TextView textView = (TextView) findViewById(R.id.TextView04);
				if(report == null){
					textView.setText(errorMessage);
				}else
					textView.setText(report.toString());
			}
		});
		
		TextView textView = (TextView) findViewById(R.id.TextView04);
		textView.setText("processing...");
	}

	public void testComment(View view){
		
		client.commentPlace("great place", 1, 3, null, sessionId, 2, 1000, new Reportable<Object>() {
			
			public void setRemainingReportsNumber(int cant) {
				// do nothing
			}
			
			public void report(Object report, String errorMessage) {
				TextView textView = (TextView) findViewById(R.id.TextView05);
				if(report == null){
					textView.setText(errorMessage);
				}else
					textView.setText(report.toString());
			}
		});
		
		TextView textView = (TextView) findViewById(R.id.TextView05);
		textView.setText("processing...");
	}

	public void testWhot(View view){
		
		client.whatIsHot(new int[]{1, 2, 3, 4}, 100, 100, 0, 0, new Reportable<HeatResponse>() {
			
			public void setRemainingReportsNumber(int cant) {
				// do nothing
			}
			
			public void report(HeatResponse report, String errorMessage) {
				TextView textView = (TextView) findViewById(R.id.TextView06);
				if(report == null){
					textView.setText(errorMessage);
				}else
					textView.setText(report.places.length+"");
			}
		});
		
		TextView textView = (TextView) findViewById(R.id.TextView06);
		textView.setText("processing...");
	}

	public void testImage(View view){
		
		client.getImage("ddeniz", new Reportable<Drawable>() {
			
			public void setRemainingReportsNumber(int cant) {
				// do nothing
			}
			
			public void report(Drawable report, String errorMessage) {
				ImageView imageView = (ImageView) findViewById(R.id.imageView1);
				if(report == null){
					TextView textView = (TextView) findViewById(R.id.TextView06);
					textView.setText(errorMessage);
				}else{
					imageView.setImageDrawable(report);
				}
			}
		});
	}	
}
