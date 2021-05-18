package com.kaymansoft.model.planning;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.kaymansoft.model.cursors.MenuCursor;
import com.kaymansoft.model.elements.Menu;
import com.kaymansoft.model.planning.DayPlanning.MealType;

import android.database.Cursor;
import android.util.Log;

import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.settings.UserSettings;

import static com.kaymansoft.model.MCons.*;

public class PlanningUtils {
	
	//minutos de desplazaminto para los horarios a la hora de determinar la comida de una hora dada
	private static final int TIME_BIAS_FOR_MEALS = 45;  
	
	public enum WeekType { Actual, Next, Default};
	
	private static WeekPlanning currentWeekPlanning;
	private static WeekPlanning nextWeekPlanning;
	private static WeekPlanning defaultWeekPlanning;
	
	public static void load(AppDBOpenHelper db) {
		Calendar today = Calendar.getInstance();
		currentWeekPlanning = loadWeek(today, db);
		currentWeekPlanning.setWeek(WeekType.Actual);
		
		Calendar in7days = (Calendar) today.clone();
		in7days.add(Calendar.DAY_OF_YEAR,7);
		nextWeekPlanning = loadWeek(in7days, db);
		nextWeekPlanning.setWeek(WeekType.Next);
		
		Calendar defaultWeekOneDay = Calendar.getInstance();
		//esto es un día arbitrario que no tiene sentido para una planificación real
		defaultWeekOneDay.set(1975, 4, 11);//creo que es domingo? :P
		defaultWeekPlanning = loadWeek(defaultWeekOneDay, db);
		defaultWeekPlanning.setWeek(WeekType.Default);
	}
	
	/**
	 * Salva la planificación de los menús en la BD
	 * @param db BD de la aplicación
	 */
	public static void save(AppDBOpenHelper db) {
		if(currentWeekPlanning != null)
			currentWeekPlanning.save(db);
		if(nextWeekPlanning != null)
			nextWeekPlanning.save(db);
		if(defaultWeekPlanning != null)
			defaultWeekPlanning.save(db);
	}
	
	private static WeekPlanning loadWeek(Calendar oneDayOfWeek, AppDBOpenHelper db) {
		SimpleDateFormat DF = DATE_FORMATTER;
		WeekPlanning wp = new WeekPlanning(oneDayOfWeek);
		//otener de la BD las planificaciones
		Cursor c = db.getPlannings(wp.getFirstDay(),wp.getLastDay());
		//c.moveToFirst();
		while(c.moveToNext()) {
			Calendar day = Calendar.getInstance();
			try {
				//obtener el día de la planificación
				day.setTime(DF.parse(c.getString(c.getColumnIndex(PLANNING_DATE_FIELD))));
				DayPlanning dp = wp.getDayPlanning(day.get(Calendar.DAY_OF_WEEK));
				int meal_number = c.getInt(c.getColumnIndex(PLANNING_MEAL_NUMBER_FIELD));
				long menu_id = c.getLong(c.getColumnIndex(PLANNING_MENU_ID_FIELD));
				MenuCursor menuCursor = db.getMenuById(menu_id);
				dp.setMenuForMeal(MealType.values()[meal_number], menuCursor.getCount()==1? menuCursor.getMenu() : null);
			} catch (ParseException e) {
				Log.e(PlanningUtils.class.getName(), "Error parseando la fecha de la BD");
			}			
		}
		return wp;
	}
	
	public static WeekPlanning getCurrentWeekPlanning() {
		return currentWeekPlanning;		
	}
	
	public static WeekPlanning getNextWeekPlanning() {
		return nextWeekPlanning;		
	}

	public static WeekPlanning getDefaultWeekPlanning() {
		return defaultWeekPlanning;
	}

	public static Menu getMenuPlanningFor(UserSettings us,Calendar moment) {
		Calendar biased_moment =  (Calendar) moment.clone();
		biased_moment.add(Calendar.MINUTE, -TIME_BIAS_FOR_MEALS);
		Menu menu = getCurrentWeekPlanning().getMenuPlanningFor(us,biased_moment);
		if(menu == null) // si se devolvió null, probar con la próxima semana
			menu = getNextWeekPlanning().getMenuPlanningFor(us,biased_moment);
		if(menu == null) //probar por último en la planificación por defecto
			menu = getDefaultWeekPlanning().getMenuPlanningFor(us, biased_moment);
		return menu;		
	}
}
