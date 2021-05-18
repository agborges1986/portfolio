package com.kaymansoft.gui;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;

import com.kaymansoft.R;
import com.kaymansoft.calories.BMIUtils;
import com.kaymansoft.calories.BMIUtils.BMIClassification;
import com.kaymansoft.calories.BMIUtils.WeigthRange;
import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.MCons;
import com.kaymansoft.model.planning.Planner;
import com.kaymansoft.model.planning.PlanningUtils;
import com.kaymansoft.settings.UserSettings;
import com.kaymansoft.settings.UserSettingsDBOpenHelper;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalInformationActivity extends Activity {

	Spinner sex, dailyActivity;
	EditText age, heightM, heightFt, weightKg, weightLb, desiredWeightKg, desiredWeightLb;	
	Button backButton,saveButton;
	ImageView incAge, decAge, incHeigth, decHeigth, incWeigth, decWeigth, incDesiredWeigth, decDesiredWeigth;
	View indicator1, indicator2;
	TextView mText,ftText,kgText1,lbText1,kgText2,lbText2;

	Animation indicatorAnimation;

	UserSettings us;
	double lastWeight;
	
	boolean firstTime = false;

	String[] sexValues, dailyActivityValues;
	
	static final String FIRST_TIME = "first_time";  

	static double step05 = 0.5, step001 = 0.01;
	static int step1 = 1;

	DecimalFormat formatter2decimals = new DecimalFormat("#0.00");
	DecimalFormat formatter1decimal = new DecimalFormat("#0.0");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);

		cacheViews();
		initComponents();
		handleIntent(getIntent());		
	}
	
	private void cacheViews() {
		backButton = (Button)findViewById(R.id.button3);
		saveButton = (Button)findViewById(R.id.button1);

		sex 			= (Spinner)findViewById(R.id.spinner1);
		age 			= (EditText)findViewById(R.id.editText1);
		heightM 		= (EditText)findViewById(R.id.editText3);
		heightFt 		= (EditText)findViewById(R.id.editText4);
		weightKg 		= (EditText)findViewById(R.id.editText5);
		weightLb 		= (EditText)findViewById(R.id.editText6);
		desiredWeightKg = (EditText)findViewById(R.id.editText2);
		desiredWeightLb = (EditText)findViewById(R.id.editText7);
		dailyActivity 	= (Spinner)findViewById(R.id.spinner2);		

		incAge = (ImageView)findViewById(R.id.imageView2);
		decAge = (ImageView)findViewById(R.id.imageView1);

		incHeigth = (ImageView)findViewById(R.id.imageView4);
		decHeigth = (ImageView)findViewById(R.id.imageView3);

		incWeigth = (ImageView)findViewById(R.id.imageView6);
		decWeigth = (ImageView)findViewById(R.id.imageView5);

		incDesiredWeigth = (ImageView)findViewById(R.id.imageView8);
		decDesiredWeigth = (ImageView)findViewById(R.id.imageView7);

		indicator1 = findViewById(R.id.imageView9);
		indicator2 = findViewById(R.id.imageView10);

		mText 	= (TextView)findViewById(R.id.textView7);
		ftText 	= (TextView)findViewById(R.id.textView10);
		kgText1 = (TextView)findViewById(R.id.textView8);
		lbText1 = (TextView)findViewById(R.id.textView11);
		kgText2 = (TextView)findViewById(R.id.textView9);
		lbText2 = (TextView)findViewById(R.id.textView12);		

	}

	private void initComponents() {
		
		//inicializar los campos para que no haya confusion
		age.setText("30");		
		heightM.setText("1.50");		
		heightFt.setText("4.92");		
		weightKg.setText("68.0");
		weightLb.setText("149.9");
		desiredWeightKg.setText("53.5");
		desiredWeightLb.setText("118.0");

		//animaciones
		indicatorAnimation = AnimationUtils.loadAnimation(this, R.anim.indicator_click_animation);

		//******* Inicializacion de los componentes ********/
		sexValues = getResources().getStringArray(R.array.sex_values_list);
		dailyActivityValues = getResources().getStringArray(R.array.daily_activity_values_list);
		
		//************* Mecánica de la vinculación entre los kg-lb y m-ft*/
		heightM.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				format2Decimals(heightM);
				if(!hasFocus) {
					double value;
					try {
						value = formatter2decimals.parse(heightM.getText().toString()).doubleValue();
						heightFt.setText(formatter2decimals.format(value*3.2808));
						showIdealWeight(value);
					} catch (Exception e) {
						heightFt.setText("0.00");
						heightM.setText("0.00");
					}
				}
			}							
		});	    
		heightFt.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				format2Decimals(heightFt);
				if(!hasFocus) {
					double value;
					try {
						value = formatter2decimals.parse(heightFt.getText().toString()).doubleValue();
						heightM.setText(formatter2decimals.format(value*0.3048));
						showIdealWeight(value*0.3048);
					} catch (Exception e) {
						heightFt.setText("0.00");
						heightM.setText("0.00");
					}
				}
			}
		});

		weightKg.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				format1Decimal(weightKg);
				if(!hasFocus) {
					double value;
					try {
						value = formatter1decimal.parse(weightKg.getText().toString()).doubleValue();
						weightLb.setText(formatter1decimal.format(value*2.2050));					
					} catch (Exception e) {
						weightKg.setText("0.0");
						weightLb.setText("0.0");
					}
				}
			}
		});
		weightLb.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				format1Decimal(weightLb);
				if(!hasFocus) {
					double value;
					try {
						value = formatter1decimal.parse(weightLb.getText().toString()).doubleValue();
						weightKg.setText(formatter1decimal.format(value*0.4535));					
					} catch (Exception e) {
						weightKg.setText("0.0");
						weightLb.setText("0.0");
					}
				}
			}
		});

		desiredWeightKg.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				format1Decimal(desiredWeightKg);
				if(!hasFocus) {
					double value;
					try {
						value = formatter1decimal.parse(desiredWeightKg.getText().toString()).doubleValue();
						desiredWeightLb.setText(formatter1decimal.format(value*2.2050));					
					} catch (Exception e) {
						desiredWeightKg.setText("0.0");
						desiredWeightLb.setText("0.0");
					}
				}
			}
		});
		desiredWeightLb.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				format1Decimal(desiredWeightLb);
				if(!hasFocus) {
					double value;
					try {
						value = formatter1decimal.parse(desiredWeightLb.getText().toString()).doubleValue();
						desiredWeightKg.setText(formatter1decimal.format(value*0.4535));					
					} catch (Exception e) {
						desiredWeightKg.setText("0.0");
						desiredWeightLb.setText("0.0");
					}
				}
			}
		});
		//********** Fin Mecánica de la vinculación entre los kg-lb y m-ft*/

		//****** Mecánica de las incremetnaciones y decrementaciones ********/
		incAge.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int value = Integer.parseInt(age.getText().toString())+step1;
				age.setText(String.valueOf(value));
			}
		});
		decAge.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int value = Integer.parseInt(age.getText().toString())-step1;
				age.setText(String.valueOf(value));		
			}
		});

		incHeigth.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				double value;
				try {
					value = formatter2decimals.parse(heightM.getText().toString()).doubleValue();
					value += step001;
					heightM.setText(formatter2decimals.format(value));
					heightFt.setText(formatter2decimals.format(value*3.2808));					
				} catch (Exception e) {
					;
				}				
			}
		});
		decHeigth.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				double value;
				try {
					value = formatter2decimals.parse(heightM.getText().toString()).doubleValue();
					value -= step001;
					heightM.setText(formatter2decimals.format(value));
					heightFt.setText(formatter2decimals.format(value*3.2808));					
				} catch (Exception e) {
					;
				}				
			}
		});

		incWeigth.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				double value;
				try {
					value = formatter1decimal.parse(weightKg.getText().toString()).doubleValue();
					value += step05;
					weightKg.setText(formatter1decimal.format(value));
					weightLb.setText(formatter1decimal.format(value*2.2050));					
				} catch (Exception e) {
					;
				}				
			}
		});
		decWeigth.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				double value;
				try {
					value = formatter1decimal.parse(weightKg.getText().toString()).doubleValue();
					value -= step05;
					weightKg.setText(formatter1decimal.format(value));
					weightLb.setText(formatter1decimal.format(value*2.2050));					
				} catch (Exception e) {
					;
				}				
			}
		});

		incDesiredWeigth.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				double value;
				try {
					value = formatter1decimal.parse(desiredWeightKg.getText().toString()).doubleValue();
					value += step05;
					desiredWeightKg.setText(formatter1decimal.format(value));
					desiredWeightLb.setText(formatter1decimal.format(value*2.2050));					
				} catch (Exception e) {
					;
				}				
			}
		});
		decDesiredWeigth.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				double value;
				try {
					value = formatter1decimal.parse(desiredWeightKg.getText().toString()).doubleValue();
					value -= step05;
					desiredWeightKg.setText(formatter1decimal.format(value));
					desiredWeightLb.setText(formatter1decimal.format(value*2.2050));					
				} catch (Exception e) {
					;
				}				
			}
		});
		//****** Fin Mecánica de las incremetnaciones y decrementaciones ********/

		//******* Mecánica de los click en los textos que designan las unidades ****/
		mText.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				heightM.requestFocus();
			}
		});

		ftText.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				heightFt.requestFocus();
			}
		});

		kgText1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				weightKg.requestFocus();
			}
		});

		lbText1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				weightLb.requestFocus();
			}
		});

		kgText2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				desiredWeightKg.requestFocus();
			}
		});

		lbText2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				desiredWeightLb.requestFocus();
			}
		});
		//******* Fin Mecánica de los click en los textos que designan las unidades ****/

		//******* Mecánica de los indicadores ***********/
		sex.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				indicator1.startAnimation(indicatorAnimation);
				return false;
			}
		});

		indicator1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				indicator1.startAnimation(indicatorAnimation);
				sex.performClick();
			}
		});

		dailyActivity.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				indicator2.startAnimation(indicatorAnimation);
				return false;
			}
		});

		indicator2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				indicator2.startAnimation(indicatorAnimation);
				dailyActivity.performClick();
			}
		});
		//******* Fin de Mecánica de los indicadores ***********/

		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();				
			}
		});

		saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				saveSettings();				
			}
		});

		us = new UserSettingsDBOpenHelper(this).getSettings();
		lastWeight = us.getWeightInKg();		

	}
	
	private void showIdealWeight(double heightInMeters) {
		WeigthRange idealWeightRange = BMIUtils.getIdealWeightRange(heightInMeters);
		double iw = (idealWeightRange.min + idealWeightRange.max)/2;
		String text = getResources().getString(R.string.ideal_weight_text);
		Toast.makeText(this,
				text + " " + 
				formatter1decimal.format(iw) + "kg/" +
				formatter1decimal.format(iw*2.205) + "lb",
				Toast.LENGTH_LONG).show();
		
	}	
	
	private void handleIntent(Intent intent) {		
		if(intent.getBooleanExtra(FIRST_TIME, false) || us.isFirstTime()) {
			firstTime = true;
			backButton.setVisibility(View.INVISIBLE);
		} else {
			showValues();
		}
	}

	private void format1Decimal(EditText editText) {
		double value;
		try {
			value = formatter1decimal.parse(editText.getText().toString()).doubleValue();
			editText.setText(formatter1decimal.format(value));					
		} catch (Exception e) {
			editText.setText("0.0");
		}
	}
	
	private void format2Decimals(EditText editText) {
		double value;
		try {
			value = formatter2decimals.parse(editText.getText().toString()).doubleValue();
			editText.setText(formatter2decimals.format(value));					
		} catch (Exception e) {
			editText.setText("0.00");
		}
	}
	
	private void saveSettings() {
		
		int index = sex.getSelectedItemPosition();
		us.setSex(sexValues[index!=-1? index : 0]);
		us.setAge(Integer.parseInt(age.getText().toString()));
		try {
			us.setHeightInM(formatter2decimals.parse(heightM.getText().toString()).doubleValue());
		} catch (ParseException e) {// no debería pasar
			Log.e(PersonalInformationActivity.class.getName(), "Error de parseo en la informacion de la altura");
		}
		try {
			us.setWeightInKg(formatter1decimal.parse(weightKg.getText().toString()).doubleValue());
		} catch (ParseException e) {// no debería pasar
			Log.e(PersonalInformationActivity.class.getName(), "Error de parseo en la informacion del peso");
		}
		try {
			us.setDesiredWeightInKg(formatter1decimal.parse(desiredWeightKg.getText().toString()).doubleValue());
		} catch (ParseException e) {// no debería pasar
			Log.e(PersonalInformationActivity.class.getName(), "Error de parseo en la informacion del peso deseado");
		}
		
		index = dailyActivity.getSelectedItemPosition();
		us.setDailyActivity(dailyActivityValues[index!=-1? index : 0]);
		us.setLastConfigurationTime(UserSettingsDBOpenHelper.DATE_FORMATTER.format(Calendar.getInstance().getTime()));
		
		
		boolean generateMenus = false;
		if(us.isFirstTime()) {
			us.setDietStartDay(UserSettingsDBOpenHelper.DATE_FORMATTER.format(Calendar.getInstance().getTime()));
			generateMenus = true;
		}
		
		if(!validateAll()) // si no pasa la validación no se puede guardar
			return;
		
		
		
		//si hay un nuevo peso configurado, hay que guardarlo
		//OJO esto siempre debería pasar en la primera vez
		AppDBOpenHelper db = new AppDBOpenHelper(this);
		db.addWeight(us.getWeightInKg(),MCons.DATE_FORMATTER.format(Calendar.getInstance().getTime()));
		db.close();
		
		us.save(); //salvar todos los datos
		//cancelar la notificacion de actualizar peso, en caso de que esté activa
		NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mgr.cancel(DietInfoActivity.UPDATE_WEIGHT_NOTIFICATION_ID);
		
		setResult(RESULT_OK);
		
		//si se deben generar los menús
		if(generateMenus) {
			generateMenus();
		} else {		
			finish(); //salir
		}
	}

	private void generateMenus() {
		Resources res = getResources();
		final ProgressDialog pd = ProgressDialog.show(
				this, 
				res.getString(R.string.generating_menus_text), 
				res.getString(R.string.generating_menus_wait_text),
				true,
				false);
		AsyncTask<Void, Void, Void> saver = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				AppDBOpenHelper db = new AppDBOpenHelper(PersonalInformationActivity.this);
				PlanningUtils.load(db);
				
				Resources res = getResources();
				
				Planner p = new Planner(us, db);
				p.setDefaultMenuDescription(res.getString(R.string.planification_text).toUpperCase());
				p.setMenuNamesPerMeal(
						res.getString(R.string.breakfast_text), 
						res.getString(R.string.snack_text),
						res.getString(R.string.lunch_text),
						res.getString(R.string.dinner_text));
				p.generatePlanning();
				
				db.close();
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				pd.hide();
				finish();
			}
		};
		saver.execute((Void)null);
		
	}

	private boolean validateAll() {
		int ageValue = us.getAge();
		if((12 > ageValue) || (120 < ageValue)) {
			Toast.makeText(this, R.string.invalid_age_text, Toast.LENGTH_SHORT).show();
			return false;
		}
		double heightValue = us.getHeightInM();
		if((heightValue < 0.5) || (heightValue > 2.2)) {
			Toast.makeText(this, R.string.invalid_height_text, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		double weightValue = us.getWeightInKg();
		if((weightValue < 20.0) || (weightValue > 300.0)) {
			Toast.makeText(this, R.string.invalid_weight_text, Toast.LENGTH_SHORT).show();
			return false;
		}
		if(BMIUtils.getActualBMIClassification(us) == BMIClassification.HEALTHY) {
			Toast.makeText(this, R.string.healthy_bmi_text, Toast.LENGTH_SHORT).show();
			return false;
		}		
		
		if(BMIUtils.getDesiredBMIClassification(us) != BMIClassification.HEALTHY) {
			Toast.makeText(this, R.string.invalid_desired_weight_text, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}

	private void showValues() {
		if(!us.isFirstTime()) {
			int index = findPosition(sexValues,us.getSex());
			if(index!=-1)
				sex.setSelection(index);
			index = findPosition(dailyActivityValues,us.getDailyActivity());
			if(index!=-1)
				dailyActivity.setSelection(index);
			age.setText(String.valueOf(us.getAge()));
			double value = us.getHeightInM();
			heightM.setText(formatter2decimals.format(value));
			heightFt.setText(formatter2decimals.format(value*3.2808));
			value = us.getWeightInKg();
			weightKg.setText(formatter1decimal.format(value));
			weightLb.setText(formatter1decimal.format(value*2.2050));
			value = us.getDesiredWeightInKg();
			desiredWeightKg.setText(formatter1decimal.format(value));
			desiredWeightLb.setText(formatter1decimal.format(value*2.2050));			
		}
	}

	private int findPosition(String[] array, String text) {
		for (int i = 0; i < array.length; i++) {
			if(array[i].equals(text))
				return i;
		}
		return -1;
	}

	@Override
	public void onBackPressed() {
		//en caso de que sea por primera vez no se puede salir hasta que no se configure la información necesaria
		if(!firstTime)
			super.onBackPressed();		
	}

}
