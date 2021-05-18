package com.kaymansoft.alarm;

import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kaymansoft.R;
import com.kaymansoft.gui.AlarmWindowActivity;
import com.kaymansoft.gui.TakeMealActivity;
import com.kaymansoft.settings.UserSettings;
import com.kaymansoft.settings.UserSettingsDBOpenHelper;

/**
 * Esta clase recibe los disparos de las alarmas y es al encargada de fijar la
 * próxima en caso de que el usuario las esté utilizando
 * 
 * @author Alvaro Javier
 * 
 */
public class AlarmHandler extends BroadcastReceiver {

	@SuppressWarnings("static-access")
	@Override
	public void onReceive(Context context, Intent intent) {

		Log.d(AlarmHandler.class.getName(), "Recibida una alarma");
		if (AlarmUtils.isUsingAlarms(context)) {
			UserSettings us = new UserSettingsDBOpenHelper(context).getSettings();
			AlarmUtils.setupNextAlarm(context, us);
			Calendar nextAlarmTime = AlarmUtils.getNextAlarmTime(us);
			if (nextAlarmTime == null) {
				Log.e(AlarmHandler.class.getName(), "No se pudo obtener el tiempo de la próxima alarma");
				return;
			}
			Log.d(AlarmHandler.class.getName(), "Se fijó una nueva alarma con tiempo: " + nextAlarmTime.toString());
		}

		//mostrar una notificación para el BB
		NotificationManager mgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		mgr.cancel(AlarmWindowActivity.MEAL_TIME_NOTIFICATION_ID);

		//datos para la notificación
		int icon = R.drawable.notification_weight_icon;
		CharSequence tickerText = context.getString(R.string.meal_time_text);
		long when = System.currentTimeMillis();
		CharSequence contentTitle = context.getString(R.string.meal_time_text);
		CharSequence contentText = context.getString(R.string.meal_time_secundary_text);

		Intent notificationIntent = new Intent(context, TakeMealActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		Notification mealTime = new Notification(icon, tickerText, when);
		mealTime.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

		mgr.notify(AlarmWindowActivity.MEAL_TIME_NOTIFICATION_ID, mealTime);

		//creando el intento
		Intent showAlarm = new Intent(context, AlarmWindowActivity.class);
		showAlarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(showAlarm);
	}

}
