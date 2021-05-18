package com.kaymansoft.gui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.kaymansoft.R;
import com.kaymansoft.settings.UserSettings;
import com.kaymansoft.settings.UserSettingsDBOpenHelper;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimesForDietActivity extends Activity {
	
	TextView usualWakeUpTime, weekendsWakeUpTime, usualSleepTime, weekendsSleepTime;
	
	OnTimeSetListener usualWakeUpTimeSetter, weekendsWakeUpTimeSetter, usualSleepTimeSetter, weekendsSleepTimeSetter;
	int uwh = 7,uwm = 0, wwh = 9, wwm = 0, ush = 22 , usm = 0, wsh = 23 , wsm = 0;
	
	CheckBox weekendsWakeUp, weekendsSleep;
	
	Button back,save;
	
	private static final int USUAL_WAKE_UP_TIME_PICKER_DIALOG 		= 19;
	private static final int WEEKENDS_WAKE_UP_TIME_PICKER_DIALOG 	= 20;
	private static final int USUAL_SLEEP_TIME_PICKER_DIALOG 		= 21;
	private static final int WEEKENDS_SLEEP_TIME_PICKER_DIALOG 		= 22;
	
	public static final int BACK_FROM_TIMES = 1029;
	
	public static final String FIRST_TIME = "first_time";
	boolean firstTime = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_times);
		
		cacheViews();
		initComponents();
		handleIntent(getIntent());		
	}
	
	private void handleIntent(Intent intent) {
		firstTime = intent.getBooleanExtra(FIRST_TIME, false);
		if(!firstTime)
			showValues();		
	}
	
	private void cacheViews() {

		usualWakeUpTime 	= (TextView) findViewById(R.id.textView1);
		weekendsWakeUpTime 	= (TextView) findViewById(R.id.textView2);
		usualSleepTime 		= (TextView) findViewById(R.id.textView3);
		weekendsSleepTime 	= (TextView) findViewById(R.id.textView4);
		
		weekendsWakeUp	= (CheckBox) findViewById(R.id.checkBox1);
		weekendsSleep 	= (CheckBox) findViewById(R.id.checkBox2);
		
		back = (Button) findViewById(R.id.button3);
		save = (Button) findViewById(R.id.button1);	
		
	}

	private void initComponents() {
		
		usualWakeUpTime.setText(formatTime(uwh, uwm));
		weekendsWakeUpTime.setText(formatTime(wwh, wwm));
		
		usualSleepTime.setText(formatTime(ush, usm));
		weekendsSleepTime.setText(formatTime(wsh, wsm));
		
		//weekendsWakeUpTime.setEnabled(true);
		weekendsWakeUpTime.setVisibility(View.VISIBLE);
		weekendsWakeUp.setChecked(true);
		
		//weekendsSleepTime.setEnabled(true);
		weekendsSleepTime.setVisibility(View.GONE);
		weekendsSleep.setChecked(false);
				
		weekendsSleep.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//weekendsSleepTime.setEnabled(isChecked);
				weekendsSleepTime.setVisibility(isChecked? View.VISIBLE : View.GONE);
			}			
		});
		weekendsWakeUp.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//weekendsWakeUpTime.setEnabled(isChecked);
				weekendsWakeUpTime.setVisibility(isChecked? View.VISIBLE : View.GONE);
			}			
		});
		
		usualWakeUpTimeSetter = new OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				usualWakeUpTime.setText(formatTime(uwh = hourOfDay, uwm = minute));
			}
		};
		weekendsWakeUpTimeSetter = new OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				weekendsWakeUpTime.setText(formatTime(wwh = hourOfDay, wwm = minute));
			}
		};
		usualSleepTimeSetter = new OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				usualSleepTime.setText(formatTime(ush = hourOfDay, usm = minute));
			}
		};
		weekendsSleepTimeSetter = new OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				weekendsSleepTime.setText(formatTime(wsh = hourOfDay, wsm = minute));
			}
		};
				
		usualWakeUpTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(USUAL_WAKE_UP_TIME_PICKER_DIALOG);				
			}
		});
		weekendsWakeUpTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(WEEKENDS_WAKE_UP_TIME_PICKER_DIALOG);				
			}
		});
		usualSleepTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(USUAL_SLEEP_TIME_PICKER_DIALOG);				
			}
		});
		weekendsSleepTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(WEEKENDS_SLEEP_TIME_PICKER_DIALOG);				
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				goBack();
			}
		});
		save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				saveTimes();
			}
		});
	}
	
	private void showValues() {
		UserSettings us = new UserSettingsDBOpenHelper(this).getSettings();
		
		SimpleDateFormat formatter = UserSettingsDBOpenHelper.TIME_FORMATTER;
		Calendar calendar = Calendar.getInstance();
		
		//poner usual_wakeup
		try {
			calendar.setTime(formatter.parse(us.getUsualWakeUpTime()));
			uwh = calendar.get(Calendar.HOUR_OF_DAY); uwm = calendar.get(Calendar.MINUTE);
			usualWakeUpTime.setText(formatTime(uwh,uwm));			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//poner weekends_wakeup
		try {
			calendar.setTime(formatter.parse(us.getWeekendsWakeUpTime()));
			wwh = calendar.get(Calendar.HOUR_OF_DAY); wwm = calendar.get(Calendar.MINUTE);
			weekendsWakeUpTime.setText(formatTime(wwh,wwm));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//poner usual_sleep
		try {
			calendar.setTime(formatter.parse(us.getUsualSleepTime()));
			ush = calendar.get(Calendar.HOUR_OF_DAY); usm = calendar.get(Calendar.MINUTE);
			usualSleepTime.setText(formatTime(ush,usm));			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//poner weekneds_sleep
		try {
			calendar.setTime(formatter.parse(us.getWeekendsSleepTime()));
			wsh = calendar.get(Calendar.HOUR_OF_DAY); wsm = calendar.get(Calendar.MINUTE);
			weekendsSleepTime.setText(formatTime(wsh,wsm));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		weekendsWakeUp.setChecked((uwh != wwh) || (uwm != wwm));
		//weekendsWakeUpTime.setEnabled(weekendsWakeUp.isChecked());
		weekendsWakeUpTime.setVisibility(weekendsWakeUp.isChecked()? View.VISIBLE : View.GONE);
		weekendsSleep.setChecked((ush != wsh) || (usm != wsm));
		//weekendsSleepTime.setEnabled(weekendsSleep.isChecked());
		weekendsSleepTime.setVisibility(weekendsSleep.isChecked()? View.VISIBLE : View.GONE);		
		
	}
	
	private void saveTimes() {
		UserSettings us = new UserSettingsDBOpenHelper(this).getSettings();
		
		SimpleDateFormat formatter = UserSettingsDBOpenHelper.TIME_FORMATTER;
		Calendar calendar = Calendar.getInstance();
		
		//salvar usual_wake_up
		calendar.set(Calendar.HOUR_OF_DAY, uwh);
		calendar.set(Calendar.MINUTE, uwm);
		us.setUsualWakeUpTime(formatter.format(calendar.getTime()));
		
		//salvar weekends_wake_up
		if(weekendsWakeUp.isChecked()) {
			calendar.set(Calendar.HOUR_OF_DAY, wwh);
			calendar.set(Calendar.MINUTE, wwm);
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, uwh);
			calendar.set(Calendar.MINUTE, uwm);			
		}
		us.setWeekendsWakeUpTime(formatter.format(calendar.getTime()));
		
		//salvar usual_sleep
		calendar.set(Calendar.HOUR_OF_DAY, ush);
		calendar.set(Calendar.MINUTE, usm);
		us.setUsualSleepTime(formatter.format(calendar.getTime()));
		
		//salvar weekends_sleep
		if(weekendsSleep.isChecked()) {
			calendar.set(Calendar.HOUR_OF_DAY, wsh);
			calendar.set(Calendar.MINUTE, wsm);
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, ush);
			calendar.set(Calendar.MINUTE, usm);			
		}
		us.setWeekendsSleepTime(formatter.format(calendar.getTime()));
		
		us.save();
		
		setResult(RESULT_OK);
		finish();
		
	}

	private String formatTime(int hour, int minute) {
		return new StringBuilder()
			.append(hour>12 ? hour-12 : (hour==0 ? 12 : hour))
			.append(":")
			.append(minute>10 ? minute : "0"+ minute)
			.append(" ")
			.append(hour>11? "pm" : "am")
			.toString();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case USUAL_WAKE_UP_TIME_PICKER_DIALOG:
				return new TimePickerDialog(this,usualWakeUpTimeSetter,uwh,uwm,false);
			case WEEKENDS_WAKE_UP_TIME_PICKER_DIALOG:
				return new TimePickerDialog(this,weekendsWakeUpTimeSetter,wwh,wwm,false);
			case USUAL_SLEEP_TIME_PICKER_DIALOG:
				return new TimePickerDialog(this,usualSleepTimeSetter,ush,usm,false);
			case WEEKENDS_SLEEP_TIME_PICKER_DIALOG:
				return new TimePickerDialog(this,weekendsSleepTimeSetter,wsh,wsm,false);
		}
		return null;
	}
	
	@Override
	public void onBackPressed() {
		goBack();		
	}

	private void goBack() {
		//este codigo es solo para si se llamo desde el splash por primera vez
		setResult(BACK_FROM_TIMES);
		finish();
	}
	
}
