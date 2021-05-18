package com.kaymannsoft.card;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Ariel
 *
 */
@SuppressLint("ShowToast")
public class FlashCard extends Activity implements OnClickListener, OnCheckedChangeListener, OnChronometerTickListener{
	
	TextView ask,head, comment, head_comment;
	String pickone, picktwo, pickthree;
	ImageView image, commentImage;
	RadioGroup chosse;
	public RadioButton pick1,pick2,pick3;
	Test test;
//	Animation  myanimation;
	RotateAnimation rotation;
	RelativeLayout card;
	LinearLayout linear_image,linear_image_comment;
	ScrollView cardscroll;
	Button salve, prev, next;
	int cont=1, answer;
	Context cxt;
	DisplayMetrics metrics;
	ScaleDrawable scale;
	
	Chronometer time;
	
	private static boolean BACK_STATUS= false;
	private static boolean ASK_SATUS= false;
	
	private long 			TEST_TIME_USED= 0;
	static long 		TEST_TIME= 120000;
	static long		TEST_TIME_ALARM=100000;
	final static long 		ANSWER_TIME_ALARM= 50000;
	final static long		ANSWER_TIME= 60000;
	
	final static int 		ACTION_NONE=0;
	final static int 		ACTION_TEST_FINISH=1;
	final static int 		ACTION_TEST_ALARM=2;
	final static int 		ACTION_ANSWER_FINISH=3;
	final static int 		ACTION_ANSWER_ALARM=4;
	private boolean			aCTION_REPEAT_FLOW=true;
	private double			PASS_INDEX=0.7;
	
	
	private long interval_adjust_chronometer=0;
	private int counter=0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flash_card_layout);
		       
        cacheView();
        initialize();
        refreshTest();
      }
	
	
	private void cacheView(){
		
		card=(RelativeLayout)findViewById(R.id.RelativeLayout1);
        ask=(TextView)findViewById(R.id.ask);
        head=(TextView)findViewById(R.id.tv_num_question);
        comment=(TextView)findViewById(R.id.textView1);
        head_comment=(TextView)findViewById(R.id.textView2);
        linear_image_comment=(LinearLayout)findViewById(R.id.LinearCommentImage);
        linear_image=(LinearLayout)findViewById(R.id.linearLayout1);
        cardscroll=(ScrollView)findViewById(R.id.scrollView1);
        
        image=(ImageView)findViewById(R.id.imageView1);
        commentImage=(ImageView)findViewById(R.id.imageView2);
        chosse=(RadioGroup)findViewById(R.id.radioGroup1);
        pick1=(RadioButton)findViewById(R.id.radio0);
        pick2=(RadioButton)findViewById(R.id.radio1);
        pick3=(RadioButton)findViewById(R.id.radio2);
        salve=(Button)findViewById(R.id.button2);
        prev=(Button)findViewById(R.id.button1);
        next=(Button)findViewById(R.id.button3);
        time=(Chronometer)findViewById(R.id.chronometer1);
        
        Typeface fonttext=UtilsTextFace.getTextFont(this);
        ask.setTypeface(fonttext);
        head.setTypeface(fonttext);
        head_comment.setTypeface(fonttext);
        comment.setTypeface(fonttext);
        pick1.setTypeface(fonttext);
        pick2.setTypeface(fonttext);
        pick3.setTypeface(fonttext);
        prev.setTypeface(fonttext);
        next.setTypeface(fonttext);
        
        salve.setTypeface(UtilsTextFace.getTextCheck(this));
        
        Typeface crono_txt=UtilsTextFace.getTextCheck(this);
        time.setTypeface(crono_txt);
        
        

	}
	private void initialize() {
		
		Exercise[] ejercicios = null;
		
		time.start();
		time.setFormat(null);
		
		cxt=this;
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        rotation = new RotateAnimation(-90, 0, metrics.widthPixels/2, metrics.heightPixels/2, 1.0f, true);
        rotation.setDuration(450);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setAnimationListener(new DisplayNextView(1));
        
        answer=1;
        FlashCard.setASK_SATUS(true);
        FlashCard.setBACK_STATUS(false);
        head_comment.setText(R.string.comment_head);
        
        SharedPreferences prefs=getSharedPreferences("appPreferences", MODE_PRIVATE);
        String idioma=prefs.getString("lenguaje_select","en");
		String ctdad_ask=prefs.getString("ask_quantity", "");
		
//		Parseo los ejercicios desde el xml
		
		try {
			if(idioma.compareTo("en")==0){
				ejercicios=ExecirsesReaderUtil.readContentExcercisesXML(this,R.xml.ask );
			}
			else
			{
				ejercicios=ExecirsesReaderUtil.readContentExcercisesXML(this,R.xml.ask_es );
			}
		} catch (XmlPullParserException c) {
			Log.e("MI TAG", "ERROR EN PARSEAR EL XML");
			c.printStackTrace();
		} catch (IOException e) {
			Log.e("MI TAG", "ERROR EN IO EXCEPTION");
			e.printStackTrace();
		}
//		fin del parseo		
		
		
//		creo el Examen		
		if(ctdad_ask==""){
			//TODO Insertar el valor por defecto de la cantidad de ejercicios.
			test= new Test(PASS_INDEX, ejercicios);
		}
		else{
			test=new Test(Integer.parseInt(ctdad_ask), PASS_INDEX, ejercicios);
		}
		
		TEST_TIME=test.ask_quantity*40000;
		TEST_TIME_ALARM=(long) (TEST_TIME*0.9);
		
		chosse.setOnCheckedChangeListener(this);
		salve.setOnClickListener(this);
		prev.setOnClickListener(this);
		next.setOnClickListener(this);
		time.setOnChronometerTickListener(this);
		
	}
		
	private void refreshTest() {
		
//		Para el encabezado y la pregunta
		
			Exercise temp=test.getNowExercise();
			counter++;
			
			if(FlashCard.isASK_SATUS()){
//			TODO para hacer si estamos en la pregunta
				
//				Inicializo pregunta y encabezado
				//head.setText(temp.head);
				head.setText(String.valueOf(test.getASK_COUNTER())+" "+this.getString(R.string.number_question)+" "+String.valueOf(test.ask_quantity)+"  ");
		        ask.setText(temp.exercise);
		        
//			    Inicializo imagen de pregunta
		        
		        if(temp.image==-1){
		        	linear_image.setVisibility(View.GONE);
		        }
		        else{
		        	linear_image.setVisibility(View.VISIBLE);
		        	image.setImageResource(temp.image); 
		        }
		       
		        
		        prev.setText(R.string.flash_button_check);
//		        Desaparezco los comentarios y lo que tiene que ver con ello
		        goneComment();
		        
//		        Para los radio Button llenarlo con ls posibles respuestas
		        if (temp.choose.length <3){
		        	pick1.setText(temp.choose[0]);
			        pick2.setText(temp.choose[1]);
			        pick3.setVisibility(View.GONE);
			        
			        }
		        else {
	        	    pick1.setText(temp.choose[0]);
	    	        pick2.setText(temp.choose[1]);
	    	        pick3.setVisibility(View.VISIBLE);
	    	        pick3.setText(temp.choose[2]);
	    	        }
			}
			if(FlashCard.isBACK_STATUS()){
				bringComment(temp);				
				
			}
			
        	
	}

	private void goneComment() {
		// Para que los comentarios no aparezcan
		
		comment.setVisibility(View.GONE);
		head_comment.setVisibility(View.GONE);
		linear_image_comment.setVisibility(View.GONE);
		
		
		
		
	}
	
	private void bringComment(Exercise temp ){
		
		comment.setVisibility(View.VISIBLE);
		head_comment.setVisibility(View.VISIBLE);
		
		if(temp.imagecomment!=-1){
			linear_image_comment.setVisibility(View.VISIBLE);
			commentImage.setImageResource(temp.imagecomment);
		}
				
		comment.setText(temp.comment);
		prev.setText(R.string.flash_button_next);
			
	}



	public void onClick(View v) {
		switch(v.getId()){
			case R.id.button2:
//				
				break;
			case R.id.button1:
				newAction();				
				break;
			case R.id.button3:
				showDialog(0);
				break;
		}		
		
	}

	

	@Override
	protected void onPause() {
		
		super.onPause();
		if(ASK_SATUS){
			time.stop();
			interval_adjust_chronometer=SystemClock.elapsedRealtime()-time.getBase();
		}
	}
	
	


	@Override
	protected void onResume() {
		
		super.onResume();
		if(ASK_SATUS){
		time.setBase(SystemClock.elapsedRealtime()-interval_adjust_chronometer);
		time.start();
		}
	}


	@SuppressLint("ShowToast")
	private void newAction() {
		
		if(!test.isFINISH_TEST_STATUS()){
			if(FlashCard.isASK_SATUS()){
	//			TODO Evaluar la respuesta
				if(test.evaluateAsk(answer)){
	//				buena respuesta
					FlashCard.setASK_SATUS(true);
					FlashCard.setBACK_STATUS(false);
					
					Toast tx1=Toast.makeText(this, getString(R.string.good_answer), 5);
					tx1.show();
					
					if(!test.passAsk()){//Ultima pregunta
						
						TEST_TIME_USED=TEST_TIME_USED+SystemClock.elapsedRealtime()-time.getBase();
						Intent i=new Intent(this, Result.class);
						Bundle data=new Bundle();
						data.putInt("good_answer", test.getGood_answers());
						data.putInt("total_answers", test.ask_quantity);
						data.putDouble("califacation", test.calification());
						data.putLong("time", TEST_TIME_USED);
						data.putBoolean("status", test.testIsPassed());
						i.putExtras(data);
						startActivity(i);
						finish();
						
					}
					else{
						refreshTest();				
						cardscroll.startAnimation(rotation);
						
						TEST_TIME_USED=TEST_TIME_USED+SystemClock.elapsedRealtime()-time.getBase();
						
						time.setBase(SystemClock.elapsedRealtime());
						time.start();
					}
			        
				}
				else{
	//				mala respuesta
	//				TODO Marcar la respuesta correcta y marcar la mala respuesta tambien
					FlashCard.setASK_SATUS(false);
					FlashCard.setBACK_STATUS(true);
					Toast tx=Toast.makeText(this, getString(R.string.bad_answer), 30);
					tx.show();
					
					time.stop();
					interval_adjust_chronometer=SystemClock.elapsedRealtime();
					TEST_TIME_USED=TEST_TIME_USED+interval_adjust_chronometer-time.getBase();
					cardscroll.startAnimation(rotation);
					
					changeRadioButton(true);
			        
				}
			}
			else{
				
				FlashCard.setASK_SATUS(true);
				FlashCard.setBACK_STATUS(false);
											        
		        test.passAsk();
		        
		        if(test.isFINISH_TEST_STATUS()){
		        	Intent i=new Intent(this, Result.class);
					Bundle data=new Bundle();
					data.putInt("good_answer", test.getGood_answers());
					data.putInt("total_answers", test.ask_quantity);
					data.putDouble("califacation", test.calification());
					data.putLong("time", TEST_TIME_USED);
					data.putBoolean("status", test.testIsPassed());
					i.putExtras(data);
					startActivity(i);
					finish();
		        }
		        else{
		        	changeRadioButton(false);
		        	cardscroll.startAnimation(rotation);
		        	refreshTest();
					chosse.setEnabled(false);
					time.setBase(SystemClock.elapsedRealtime());
			        time.start();
		        }
			}
		}
		else{
			Intent i=new Intent(this, Result.class);
			Bundle data=new Bundle();
			data.putInt("good_answer", test.getGood_answers());
			data.putInt("total_answers", test.ask_quantity);
			data.putDouble("califacation", test.calification());
			data.putLong("time", TEST_TIME_USED);
			data.putBoolean("status", test.testIsPassed());
			
			i.putExtras(data);
			startActivity(i);
			finish();
		}
			
		
		
	}


	public static boolean isASK_SATUS() {
		return ASK_SATUS;
	}



	public static void setASK_SATUS(boolean aSK_SATUS) {
		ASK_SATUS = aSK_SATUS;
	}

	public static boolean isBACK_STATUS() {
		return BACK_STATUS;
	}



	public static void setBACK_STATUS(boolean bACK_STATUS) {
		BACK_STATUS = bACK_STATUS;
	}

	private final class DisplayNextView implements Animation.AnimationListener {
	       

	        private DisplayNextView(int position) {
	           
	        }

	        public void onAnimationStart(Animation animation) {
	        }

	        public void onAnimationEnd(Animation animation) {
	        	refreshTest();
	        }

	        public void onAnimationRepeat(Animation animation) {
	            // Do nothing!!
	        }
	    }

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		
		switch(checkedId){
			case R.id.radio0:
				answer=1;
				break;
			case R.id.radio1:
				answer=2;
				break;
			case R.id.radio2:
				answer=3;
				break;
		}
	}


	public void onChronometerTick(Chronometer chronometer) {
		
		long time_interval=SystemClock.elapsedRealtime()-chronometer.getBase();
		if(ASK_SATUS){	
			timeAction(time_interval);
		}
	}
	    
	@SuppressLint("ShowToast")
	private void timeAction(long time_interval) {
//  	Toda la mecanica del tiempo aqui	
//		por defecto la accion es ninguna
		int action=ACTION_NONE;
		long tiempo_examen=TEST_TIME_USED+time_interval;
		
//		Para saber la accion que debe realizarse a cada momento
		if(tiempo_examen>=TEST_TIME){
			action=ACTION_TEST_FINISH;
		}else if(tiempo_examen>=TEST_TIME_ALARM){
			action=ACTION_TEST_ALARM;
		}else if(time_interval>ANSWER_TIME){
			action=ACTION_ANSWER_FINISH;
		}else if(time_interval>=ANSWER_TIME_ALARM){
			action=ACTION_ANSWER_ALARM;
		}
		
		switch(action){
		case ACTION_NONE:
			break;
		case ACTION_ANSWER_ALARM:
//			time.setTextColor(getResources().getColor(R.color.color_alarm_test));
			Animation anim=AnimationUtils.loadAnimation(this,R.anim.alarm_animation_test);
			time.startAnimation(anim);
			break;
		case ACTION_ANSWER_FINISH:
			time.stop();
			Toast tx2=Toast.makeText(this, getString(R.string.alarm_time_answer), 1);
			tx2.show();
			newAction();
			break;
		case ACTION_TEST_ALARM:
			Animation anim2=AnimationUtils.loadAnimation(this,R.anim.alarm_animation_test);
			if(aCTION_REPEAT_FLOW){
				Toast tx1=Toast.makeText(this, getString(R.string.alarm_time_down), 1);
				tx1.show();
			}
			aCTION_REPEAT_FLOW=false;
			time.startAnimation(anim2);
			break;
		case ACTION_TEST_FINISH:
			test.setASK_COUNTER(test.ask_quantity);
			Toast tx=Toast.makeText(this, getString(R.string.alarm_time_test),5);
			tx.show();
			newAction();
			break;
		
		}
		
				
	}


	public void changeRadioButton(boolean onBack){
		
		if(onBack){
			
			pick1.setButtonDrawable(R.drawable.radio_back_un);
			pick2.setButtonDrawable(R.drawable.radio_back_un);
			pick3.setButtonDrawable(R.drawable.radio_back_un);
			switch (answer) {
			case 1:
				pick1.setButtonDrawable(R.drawable.radio_back_false);
				pick1.setTextColor(getResources().getColor(R.color.false_answer_text_color));
				break;
			case 2:
				pick2.setButtonDrawable(R.drawable.radio_back_false);
				pick2.setTextColor(getResources().getColor(R.color.false_answer_text_color));
				break;
			case 3:
				pick3.setButtonDrawable(R.drawable.radio_back_false);
				pick3.setTextColor(getResources().getColor(R.color.false_answer_text_color));
				break;
			
			}
			switch(test.getAnswerNow()){
			case 1:
				pick1.setButtonDrawable(R.drawable.radio_back_true);
				pick2.setTextColor(getResources().getColor(R.color.false_answer_text_color));
				pick3.setTextColor(getResources().getColor(R.color.false_answer_text_color));
				break;
			case 2:
				pick2.setButtonDrawable(R.drawable.radio_back_true);
				pick1.setTextColor(getResources().getColor(R.color.false_answer_text_color));
				pick3.setTextColor(getResources().getColor(R.color.false_answer_text_color));
				break;
			case 3:
				pick3.setButtonDrawable(R.drawable.radio_back_true);
				pick1.setTextColor(getResources().getColor(R.color.false_answer_text_color));
				pick2.setTextColor(getResources().getColor(R.color.false_answer_text_color));
				break;
			}
		}
		else{
			pick1.setButtonDrawable(R.drawable.custom_radio_button);
			pick2.setButtonDrawable(R.drawable.custom_radio_button);
			pick3.setButtonDrawable(R.drawable.custom_radio_button);
			
			
			pick1.setTextColor(Color.BLACK);
			pick2.setTextColor(Color.BLACK);
			pick3.setTextColor(Color.BLACK);
		}
	}
	
	 protected Dialog onCreateDialog(int id){ 
		 
         switch (id) 
         {
         case 0:
         
            return new  AlertDialog.Builder(FlashCard.this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(R.string.finish_title_dialog)
            .setMessage(R.string.finish_text_dialog)
            .setPositiveButton(R.string.ok_dialog, 
                new  DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int  whichButton)
                    {
                    	test.finishTest();
        				newAction();
        				dialog.dismiss();
                    }
                }
            )
            .setNegativeButton(R.string.cancel_dialog , 
                new  DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int  whichButton)
                    {
                       dialog.cancel();
                    }
                }
            ).create();
            
            
        }
         return null;
	 }

	
}
