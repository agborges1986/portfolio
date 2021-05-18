package com.kaymansoft.gui;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kaymansoft.R;
import com.kaymansoft.alarm.AlarmUtils;
import com.kaymansoft.calories.CaloriesConsumptionUtils;
import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.MCons;
import com.kaymansoft.model.cursors.WeightCursor;
import com.kaymansoft.model.elements.Weight;
import com.kaymansoft.settings.UserSettings;
import com.kaymansoft.settings.UserSettingsDBOpenHelper;

public class DietInfoActivity extends Activity {

	TextView					consumedCalories, recommendedCalories, remainingCalories, nextAlarm, remainingTime;
	ProgressBar					calConsumption;
	DecimalFormat				caloriesFormatter;
	SimpleDateFormat			nextAlarmFormatter;
	SimpleDateFormat			timeFormatter, timeFormatter2;
	Button						eat;

	public static final int		UPDATE_WEIGHT_NOTIFICATION_ID	= 1019;
	private static final int	NEW_MEAL_CODE					= 1029;

	private static final int	MILLISECONDS_IN_ONE_HOUR		= 1000 * 60 * 60;
	private static final int	MILLISECONDS_IN_ONE_MINUTE		= 1000 * 60;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diet_info);

		cacheViews();
		initComponents();

	}

	private void cacheViews() {
		recommendedCalories = (TextView) findViewById(R.id.textView4);
		consumedCalories = (TextView) findViewById(R.id.textView6);
		remainingCalories = (TextView) findViewById(R.id.textView13);
		nextAlarm = (TextView) findViewById(R.id.textView9);
		remainingTime = (TextView) findViewById(R.id.textView11);

		calConsumption = (ProgressBar) findViewById(R.id.progressBar1);

		eat = (Button) findViewById(R.id.button1);
	}

	private void initComponents() {

		caloriesFormatter = new DecimalFormat("###0");
		timeFormatter = new SimpleDateFormat("H:mm ");
		timeFormatter2 = new SimpleDateFormat("m ");
		nextAlarmFormatter = new SimpleDateFormat("h:mm a");

		eat.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DietInfoActivity.this, TakeMealActivity.class);
				startActivityForResult(intent, NEW_MEAL_CODE);
			}
		});

		showData();

	}

	@Override
	protected void onResume() {
		super.onResume();
		showData();
	}

	private void showData() {
		AppDBOpenHelper db = new AppDBOpenHelper(this);
		WeightCursor cur = db.getLastWeightsFirst();
		if (cur.getCount() > 0) {
			cur.moveToFirst();
			Weight lastWeight = cur.getWeight();
			Calendar weightTime = Calendar.getInstance();
			try {
				weightTime.setTime(MCons.DATE_FORMATTER.parse(lastWeight.getDate()));
				weightTime.add(Calendar.DAY_OF_YEAR, 10);//aumentar 10 días en la fecha del último peso configurado
				NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				//si hoy hace más de diez días que se configuró el peso
				if (weightTime.before(Calendar.getInstance())) {
					//mostrar mensaje
					Toast.makeText(this, getResources().getString(R.string.update_weight_text), Toast.LENGTH_LONG).show();
					Resources res = getResources();

					//mostrar notificación
					int icon = R.drawable.notification_weight_icon;
					CharSequence tickerText = res.getString(R.string.notification_weight_title);
					long when = System.currentTimeMillis();
					Context context = getApplicationContext();
					CharSequence contentTitle = res.getString(R.string.notification_weight_expand_title);
					CharSequence contentText = res.getString(R.string.notification_weight_text);

					Intent notificationIntent = new Intent(this, PersonalInformationActivity.class);
					PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

					Notification updateWeight = new Notification(icon, tickerText, when);
					updateWeight.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

					mgr.notify(UPDATE_WEIGHT_NOTIFICATION_ID, updateWeight);
				} else {
					//cancelar la notificación en caso de que el peso se haya actualizado
					mgr.cancel(UPDATE_WEIGHT_NOTIFICATION_ID);
				}
			} catch (ParseException e) {
				Log.e(DietInfoActivity.class.getName(), "Error parseando la fecha en la BD (Tabla Weights)");
			}
		} else {
			Log.e(DietInfoActivity.class.getName(), "no se ha encontrado un peso registrado en la BD!");
		}
		cur.close();
		db.close();

		UserSettingsDBOpenHelper usDB = new UserSettingsDBOpenHelper(this);

		UserSettings us = usDB.getSettings();
		double maxCalories = CaloriesConsumptionUtils.getRecommendedDailyCaloriesConsumption(us);
		usDB.close();

		setAlarmAndRemainingTime();

		recommendedCalories.setText(caloriesFormatter.format(maxCalories));

		double calories = CaloriesConsumptionUtils.getTodayConsumedCalories(this, us);
		consumedCalories.setText(caloriesFormatter.format(calories));

		double remCalories = maxCalories - calories;
		remCalories = remCalories > 0 ? remCalories : 0;
		remainingCalories.setText(caloriesFormatter.format(remCalories));

		calConsumption.setMax((int) maxCalories);
		if (calories < maxCalories)
			calConsumption.setProgress((int) calories);
		else
			calConsumption.setProgress(calConsumption.getMax());

		if (calories >= maxCalories)
			consumedCalories.setTextColor(getResources().getColor(R.color.red));

		/**
		 * TODO sería bueno cambiar de color el progress bar en dependecia del
		 * porcentaje de calorías consumidas con respecto al máximo total
		 */
	}

	private void setAlarmAndRemainingTime() {
		UserSettingsDBOpenHelper usDB = new UserSettingsDBOpenHelper(this);
		UserSettings us = usDB.getSettings();
		Calendar nextAlarmTime = AlarmUtils.getNextAlarmTime(us);
		usDB.close();

		if (nextAlarmTime == null) {
			Log.e(DietInfoActivity.class.getName(), "no se pudo obtener la próxima alarma");
			nextAlarm.setText("");
		} else {
			nextAlarm.setText(nextAlarmFormatter.format(nextAlarmTime.getTime()));
		}

		if (nextAlarmTime == null) {
			Log.e(DietInfoActivity.class.getName(), "No se pudo obtener el horario de la próxima alarma");
		} else {
			Calendar tmp = Calendar.getInstance();
			long diffTime = nextAlarmTime.getTimeInMillis() - tmp.getTimeInMillis();
			tmp.set(Calendar.HOUR_OF_DAY, (int) diffTime / MILLISECONDS_IN_ONE_HOUR);
			tmp.set(Calendar.MINUTE, (int) ((diffTime % MILLISECONDS_IN_ONE_HOUR) / MILLISECONDS_IN_ONE_MINUTE));

			//si queda menos de una hora
			if (diffTime / MILLISECONDS_IN_ONE_MINUTE < 60) {
				remainingTime.setText(timeFormatter2.format(tmp.getTime()) + getString(R.string.minutes_text));
			} else {
				remainingTime.setText(timeFormatter.format(tmp.getTime()) + getString(R.string.hours_text));
			}
		}

		new Handler().postAtTime(new Runnable() {
			public void run() {
				setAlarmAndRemainingTime();
			}

		}, SystemClock.uptimeMillis() + MILLISECONDS_IN_ONE_MINUTE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case NEW_MEAL_CODE:
					showData();
					break;
			}
		}
	}

}
