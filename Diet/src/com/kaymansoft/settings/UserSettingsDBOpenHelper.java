package com.kaymansoft.settings;

import java.text.SimpleDateFormat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserSettingsDBOpenHelper extends SQLiteOpenHelper {
	
	public static final String TIME_FORMAT = "HH:mm"; // 21:04 para las 9 y 4 pm
	public static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat(TIME_FORMAT);
	public static final String DATE_FORMAT = "yyyy-MM-dd"; //2012-05-29 para 29 e mayo del 2012 
	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);
	
	private static UserInfoImpl us;// = new UserInfoImpl();
		
	private class UserInfoImpl implements UserSettings {
			
		@SuppressWarnings("unused")
		private long id = 1;
		private boolean isFirstTime = true;
		
		String userName = "";
		String sex = "";
		int age = 0;
		double weightInKg = 0.0;
		double heightInM = 0.0;
		double desiredWeightInKg = 0.0;
		String dailyActivity = "";
		String dietStartDay = "";
		int expectedDietDays = 0;
		String lastConfigurationTime = "";
		String usualWakeUpTime = "";
		String weekendsWakeUpTime = "";
		String usualSleepTime = "";
		String weekendsSleepTime = "";
				
		public UserInfoImpl() {	}
		
		public int getExpectedDietDays() {
			return expectedDietDays;
		}
		public void setExpectedDietDays(int expectedDietDays) {
			this.expectedDietDays = expectedDietDays;
		}
		public String getLastConfigurationTime() {
			return lastConfigurationTime;
		}
		public void setLastConfigurationTime(String lastConfigurationTime) {
			this.lastConfigurationTime = lastConfigurationTime;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public double getWeightInKg() {
			return weightInKg;
		}
		public void setWeightInKg(double weightInKg) {
			this.weightInKg = weightInKg;
		}
		public double getHeightInM() {
			return heightInM;
		}
		public void setHeightInM(double heightInM) {
			this.heightInM = heightInM;
		}
		public double getDesiredWeightInKg() {
			return desiredWeightInKg;
		}
		public void setDesiredWeightInKg(double expectedWeightInKg) {
			this.desiredWeightInKg = expectedWeightInKg;
		}
		public String getDailyActivity() {
			return dailyActivity;
		}
		public void setDailyActivity(String dailyActivity) {
			this.dailyActivity = dailyActivity;
		}
		public String getDietStartDay() {
			return dietStartDay;
		}
		public void setDietStartDay(String dietStartDay) {
			this.dietStartDay = dietStartDay;
		}
		public void setFirstTime(boolean isFirstTime) {
			this.isFirstTime = isFirstTime;
		}
		public boolean isFirstTime() {
			return isFirstTime;
		}

		public String getUsualWakeUpTime() {
			return usualWakeUpTime;
		}

		public void setUsualWakeUpTime(String usualWakeUpTime) {
			this.usualWakeUpTime = usualWakeUpTime;
		}

		public String getWeekendsWakeUpTime() {
			return weekendsWakeUpTime;
		}

		public void setWeekendsWakeUpTime(String weekendsWakeUpTime) {
			this.weekendsWakeUpTime = weekendsWakeUpTime;
		}

		public String getUsualSleepTime() {
			return usualSleepTime;
		}

		public void setUsualSleepTime(String usualSleepTime) {
			this.usualSleepTime = usualSleepTime;
		}

		public String getWeekendsSleepTime() {
			return weekendsSleepTime;
		}

		public void setWeekendsSleepTime(String weekendsSleepTime) {
			this.weekendsSleepTime = weekendsSleepTime;
		}
		public void save() {
			saveSettings();
		}
	}
		
	private static final int SETTINGS_ID = 1;
	
	private static final String DATABASE_NAME = "UserSettings.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String USER_SETTINGS_TABLE = "UserSettings";
	public static final String ID_FIELD = "_id";
	public static final String NAME_FIELD = "name";
	public static final String SEX_FIELD = "sex";
	public static final String AGE_FIELD = "age";
	public static final String WEIGHT_FIELD = "weight";
	public static final String HEIGHT_FIELD = "height";
	public static final String DESIRED_WEIGHT_FIELD = "expected_weight";
	public static final String DIET_START_DAY_FIELD = "diet_start_day";
	public static final String EXPECTED_DIET_DAYS_FIELD = "expected_diet_days";
	public static final String LAST_CONFIGURATION_TIME_FIELD = "last_config_time";
	public static final String DAILY_ACTIVITY_FIELD = "daily_activity";
	public static final String USUAL_WAKE_UP_TIME_FIELD = "usual_wake_up_time";
	public static final String WEEKENDS_WAKE_UP_TIME_FIELD = "weekends_wake_up_time";
	public static final String USUAL_SLEEP_TIME_FIELD = "usual_sleep_time";
	public static final String WEEKENDS_SLEEP_TIME_FIELD = "weekends_sleep_time";

	
	private static final String CREATE_USER_SETTINGS_TABLE =
			"CREATE TABLE " + USER_SETTINGS_TABLE + " ( " +
			ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
			NAME_FIELD + " TEXT, " +
			SEX_FIELD + " TEXT, " +
			AGE_FIELD + " INTEGER, " +
			WEIGHT_FIELD + " REAL, " +
			HEIGHT_FIELD + " REAL, " +
			DESIRED_WEIGHT_FIELD + " REAL, " +
			DAILY_ACTIVITY_FIELD + " TEXT, " +
			DIET_START_DAY_FIELD + " TEXT, " +
			EXPECTED_DIET_DAYS_FIELD + " INTEGER , " +
			LAST_CONFIGURATION_TIME_FIELD + " TEXT, " +
			USUAL_WAKE_UP_TIME_FIELD + " TEXT, " +
			WEEKENDS_WAKE_UP_TIME_FIELD + " TEXT, " +
			USUAL_SLEEP_TIME_FIELD + " TEXT, " +		
			WEEKENDS_SLEEP_TIME_FIELD + " TEXT " +			
			" )";
		
	public UserSettingsDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_USER_SETTINGS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	public UserSettings getSettings() {
		if(us==null) us = new UserInfoImpl();
		boolean isFirstTime = !hadBeenInitialized();
		if(!isFirstTime) {
			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = db.rawQuery("SELECT * FROM " + USER_SETTINGS_TABLE, null);
			cursor.moveToFirst();
			us.setFirstTime				(isFirstTime);
			us.setUserName 				(cursor.getString(cursor.getColumnIndex(NAME_FIELD)));
			us.setSex 					(cursor.getString(cursor.getColumnIndex(SEX_FIELD)));
			us.setAge 					(cursor.getInt(cursor.getColumnIndex(AGE_FIELD)));
			us.setHeightInM 			(cursor.getDouble(cursor.getColumnIndex(HEIGHT_FIELD)));
			us.setWeightInKg 			(cursor.getDouble(cursor.getColumnIndex(WEIGHT_FIELD)));
			us.setDesiredWeightInKg 	(cursor.getDouble(cursor.getColumnIndex(DESIRED_WEIGHT_FIELD)));
			us.setDailyActivity			(cursor.getString(cursor.getColumnIndex(DAILY_ACTIVITY_FIELD)));
			us.setDietStartDay 			(cursor.getString(cursor.getColumnIndex(DIET_START_DAY_FIELD)));
			us.setExpectedDietDays 		(cursor.getInt(cursor.getColumnIndex(EXPECTED_DIET_DAYS_FIELD)));
			us.setLastConfigurationTime (cursor.getString(cursor.getColumnIndex(LAST_CONFIGURATION_TIME_FIELD)));
			us.setUsualWakeUpTime		(cursor.getString(cursor.getColumnIndex(USUAL_WAKE_UP_TIME_FIELD)));
			us.setWeekendsWakeUpTime	(cursor.getString(cursor.getColumnIndex(WEEKENDS_WAKE_UP_TIME_FIELD)));
			us.setUsualSleepTime		(cursor.getString(cursor.getColumnIndex(USUAL_SLEEP_TIME_FIELD)));
			us.setWeekendsSleepTime		(cursor.getString(cursor.getColumnIndex(WEEKENDS_SLEEP_TIME_FIELD)));
			cursor.close();
			db.close();
		}
		return us;		
	}
	
	private boolean hadBeenInitialized() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + USER_SETTINGS_TABLE + 
									" WHERE " +	ID_FIELD + " = " + SETTINGS_ID,
									null);
		boolean res = (cursor.getCount()>=1);
		db.close();
		return res;
	}
	
	private void initSettings() {
		ContentValues newData = new ContentValues();
		newData.put(NAME_FIELD, 				"");
		newData.put(AGE_FIELD, 					0);
		newData.put(SEX_FIELD, 					"");
		newData.put(WEIGHT_FIELD,				0.0);
		newData.put(HEIGHT_FIELD, 				0.0);
		newData.put(DESIRED_WEIGHT_FIELD, 		0.0);
		newData.put(DAILY_ACTIVITY_FIELD, 		"");
		newData.put(DIET_START_DAY_FIELD, 		"");
		newData.put(EXPECTED_DIET_DAYS_FIELD, 	0);
		newData.put(DIET_START_DAY_FIELD, 		"");
		newData.put(USUAL_WAKE_UP_TIME_FIELD,	"");
		newData.put(WEEKENDS_WAKE_UP_TIME_FIELD,"");
		newData.put(USUAL_SLEEP_TIME_FIELD,		"");
		newData.put(WEEKENDS_SLEEP_TIME_FIELD,	"");
		
		SQLiteDatabase db = getWritableDatabase();
		db.insert(
				USER_SETTINGS_TABLE,
				null,
				newData);
		db.close();
	}

	private void saveSettings() {
		if(us.isFirstTime())
			initSettings();
		us.setFirstTime(false);
		ContentValues updateData = new ContentValues();
		updateData.put(NAME_FIELD, 					us.getUserName());
		updateData.put(AGE_FIELD, 					us.getAge());
		updateData.put(SEX_FIELD, 					us.getSex());
		updateData.put(WEIGHT_FIELD,				us.getWeightInKg());
		updateData.put(HEIGHT_FIELD, 				us.getHeightInM());
		updateData.put(DESIRED_WEIGHT_FIELD, 		us.getDesiredWeightInKg());
		updateData.put(DAILY_ACTIVITY_FIELD, 		us.getDailyActivity());
		updateData.put(DIET_START_DAY_FIELD, 		us.getDietStartDay());
		updateData.put(EXPECTED_DIET_DAYS_FIELD, 	us.getExpectedDietDays());
		updateData.put(DIET_START_DAY_FIELD, 		us.getDietStartDay());
		updateData.put(USUAL_WAKE_UP_TIME_FIELD,	us.getUsualWakeUpTime());
		updateData.put(WEEKENDS_WAKE_UP_TIME_FIELD,	us.getWeekendsWakeUpTime());
		updateData.put(USUAL_SLEEP_TIME_FIELD,		us.getUsualSleepTime());
		updateData.put(WEEKENDS_SLEEP_TIME_FIELD,	us.getWeekendsSleepTime());
		
		SQLiteDatabase db = getWritableDatabase();
		db.update(
				USER_SETTINGS_TABLE,
				updateData,
				null,
				null);
		db.close();		
	}
	
}
