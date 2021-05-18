package com.kaymansoft.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.kaymansoft.R;
import com.kaymansoft.alarm.AlarmUtils;
import com.kaymansoft.settings.UserSettings;
import com.kaymansoft.settings.UserSettingsDBOpenHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends Activity{
	
	static final int SET_USER_SETTINGS 			= 1023;
	static final int SET_USER_TIMES_FOR_DIET 	= 1037;
	static final int AGREEMENT 					= 1017;
	static final int DIET_BENEFITS 				= 1007;

	
	static final long DB_SIZE_BYTES = 1500 * 1024;
	
	UserSettingsDBOpenHelper db;
	UserSettings us;
	ProgressBar pg;
	TextView label;
	Handler handler;
	
	private static long WAIT_TIME = 2000;
	
	@Override
	protected void onCreate(Bundle back) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(back);
		setContentView(R.layout.splash);
		
		cacheViews();
		initComponents();
		
		waitForLoading();		
		
	}

	private void waitForLoading() {
		pg.setProgress(0);
		pg.setSecondaryProgress(0);
		pg.setMax(1000);
		handler.postAtTime(new Runnable() {			
			public void run() {
				doStuff();				
			}
		}, SystemClock.uptimeMillis() + WAIT_TIME/2);		
	}

	private void cacheViews() {
		pg = (ProgressBar) findViewById(R.id.progressBar1);
		label = (TextView) findViewById(R.id.textView2);
	}

	private void initComponents() {
		pg.setProgress(0);
		handler = new Handler();
	}

	private void doStuff() {
		db = new UserSettingsDBOpenHelper(this);
		us = db.getSettings();
		
		pg.setProgress(200);
		
		if(us.isFirstTime()) {
			
			label.setText(R.string.init_database_text);		
			//TODO verificar lo del tamaño de la BD
			//la siguente línea está comentada porque no funciona, pero se debería hacer algo como eso 
			//totalSizeKb = getAssets().openFd("AppDB.db").getDeclaredLength();
			//en lugar de poner el tamaño fijo a 1500 KB
			final long totalSizeKb = DB_SIZE_BYTES/1024; //kilobytes 
			
			pg.setMax((int)totalSizeKb);
			//tarea asíncrona para copiar la BD fuera del hilo de la interfaz
			AsyncTask<Void, Long, Void> copyDB = new AsyncTask<Void, Long, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					File file = new File("/data/data/com.kaymansoft/databases/AppDB.db");
					InputStream in = null; 
					OutputStream out = null;				
					
					try {					
						in = getAssets().open("AppDB.db");
						out = new FileOutputStream(file);
						long kbTransfered = 0;
						byte[] buffer = new byte[1024];
						int read = in.read(buffer);
						while (read != -1) {
							out.write(buffer, 0, read);
							kbTransfered += 1;
							publishProgress(kbTransfered);
							read = in.read(buffer);
						}
					} catch (IOException e) {
						Log.e(this.getClass().toString(),e.getMessage());
					} finally {
						if(out !=null)
							try {
								out.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						if(in != null)
							try {
								in.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
					}
					return null;
				}
				@Override
				protected void onProgressUpdate(Long... values) {
					pg.setProgress(pg.getProgress() + (int) (values[0]*600/totalSizeKb));
				}
				@Override
				protected void onPostExecute(Void result) {
					label.setText(R.string.init_user_info_text);
					handler.postAtTime(new Runnable() {
						public void run() {
							showAgreement();
						}
					},  SystemClock.uptimeMillis() + WAIT_TIME/4);
				}
			};

			copyDB.execute((Void)null);
		
		} else {		
			handler.postAtTime(new Runnable() {
				public void run() {
					pg.setProgress(pg.getMax());
					startMainActivity();
				}
			}, SystemClock.uptimeMillis() + WAIT_TIME/2);
		}
	}
	
	private void showAgreement() {
		Intent newIntent = new Intent();
		newIntent.setClass(SplashActivity.this, AgreementActivity.class);
		startActivityForResult(newIntent,AGREEMENT);
	}
	
	private void showDietBenefits() {
		Intent newIntent = new Intent();
		newIntent.setClass(SplashActivity.this, DietBenefitsActivity.class);
		startActivityForResult(newIntent,DIET_BENEFITS);
	}	
	
	private void defineUserSettings(boolean firstTime) {
		Intent newIntent = new Intent();
		newIntent.setClass(SplashActivity.this, PersonalInformationActivity.class);
		if(firstTime)
			newIntent.putExtra(PersonalInformationActivity.FIRST_TIME,true);
		startActivityForResult(newIntent,SET_USER_SETTINGS);
	}
	
	private void defineUserTimes() {
		Intent newIntent = new Intent();
		newIntent.setClass(this, TimesForDietActivity.class);
		newIntent.putExtra(TimesForDietActivity.FIRST_TIME,true);
		startActivityForResult(newIntent,SET_USER_TIMES_FOR_DIET);
	}	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			switch (requestCode) {
				case AGREEMENT:
					showDietBenefits();
					break;
				case DIET_BENEFITS:
					defineUserSettings(true);
					break;
				case SET_USER_SETTINGS:
					defineUserTimes();
					break;
				case SET_USER_TIMES_FOR_DIET:
					initAlarms();
					label.setText(R.string.init_app_text);
					handler.postAtTime(new Runnable() {
						public void run() {
							pg.setProgress(pg.getMax());
							startMainActivity();
						}
					}, SystemClock.uptimeMillis() + WAIT_TIME/4);
					break;
			}
		}
		if(resultCode == TimesForDietActivity.BACK_FROM_TIMES) {
			defineUserSettings(false);
		}
		if(resultCode == RESULT_CANCELED) {
			switch (requestCode) {
				case AGREEMENT:
					finish();
					break;
				case DIET_BENEFITS:
					defineUserSettings(true);
					break;
				case SET_USER_SETTINGS:
					finish();
					break;
				case SET_USER_TIMES_FOR_DIET:
					finish();
					break;
			}
		}
	}

	private void initAlarms() {
		AlarmUtils.setupNextAlarm(this, us);
		AlarmUtils.setupAlarmFixer(this);
	}
	
	private void startMainActivity() {
		startActivity(new Intent(this,MainAplicationActivity.class));
		db.close();
		finish();
	}
	
	@Override
	public void onBackPressed() {
		;//ignorar
	}
	
}
