package com.kaymansoft.gui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.kaymansoft.R;
import com.kaymansoft.calories.BMIUtils;
import com.kaymansoft.calories.BMIUtils.BMIClassification;
import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.cursors.WeightCursor;
import com.kaymansoft.settings.UserSettings;
import com.kaymansoft.settings.UserSettingsDBOpenHelper;

public class UserResumeActivity extends Activity {

	TextView					age, sex, height, weight, desiredWeight, prevWeight, estimatedWeight, dailyActivity, currentBMI;
	TextView					usualWakeUpTime, usualSleepTime, weekendsWakeUpTime, weekendsSleepTime;
	String						sexValues[], dailyActivityValues[], sexTexts[], dailyActivityTexts[];
	Button						changeInfo, changeTimes;

	private static final int	EDIT_INFO	= 1029;
	private static final int	EDIT_TIMES	= 1019;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.user_resume);

		cacheViews();
		initComponents();

	}

	private void cacheViews() {
		age = (TextView) findViewById(R.id.textView2);
		sex = (TextView) findViewById(R.id.textView4);
		height = (TextView) findViewById(R.id.textView6);
		weight = (TextView) findViewById(R.id.textView8);
		desiredWeight = (TextView) findViewById(R.id.textView10);
		prevWeight = (TextView) findViewById(R.id.textView12);
		//estimatedWeight = (TextView) findViewById(R.id.textView14);
		dailyActivity = (TextView) findViewById(R.id.textView16);
		currentBMI = (TextView) findViewById(R.id.textView28);

		usualWakeUpTime = (TextView) findViewById(R.id.textView20);
		usualSleepTime = (TextView) findViewById(R.id.textView24);
		weekendsWakeUpTime = (TextView) findViewById(R.id.textView22);
		weekendsSleepTime = (TextView) findViewById(R.id.textView26);

		changeInfo = (Button) findViewById(R.id.button1);
		changeTimes = (Button) findViewById(R.id.button2);
	}

	private void initComponents() {

		Resources res = getResources();

		sexTexts = res.getStringArray(R.array.sexs_list);
		sexValues = res.getStringArray(R.array.sex_values_list);

		dailyActivityTexts = res.getStringArray(R.array.daily_activity_list);
		dailyActivityValues = res.getStringArray(R.array.daily_activity_values_list);

		showData();

		changeInfo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(UserResumeActivity.this, PersonalInformationActivity.class);
				startActivityForResult(intent, EDIT_INFO);
			}
		});

		changeTimes.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(UserResumeActivity.this, TimesForDietActivity.class);
				startActivityForResult(intent, EDIT_TIMES);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		showData();
		super.onActivityResult(requestCode, resultCode, data);
	}

	private String getSexText(String sexValue) {
		return sexValue.equals(sexValues[0]) ? sexTexts[0] : sexTexts[1];
	}

	private String getDailyActivityText(String dailyActivityValue) {
		for (int i = 0; i < dailyActivityValues.length; i++)
			if (dailyActivityValues[i].equals(dailyActivityValue))
				return dailyActivityTexts[i];
		return dailyActivityTexts[0];
	}

	private void showData() {
		NumberFormat formatter = new DecimalFormat("0.0");
		SimpleDateFormat inTimeFormatter = UserSettingsDBOpenHelper.TIME_FORMATTER;
		SimpleDateFormat outTimeFormatter = new SimpleDateFormat(" h:mm a");

		UserSettings us = new UserSettingsDBOpenHelper(this).getSettings();
		age.setText(String.valueOf(us.getAge()) + " " + getResources().getString(R.string.years_text));
		sex.setText(getSexText(us.getSex()));
		height.setText(formatter.format(us.getHeightInM() * 3.28) + " ft");
		weight.setText(formatter.format(us.getWeightInKg() * 2.205) + " lb");
		desiredWeight.setText(formatter.format(us.getDesiredWeightInKg() * 2.205) + " lb");
		prevWeight.setText("---! buscar");
		//estimatedWeight.setText("----! calcular");
		dailyActivity.setText(getDailyActivityText(us.getDailyActivity()));
		Enum<BMIClassification> bmiClass = BMIUtils.getActualBMIClassification(us);
		currentBMI.setText(getResources().getStringArray(R.array.bmi_class_text)[bmiClass.ordinal()]);

		try {
			usualWakeUpTime.setText(outTimeFormatter.format(inTimeFormatter.parse(us.getUsualWakeUpTime())));
			weekendsWakeUpTime.setText(outTimeFormatter.format(inTimeFormatter.parse(us.getWeekendsWakeUpTime())));
			usualSleepTime.setText(outTimeFormatter.format(inTimeFormatter.parse(us.getUsualSleepTime())));
			weekendsSleepTime.setText(outTimeFormatter.format(inTimeFormatter.parse(us.getWeekendsSleepTime())));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		AppDBOpenHelper db = new AppDBOpenHelper(this);
		WeightCursor wc = db.getWeights();
		if (wc.getCount() > 1) {
			wc.moveToPosition(wc.getCount() - 2);
			prevWeight.setText(formatter.format(wc.getWeight().getWeightValue() * 2.205) + " lb");
		} else {
			prevWeight.setText(formatter.format(us.getWeightInKg() * 2.205) + " lb");
		}
		wc.close();
		db.close();

	}

}
