package com.kaymansoft.model.planning;

import java.util.Calendar;
import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.elements.Menu;

public class DayPlanning {

	private static final int ACTION_NONE 	= 0; 
	private static final int ACTION_NEW 	= 1; 
	private static final int ACTION_UPDATE 	= 2; 
	private static final int ACTION_DELETE 	= 3; 

	private Calendar day;
	public enum MealType { Breakfast, MidMorningSnack, Lunch, MidAfternoonSnack, Dinner, MidNightSnack };
	private Menu[] menus = new Menu[6];
	private int[] actions = new int[6];

	DayPlanning(Calendar day) {
		this.day = (Calendar) day.clone();
		this.day.set(Calendar.HOUR_OF_DAY, 0);
		this.day.set(Calendar.MINUTE, 0);
		this.day.set(Calendar.SECOND, 1);
		menus = new Menu[MealType.values().length];
		actions = new int[6];
		for(int i=0;i<6;i++)
			actions[i] = ACTION_NONE;
	}

	/**
	 * Fija el menú planificado para el día modelado por este objeto
	 * @param meal Comida del día para la cuals e planificará el menú
	 * @param menuId Id del menú a planificar
	 */
	public void setMenuForMeal(MealType meal, Menu menu) {
		int ordinal = meal.ordinal();
		if(menu != null) { //el menu NO ES NULL
			switch (actions[ordinal]) {
			case ACTION_NONE://no se ha realizado accion todavia
				if(menus[ordinal] == null)
					actions[ordinal] = ACTION_NEW;
				else
					actions[ordinal] = ACTION_UPDATE;
				break;
			case ACTION_NEW: //la accion anterior era new
				actions[ordinal] = ACTION_NEW;
			case ACTION_UPDATE: //la accion anterior era update
				actions[ordinal] = ACTION_UPDATE;
			case ACTION_DELETE: //la accion anterior era borrar
				actions[ordinal] = ACTION_UPDATE;
				//esto nunca deberia pasar porque luego de una accion DELETE el menu es null
			default:
				break;
			}
		} else {//el menu ES NULL
			switch (actions[ordinal]) {
			case ACTION_NONE: //no se ha realizado accion todavia
				if(menus[ordinal] == null)
					actions[ordinal] = ACTION_NONE;
					//nunca se deberia llegar a aqui poruqe si no hay menu no se deberia poder editar
				else
					actions[ordinal] = ACTION_DELETE;
				break;
			case ACTION_NEW: //la accion anterior era new
				actions[ordinal] = ACTION_NONE;
			case ACTION_UPDATE: //la accion anterior era update
				actions[ordinal] = ACTION_DELETE;
			default:
				break;
			}
		}

		menus[ordinal] = menu;
	}

	/**
	 * Devuelve el menú planificado para una comida específica
	 * @param meal comida sobre la cual se queire saber el menu
	 * @return el menu de la comida especificada
	 */
	public Menu getMenuForMeal(MealType meal) {
		return menus[meal.ordinal()];
	}

	/**
	 * Devuelve el día de la semana según <b>java.util.Calendar</b>
	 * @return el día de la semana
	 * @see Calendar
	 */
	public int getWeekDay() {
		return day.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Devuelve el día específico marcado por esta planificación 
	 * @return el día marcado por esta planificación (tipo Calendar)
	 */
	public Calendar getDay() {
		return (Calendar)day.clone();
	}

	/**
	 * Salva la configuración de este día en la BD
	 * @param db BD de la aplicación
	 */
	void save(AppDBOpenHelper db) {
		for(int i=0;i<6;i++) {
			long menuId = menus[i]!=null ? menus[i].getId() : -1;			
			switch (actions[i]) {
			case ACTION_NEW:
				if(menuId != -1)
					db.addPlanning(day, i, menuId);
				break;
			case ACTION_UPDATE:
				if(menuId != -1)
					db.updatePlanning(day, i, menuId);
				break;
			case ACTION_DELETE:
				db.deletePlanning(day, i);
				break;
			default://ACTION_NONE
				break;
			}
		}
	}

}
