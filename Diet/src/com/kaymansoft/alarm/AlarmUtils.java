package com.kaymansoft.alarm;

import java.text.ParseException;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.kaymansoft.settings.UserSettings;
import com.kaymansoft.settings.UserSettingsDBOpenHelper;
/**
 * Clase utilitria que constituye el nucleo de las alarmas en DietManager.
 * Permite obtener las alarmas segun la configurariocn de los tiempos, fijarla o cancelarlas.
 * @author Alvaro Javier
 *
 */
public class AlarmUtils {

	private static Calendar now 				= Calendar.getInstance();
	private static Calendar wakeUpTime 			= Calendar.getInstance();
	private static Calendar sleepTime 			= Calendar.getInstance();	
	private static Calendar tomorrowWakeUpTime 	= Calendar.getInstance();
	private static Calendar yesterdaySleepTime 	= Calendar.getInstance();
	
	public static final int INTERVAL_ERROR 		= -1;
	public static final int BREAKFAST 			= 0;
	public static final int MIDMORNING_SNACK 	= 1;
	public static final int LUNCH 				= 2;
	public static final int MIDAFTERNOON_SNACK 	= 3;
	public static final int DINNER 				= 4;
	public static final int MIDNIGHT_SNACK 		= 5;
	public static final int TOMORROW_BREAKFAST 	= 6;

	/**
	 * Encuentra los tiempos definidos por el usuario e inicializa las variables 
	 * {@code now, wakeUpTime, sleepTime, tomorrowWakeUpTime, yesterdaySleepTime}
	 * @param us la configuración de usuario de la BD
	 * @return true si no ocurrió ningun error al obtener los tiempos, false en caso contrario
	 */
	private static boolean setupTimes(UserSettings us) {
		now 				= Calendar.getInstance();
		wakeUpTime 			= Calendar.getInstance();
		tomorrowWakeUpTime 	= Calendar.getInstance();
		sleepTime 			= Calendar.getInstance();
		//si es fin de semana
		if(now.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || now.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			try {
				wakeUpTime.setTime(UserSettingsDBOpenHelper.TIME_FORMATTER.parse(us.getWeekendsWakeUpTime()));
				sleepTime.setTime(UserSettingsDBOpenHelper.TIME_FORMATTER.parse(us.getWeekendsSleepTime()));
				//si es domingo, la hora de despertarse mañana es la usual
				if(now.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
					tomorrowWakeUpTime.setTime(UserSettingsDBOpenHelper.TIME_FORMATTER.parse(us.getUsualWakeUpTime()));
				//para cualquier caso la hora de dormir de ayer fue la de los fines de semana pues suponemos que los
				//viernes el susuario se acuesta también tarde
			} catch (ParseException ex) {
				Log.e(AlarmUtils.class.getName(), "Error parseando el tiempo en la BD de configuración del usuario", ex);
				return false;
			}
		} else {
			try {
				wakeUpTime.setTime(UserSettingsDBOpenHelper.TIME_FORMATTER.parse(us.getUsualWakeUpTime()));
				sleepTime.setTime(UserSettingsDBOpenHelper.TIME_FORMATTER.parse(us.getUsualSleepTime()));
				//por defecto la hora de dormir de ayer y la despertarse mañana son las usuales
				yesterdaySleepTime.setTime(UserSettingsDBOpenHelper.TIME_FORMATTER.parse(us.getUsualSleepTime()));
				tomorrowWakeUpTime.setTime(UserSettingsDBOpenHelper.TIME_FORMATTER.parse(us.getUsualWakeUpTime()));
				//si hoy es viernes, la hora de despertarse mañana es la de los fines de semana 
				if(now.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
					tomorrowWakeUpTime.setTime(UserSettingsDBOpenHelper.TIME_FORMATTER.parse(us.getWeekendsWakeUpTime()));
				//si hoy es lunes, la hora de dormir de ayer es la de los fines de semana 
				if(now.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) 
					yesterdaySleepTime.setTime(UserSettingsDBOpenHelper.TIME_FORMATTER.parse(us.getWeekendsSleepTime()));
			} catch (ParseException ex) {
				Log.e(AlarmUtils.class.getName(), "Error parseando el tiempo en la BD de configuración del usuario", ex);
				return false;
			}			
		}
		//ajustar los demás campos

		wakeUpTime.set(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));

		tomorrowWakeUpTime.set(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));
		tomorrowWakeUpTime.add(Calendar.DAY_OF_YEAR, 1);

		sleepTime.set(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));
		//corregir la fecha en caso que se acueste despues de las 12:00 pm
		if(sleepTime.get(Calendar.AM_PM) == Calendar.AM)
			sleepTime.add(Calendar.DAY_OF_YEAR, 1);

