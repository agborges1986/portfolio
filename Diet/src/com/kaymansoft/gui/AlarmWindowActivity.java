package com.kaymansoft.gui;

import java.io.IOException;
import java.text.DecimalFormat;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kaymansoft.R;
import com.kaymansoft.alarm.AlarmUtils;
import com.kaymansoft.calories.CaloriesConsumptionUtils;
import com.kaymansoft.settings.UserSettings;
import com.kaymansoft.settings.UserSettingsDBOpenHelper;

public class AlarmWindowActivity extends Activity {

	Button						ok, later;
	ProgressBar					calories;
	TextView					consumption;
	MediaPlayer					mp;
	Vibrator					vbr;
	OnPreparedListener			play;
	long[]						pattern						= new long[] { 1000, 200, 500, 400 };

	//TODO debería estar en Shared preferences
	private static final long	MAX_VIBRATION_TIME			= 20000;								//20 segundos

	static final DecimalFormat	formatter					= new DecimalFormat("0");
	public static final int		MEAL_TIME_NOTIFICATION_ID	= 17;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//para que despierte la pantlla
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		setContentView(R.layout.alarm_window);

		cacheViews();
		initComponents();

	}

	private void cacheViews() {
		ok = (Button) findViewById(R.id.button2);
		later = (Button) findViewById(R.id.button1);
		calories = (ProgressBar) findViewById(R.id.progressBar1);
		consumption = (TextView) findViewById(R.id.textView3);
	}

	private void initComponents() {

		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//cuando se acepta esta actividad se ocultar la notificación lanzada para el BB
				NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				mgr.cancel(MEAL_TIME_NOTIFICATION_ID);
				//stopSoundAndVibration();
				startActivity(new Intent(AlarmWindowActivity.this, TakeMealActivity.class));
				finish();
			}
		});

		later.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//stopSoundAndVibration();
				finish();
			}
		});

		mp = new MediaPlayer();
		vbr = (Vibrator) getSystemService(VIBRATOR_SERVICE);

		play = new OnPreparedListener() {
			public void onPrepared(MediaPlayer mp) {
				mp.start();
			}
		};

	}

	private void playSoundAndVibration() {
		if (AlarmUtils.isUsingAlarmSound(this)) {
			Exception ex = null;
			try {
				mp.setDataSource(this,
						RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM));
				mp.setOnPreparedListener(play);
				mp.prepareAsync();
				//mp.prepare();
				//mp.start();
			} catch (IllegalArgumentException e) {
				ex = e;
				e.printStackTrace();
			} catch (SecurityException e) {
				ex = e;
				e.printStackTrace();
			} catch (IllegalStateException e) {
				ex = e;
				e.printStackTrace();
			} catch (IOException e) {
				ex = e;
				e.printStackTrace();
			}
			if (ex != null)
				Log.e(getClass().getName(), " Error al reproducir sonido de alarma, excepción: " + ex.getMessage());
		}
		if (AlarmUtils.isUsingAlarmVibration(this)) {
			vbr.vibrate(pattern, 0);
			new Handler().postAtTime(new Runnable() {
				public void run() {
					vbr.cancel();
				}
			}, SystemClock.uptimeMillis() + MAX_VIBRATION_TIME);
		}
	}

	private void stopSoundAndVibration() {
		mp.stop();
		vbr.cancel();
	}

	private void showData() {

		//mostrar los datos
		UserSettings us = new UserSettingsDBOpenHelper(this).getSettings();
		double consumedCalories = CaloriesConsumptionUtils.getTodayConsumedCalories(
				this, us);
		double maxCalories = CaloriesConsumptionUtils.getRecommendedDailyCaloriesConsumption(us);

		calories.setMax((int) maxCalories);
		if (consumedCalories < maxCalories)
			calories.setProgress((int) consumedCalories);
		else
			calories.setProgress(calories.getMax());

		consumption.setText(formatter.format(consumedCalories) + "/" + formatter.format(maxCalories));

	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//mostrando datos
		showData();
		playSoundAndVibration();
	}

	@Override
	protected void onPause() {
		stopSoundAndVibration();
		super.onPause();
	}

}
