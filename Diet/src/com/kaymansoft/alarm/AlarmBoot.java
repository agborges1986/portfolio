package com.kaymansoft.alarm;

import com.kaymansoft.settings.UserSettingsDBOpenHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Esta clase es s�lo para recuperar las alarmas en caso de que se apague el tel�fono
 * 
 * @author Alvaro Javier
 *
 */
public class AlarmBoot extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		//fijar la alarma de recuepraricon (diaria)
		Log.d(AlarmBoot.class.getName(), "Nueva alarma de recuperacion diaria");
		AlarmUtils.setupAlarmFixer(context);
		//recuerar la proxima alarma
		Log.d(AlarmBoot.class.getName(), "Se recuperar�n las alarmas");
		UserSettingsDBOpenHelper db = new UserSettingsDBOpenHelper(context);
		AlarmUtils.setupNextAlarm(context, db.getSettings()); //fijar la pr�xima alarma
		AlarmUtils.setupAlarmFixer(context);//fijar el "arreglador" de alarmas
		db.close();
	}

}