		return true; //todo bien		
	}

	/**
	 * Devuelve el intervalo de de tiempo en el que está el momento dado
	 * @param us Configración del usuario en la BD
	 * @param moment Momento del cual se quiere saber el intervalo
	 * @return el intervalo del día para el momento especificado:
	 * <ol start=-1>
	 * <li> <b>Error</b></li>
	 * <li> Desayuno </li>
	 * <li> Merienda de media mañana </li>
	 * <li> Almuerzo </li>
	 * <li> Merienda de la tarde </li>
	 * <li> Cena </li>
	 * <li> Meriende de media noche </li>
	 * <li> Desayuno de mañana </li>
	 * </ol>
	 * 
	 */
	public static int getTimeIntervalFor(UserSettings us, Calendar moment) {
		//utilitario para buscar el intervalo
		Calendar util = Calendar.getInstance();
		//poner util a la hora de levantarse
		try {
			util.setTime(
					//si es fin de semana
					moment.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || moment.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ?
							//poner el horario del fin de semana
							UserSettingsDBOpenHelper.TIME_FORMATTER.parse(us.getWeekendsWakeUpTime()) :
							//si no poner el normal
							UserSettingsDBOpenHelper.TIME_FORMATTER.parse(us.getUsualWakeUpTime())
					);
		} catch (ParseException e) {
			Log.d(AlarmUtils.class.getName(),"Error parseando los timepos de la BD de user settings");
			return INTERVAL_ERROR;
		}
		//ahora poner el día, específico
		util.set(moment.get(Calendar.YEAR), moment.get(Calendar.MONTH), moment.get(Calendar.DAY_OF_MONTH));
		
		//verificar si es antes de levantarse
		if(moment.before(util))
			return BREAKFAST;
		
		int interval = BREAKFAST;//desayuno
		//mientras el momento sea despues del horario de util y el intervalo sea antes del desayuno de mañana
		while(moment.after(util) && interval<TOMORROW_BREAKFAST) { 
			util.add(Calendar.HOUR_OF_DAY, 3);//sumar 3 horas
			interval++;//aumentar el intervalo
		}
		//poner util a la hora de acostarse
		try {
			util.setTime(
					//si es fin de semana
					moment.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || moment.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ?
							//poner el horario del fin de semana
							UserSettingsDBOpenHelper.TIME_FORMATTER.parse(us.getWeekendsSleepTime()) :
							//si no poner el normal
							UserSettingsDBOpenHelper.TIME_FORMATTER.parse(us.getUsualSleepTime())
					);
		} catch (ParseException e) {
			Log.d(AlarmUtils.class.getName(),"Error parseando los tiempos de la BD de user settings");
			return INTERVAL_ERROR;
		}
		//ahora poner el día específico
		util.set(moment.get(Calendar.YEAR), moment.get(Calendar.MONTH), moment.get(Calendar.DAY_OF_MONTH));
		//corregir un error comun en los horarios (12pm hora de acostarse en realidad es 12am, nadie se acuesta a las 12 del día)
		if(util.get(Calendar.HOUR) == 12 && util.get(Calendar.AM_PM) == Calendar.PM)
			util.set(Calendar.AM_PM, Calendar.AM);
		//verficar si la hora de acostarse es al otro día
		if(util.get(Calendar.AM_PM) == Calendar.AM)
			util.add(Calendar.DAY_OF_YEAR, 1);
		//verificar si es después de acostarse
		if(moment.after(util))
			return TOMORROW_BREAKFAST;
		//ok, retornar entonces
		return interval;
	}

	/**
	 * Devuelve el horario de la próxima alarma dada la configuración del usuario de la BD
	 * @param us configuracion del usuario de la BD
	 * @return el horario de la proxima alarma
	 */
	public static Calendar getNextAlarmTime(UserSettings us) {
		if(!setupTimes(us))
			return null;
		//utilitario para calcular la hora de la alarma
		Calendar tmpCal = (Calendar) wakeUpTime.clone();
		//buscamos el intervalo del día en el que estamos
		int interval = getTimeIntervalFor(us, now);
		if(interval == INTERVAL_ERROR)
			return null;//hubo algún error		
		//si es un intervalo de hoy 
		if(BREAKFAST <= interval && interval <= MIDNIGHT_SNACK) {
			tmpCal.add(Calendar.HOUR_OF_DAY, interval*3);//sumar las horas necesarias (3 por intervalo)
			return tmpCal;
		}
		else //interval==TOMORROW_BREAKFAST  quiere decir que será mañana
			return (Calendar) tomorrowWakeUpTime.clone();
	}

	/**
	 * Devuelve la hora de levantarse de hoy
	 * @param us configuración del usuario en la BD
	 * @return la hora de leventarse hoy
	 */
	public static Calendar getWakeUpTime(UserSettings us) {
		setupTimes(us);
		return wakeUpTime;
	}

	/**
	 * Devuelve la hora de acostarse de hoy
	 * @param us configuración del usuario en la BD
	 * @return la hora de acostarse hoy
	 */
	public static Calendar getSleepTime(UserSettings us) {
		setupTimes(us);
		return sleepTime;
	}

	/**
	 * Verifica si el usuario está usando las alarmas
	 * @param ctx contexto necesario para la verificación
	 * @return true si está usando alarmas, fasle en caso contrario
	 */
	public static boolean isUsingAlarms(Context ctx) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		return prefs.getBoolean("alarm_check", true);
	}

	/**
	 * Verifica si el usuario está usando alarmas con vibrador
	 * @param ctx contexto necesario para la verificación
	 * @return true si está usando vibración, fasle en caso contrario
	 */
	public static boolean isUsingAlarmVibration(Context ctx) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		return isUsingAlarms(ctx) && prefs.getBoolean("vibration_check", true);
	}

	/**
	 * Verifica si el usuario está usando alarmas con sonido
	 * @param ctx contexto necesario para la verificación
	 * @return true si está usando sonido en las alarmas, fasle en caso contrario
	 */
	public static boolean isUsingAlarmSound(Context ctx) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		return isUsingAlarms(ctx) && prefs.getBoolean("sound_check", true);
	}

	/**
	 * Fija la próxima alarma para comer
	 * @param ctx contexto neceario para fijar la alarma
	 * @param us configuración del usuario en la BD
	 */
	public static void setupNextAlarm(Context ctx, UserSettings us) {
		Calendar nextAlarmTime = getNextAlarmTime(us);
		if(nextAlarmTime==null) {
			Log.e(AlarmUtils.class.getName(),"No se pudo obtner el horario de la próxima alarma");
			return;
		}
		Log.d(AlarmUtils.class.getName(), "fijando la nueva alarma: " + nextAlarmTime.toString());
		AlarmManager mgr = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
		mgr.set(
				AlarmManager.RTC_WAKEUP,
				nextAlarmTime.getTimeInMillis(),
				PendingIntent.getBroadcast(ctx, 0, new Intent(ctx, AlarmHandler.class), PendingIntent.FLAG_CANCEL_CURRENT)
				);
	}	

	/**
	 * Cancela la próxima alarma de comida
	 * @param ctx contexto neceario para fijar la alarma
	 * @param us configuración del usuario en la BD
	 */
	public static void cancelNextAlarm(Context ctx, UserSettings us) {
		Calendar nextAlarmTime = getNextAlarmTime(us);
		if(nextAlarmTime == null) {
			Log.e(AlarmUtils.class.getName(),"no se pudo obtener el horario de la próxima alarma");
		} else {
			Log.d(AlarmUtils.class.getName(), "cancelando la próxima alarma: " + nextAlarmTime.toString());
		}
		AlarmManager mgr = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
		mgr.cancel(PendingIntent.getBroadcast(ctx, 0, new Intent(ctx, AlarmHandler.class), PendingIntent.FLAG_CANCEL_CURRENT));		
	}

	/**
	 * Fija una alarma de recuperación para si se funde algo con las alarmas
	 * @param context contexto neceario para fijar la alarma
	 */
	public static void setupAlarmFixer(Context context) {
		Log.d(AlarmHandler.class.getName(),
				"Fijar una alarma que se repita todos los días para recuperar alas alarmas en caso de error");
		AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		mgr.setRepeating(
				AlarmManager.RTC_WAKEUP, 
				Calendar.getInstance().getTimeInMillis() + AlarmManager.INTERVAL_DAY, 
				AlarmManager.INTERVAL_DAY,
				PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmBoot.class), 
						PendingIntent.FLAG_CANCEL_CURRENT));
	} 
}
