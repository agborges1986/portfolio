package com.kaymannsoft.card;

import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;



import com.jjoe64.graphview.GraphView;
import com.kaymannsoft.card.Legend;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Result extends Activity implements OnClickListener{
	
	int total_answers, good_answers;
	long time_test;
	
	GraphView result_view;
	
	TextView good_answer_text, total_answer_text, indice_text, time_text, title;
	TextView good_answer_value, total_answer_value,indice_value, time_value;
	
	LinearLayout graf_view, total_graf, legend_view;
	
	Button setting, about, again;
	
	boolean status;
	
	ImageView status_image;
	Legend legend;
	
	double indice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		
		
		
		Bundle extras=getIntent().getExtras();
		time_test=extras.getLong("time");
		total_answers=extras.getInt("total_answers");
		good_answers=extras.getInt("good_answer");
		status=extras.getBoolean("status");
		
		status_image=(ImageView)findViewById(R.id.imageView1);
		
		setAnimation();
		cacheViews();
		initialize();
		setGraph();
		
		
	}


	private void setAnimation() {
	
		if(status){
			status_image.setImageResource(R.drawable.approved);
		}
		else{
			status_image.setImageResource(R.drawable.rejected);
		}
		
		Animation animation=AnimationUtils.loadAnimation(this,R.anim.grow_to_middle);
		status_image.startAnimation(animation);
	}


	private void setGraph() {
		
		DisplayMetrics metrics= new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		GraphViewResult nuevo=new GraphViewResult(this, total_answers, good_answers);
		
		graf_view.addView(nuevo,metrics.widthPixels/2, metrics.heightPixels/3);
		legend_view.addView(legend,metrics.widthPixels/3, metrics.heightPixels/5);
		
		ImageView adapter=new ImageView(this);
		adapter.setImageResource(R.drawable.adapter_view);
		total_graf.addView(adapter);
	}


	private void initialize() {
				
		indice=(double)good_answers/total_answers;
		good_answer_value.setText(""+good_answers);
		total_answer_value.setText(""+total_answers);
		
		indice_value.setText(NumberFormat.getPercentInstance().format(indice));
		time_value.setText(new SimpleDateFormat("mm:ss").format(new Date(time_test)));
		
//		Para poner la letra de cada texto.
		Typeface fonttext=UtilsTextFace.getTextFont(this);
		
		indice_value.setTypeface(fonttext);
		time_value.setTypeface(fonttext);
		good_answer_value.setTypeface(fonttext);
		total_answer_value.setTypeface(fonttext);
		
		good_answer_text.setTypeface(fonttext);
		total_answer_text.setTypeface(fonttext);
		indice_text.setTypeface(fonttext);
		time_text.setTypeface(fonttext);
		title.setTypeface(fonttext);
		
		about.setTypeface(fonttext);
		setting.setTypeface(fonttext);
		
		again.setTypeface(UtilsTextFace.getTimeFont(this));
		
		about.setOnClickListener(this);
		setting.setOnClickListener(this);
		again.setOnClickListener(this);
		
		legend= new Legend(this, new String[]{
				this.getString(R.string.result_good_answers_graf_legend),
				this.getString(R.string.result_bad_answers_graf_legend),
				this.getString(R.string.result_total_answers_graf_legend),
		}, new int[]{
				this.getResources().getColor(R.color.good_series_color),
				this.getResources().getColor(R.color.bad_series_color),
				this.getResources().getColor(R.color.total_series_color)
		});
		
				
	}


	private void cacheViews() {
		
			
		good_answer_text=(TextView)findViewById(R.id.TextView01);
		total_answer_text=(TextView)findViewById(R.id.TextView04);
		indice_text=(TextView)findViewById(R.id.TextView02);
		time_text=(TextView)findViewById(R.id.textView2);
		title=(TextView)findViewById(R.id.textView1);
		
		good_answer_value=(TextView)findViewById(R.id.textView4);
		total_answer_value=(TextView)findViewById(R.id.TextView03);
		indice_value=(TextView)findViewById(R.id.textView3);
		time_value=(TextView)findViewById(R.id.textView5);
		
		graf_view=(LinearLayout)findViewById(R.id.linearLayout3);
		total_graf=(LinearLayout)findViewById(R.id.linearLayout1);
		legend_view=(LinearLayout)findViewById(R.id.linearLayout4);
		
		setting=(Button)findViewById(R.id.button1);
		again=(Button)findViewById(R.id.button3);
		about=(Button)findViewById(R.id.button2);
		
		
		
	}


	public void onClick(View v) {
		
		switch(v.getId()){
			case R.id.button1:
			{// Setting
				Intent ii=new Intent(this,Preferences.class);
				startActivity(ii);
				break;
			}
			case R.id.button2:
			{//About us
				Intent ii=new Intent(this,About.class);
				startActivity(ii);
				break;
			}
			case R.id.button3:
			{//Test Again
				Intent i=new Intent(this,FlashCard.class);
				startActivity(i);
				finish();
				break;
			}
		}
	}
	
	

}
