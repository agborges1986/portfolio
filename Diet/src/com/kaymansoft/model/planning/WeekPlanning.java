package com.kaymansoft.model.planning;

import java.util.Calendar;
import java.util.HashMap;

import com.kaymansoft.alarm.AlarmUtils;
import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.elements.Menu;
import com.kaymansoft.model.planning.DayPlanning.MealType;
import com.kaymansoft.model.planning.PlanningUtils.WeekType;
import com.kaymansoft.settings.UserSettings;

public class WeekPlanning {

	private WeekType week;

	private HashMap<Integer, DayPlanning> days_planning;
	private Calendar firstDay, lastDay;

	/**
	 * Crea una planificaci�n semanal dado un d�a cualquiera de la semana
	 * @param oneDayInTheWeek Un d�a cualquiera que pertenezca a la semana que se desea planificar
	 */
	WeekPlanning(Calendar oneDayInTheWeek) {
		Calendar util = (Calendar) oneDayInTheWeek.clone();
		while(util.get(Calendar.DAY_OF_WEEK) != util.getFirstDayOfWeek())
			util.roll(Calendar.DAY_OF_WEEK, false);

		this.firstDay = (Calendar) util.clone();
		this.firstDay.set(Calendar.SECOND,1);
		this.firstDay.set(Calendar.MINUTE,0);
		this.firstDay.set(Calendar.HOUR_OF_DAY,0);		

		lastDay = (Calendar) this.firstDay.clone();
		lastDay.add(Calendar.DAY_OF_WEEK,6);				

		//inicializar el contenedor de los d�as planificados
		days_planning = new HashMap<Integer, DayPlanning>();
		//agregar el primer d�a y los 6 siguientes
		int i=0;
		do {
			days_planning.put(util.get(Calendar.DAY_OF_WEEK),new DayPlanning(util));
			util.roll(Calendar.DAY_OF_WEEK, true);
		} while(++i < 7);//cuando i=6 no vuelve a entrar
	}

	public Calendar getFirstDay() {
		return (Calendar) firstDay.clone();
	}

	public Calendar getLastDay() {
		return (Calendar) lastDay.clone();
	}

	public DayPlanning getDayPlanning(int dayOfWeek) {
		if(	dayOfWeek == Calendar.MONDAY ||
				dayOfWeek == Calendar.TUESDAY ||
				dayOfWeek == Calendar.WEDNESDAY ||
				dayOfWeek == Calendar.THURSDAY ||
				dayOfWeek == Calendar.FRIDAY ||
				dayOfWeek == Calendar.SUNDAY ||
				dayOfWeek == Calendar.SATURDAY) {
			return days_planning.get(dayOfWeek);			
		} 
		return null;
	}

	/**
	 * Salva la configuraci�n de esta semana en la BD
	 * @param db BD de la aplicaci�n
	 */
	void save(AppDBOpenHelper db) {
		getDayPlanning(Calendar.MONDAY).save(db);
		getDayPlanning(Calendar.TUESDAY).save(db);
		getDayPlanning(Calendar.WEDNESDAY).save(db);
		getDayPlanning(Calendar.THURSDAY).save(db);
		getDayPlanning(Calendar.FRIDAY).save(db);
		getDayPlanning(Calendar.SATURDAY).save(db);
		getDayPlanning(Calendar.SUNDAY).save(db);		
	}

	void setWeek(WeekType week) {
		this.week = week;
	}

	public WeekType getWeekType() {
		return week;
	}

	public Menu getMenuPlanningFor(UserSettings us ,Calendar moment) {
		int interval = AlarmUtils.getTimeIntervalFor(us, moment);
		//primer d�a de la pr�xima semana
		Calendar lastPlus1 = getLastDay();
		lastPlus1.add(Calendar.DAY_OF_YEAR,1);

		DayPlanning dp;
		//para las semanas actual y pr�xima
		if(getWeekType() != WeekType.Default) {
			//si es un d�a de esta semana
			if(moment.after(getFirstDay()) && moment.before(lastPlus1)) {
				//si es un intervalo en el d�a de moment
				if(interval >= AlarmUtils.BREAKFAST && interval <= AlarmUtils.MIDNIGHT_SNACK) {
					dp = getDayPlanning(moment.get(Calendar.DAY_OF_WEEK));
					return dp.getMenuForMeal(MealType.values()[interval]);
				} else {
					//si es al otro d�a y el otro d�a cae en esta semana
					if(interval == AlarmUtils.TOMORROW_BREAKFAST && moment.before(getLastDay())) {
						dp = getDayPlanning(moment.get(Calendar.DAY_OF_WEEK)+1);
						return dp.getMenuForMeal (MealType.values()[0]);
					}
				}
			}
		} else { //si es la semana por defecto
			//si es un intervalo en el d�a de moment
			if(interval >= AlarmUtils.BREAKFAST && interval <= AlarmUtils.MIDNIGHT_SNACK) {
				dp = getDayPlanning(moment.get(Calendar.DAY_OF_WEEK));
				return dp.getMenuForMeal(MealType.values()[interval]);
			} else {
				//si es al otro d�a
				if(interval == AlarmUtils.TOMORROW_BREAKFAST) {
					int dayOfWeek = (moment.get(Calendar.DAY_OF_WEEK)+1)%7+1;
					dp = getDayPlanning(dayOfWeek);
					return dp.getMenuForMeal(MealType.values()[0]);
				}
			}
		}
		//si no retornar nulo
		return null;
	}

}
