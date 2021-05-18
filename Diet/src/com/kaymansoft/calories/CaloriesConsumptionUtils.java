package com.kaymansoft.calories;

import java.text.ParseException;
import java.util.Calendar;

import android.content.Context;

import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.cursors.FoodWithQuantityCursor;
import com.kaymansoft.model.cursors.MealCursor;
import com.kaymansoft.model.elements.Food;
import com.kaymansoft.model.elements.Meal;
import com.kaymansoft.settings.UserSettings;
import com.kaymansoft.settings.UserSettingsDBOpenHelper;

public class CaloriesConsumptionUtils {
	
	/**
	 * Devuelve el consumo actual de calorías basado en el peso actual del usuario
	 * @param us configuración del usuario en la BD
	 * @return el consumo diario de calorías actual del usuario
	 */
	public static double getActualCaloriesConsumption(UserSettings us) {
		return getHBCaloriesConsumption(BMRUtils.getActualBMR(us),us.getDailyActivity());		
	}
	
	/**
	 * Devuelve el consumo deseado de calorías basado en el peso deseado por el usuario
	 * @param us configuración del usuario en la BD
	 * @return el consumo diario de calorías para el peso deseado por el usuario
	 */
	public static double getCaloriesConsumptionForDesiredWeight(UserSettings us) {
		return getHBCaloriesConsumption(BMRUtils.getDesiredBMR(us),us.getDailyActivity());		
	}
	
	/**
	 * Devuelve el consumo de calorías de un humano dado su BMR y la cantidad de actividad física diaria
	 * que realiza.
	 * La fórmula usada para el cálculo está relacionada con la actividad física diaria del usuario
	 * de la siguiente forma (fórmula de Harris-Benedict):
	 * <ul>
	 * <li>Sedentario: BMR*1.2</li>
	 * <li>Ligeramente Activo: BMR*1.375</li>
	 * <li>Activo: BMR*1.55</li>
	 * <li>Muy Activo: BMR*1.1725</li>
	 * </ul>
	 * @param bmr BMR base para el cálculo
	 * @param dailyActivity actividad diaria (uno de los valores S,LA,A,VA)
	 * @return
	 */
	private static double getHBCaloriesConsumption(double bmr,String dailyActivity) {
		double hbFactor = 1.55; //por defecto asumimos ligeramente activo
		//sedentario
		if(dailyActivity.equalsIgnoreCase("S")) {
			hbFactor = 1.2;
		}
		//ligeramente activo
		if(dailyActivity.equalsIgnoreCase("LA")) {
			hbFactor = 1.375;
		}		
		//activo
		if(dailyActivity.equalsIgnoreCase("A")) {
			hbFactor = 1.55;
		}
		//muy activo
		if(dailyActivity.equalsIgnoreCase("VA")) {
			hbFactor = 1.725;
		}
		return bmr*hbFactor;
	}
	
	/**
	 * Diferencia de calorías entre el consumo actual y el ideal para el peso deseado por el susuario
	 * @param us configuración del usuario en la BD
	 * @return la diferencia consumoDeseado-consumoActual
	 */
	private static double getCaloriesDifference(UserSettings us) {
		return getCaloriesConsumptionForDesiredWeight(us) - getActualCaloriesConsumption(us); 
	}
	
	/**
	 * Devuelve el consumo de calorías recomendado para hoy teniendo en cuenta el día de inicio de la dieta,
	 * el peso actual del usuario y el peso deseado
	 * @param us configuración del usuario en la BD
	 * @return el consumo de caloria recomendado para hoy
	 */
	public static double getRecommendedDailyCaloriesConsumption(UserSettings us) {
		return getRecommendedDailyCaloriesConsumption(us,Calendar.getInstance());		
	}
	
	/**
	 * Devuelve el consumo de calorías recomendado para daterminado día teniendo en cuenta el día de inicio de la dieta,
	 * el peso actual del usuario y el peso deseado
	 * @param us configuración del usuario en la BD
	 * @param day el día específico para el cual se desea obtener el consumo de calorías
	 * @return el consumo de calorías recomendado para el día determinado por <code>day</coe>
	 */
	public static double getRecommendedDailyCaloriesConsumption(UserSettings us, Calendar day) {
		//TODO me parece que se debe segmentar más el cambio de la dieta, con cuatro pasos podría haber un cambio brusco 
		Calendar rollingDate = Calendar.getInstance();
		try {			
			//prefijar en el dia que comenzo la dieta
			rollingDate.setTime(UserSettingsDBOpenHelper.DATE_FORMATTER.parse(us.getDietStartDay()));			
		} catch (ParseException e) {
			e.printStackTrace();
			return -1; //error
		}
		//diferecia entra las calorias consumidas actualmente y las que se deben consumir
		double diffCalories = getCaloriesDifference(us);
		
		//aumentar 5 dias
		rollingDate.add(Calendar.DAY_OF_MONTH, 5);
		//si hoy es uno de los primeros 5 días de dieta
		if(day.compareTo(rollingDate)<1) {
			diffCalories = diffCalories/4.0; //*1/4
		}
		//aumentar 5 dias
		rollingDate.add(Calendar.DAY_OF_MONTH, 5);
		//si hoy es uno de los 5-10 días de dieta
		if(day.compareTo(rollingDate)<1) {
			diffCalories = diffCalories/2.0; //*2/4
		}
		//aumentar 5 dias
		rollingDate.add(Calendar.DAY_OF_MONTH, 5);
		//si hoy es uno de los 10-15 días de dieta
		if(day.compareTo(rollingDate)<1) {
			diffCalories = 3.0*diffCalories/4.0; //*3/4
		}
		//si hace más de 15 días que se comenzó la dieta se debe consumir las colrias recomendadas		
		return getActualCaloriesConsumption(us) + diffCalories;		
	}

	/**
	 * Devuelve el consumo de calorías de hoy
	 * @param ctx contexto necesario para acceder a la BD
	 * @param us configuraicon del usuario en la BD
	 * @return la cantidad de calorías consumidas por el usuario hoy
	 */
	public static double getTodayConsumedCalories(Context ctx, UserSettings us) {
		//calcular el consumo de hoy
		AppDBOpenHelper db = new AppDBOpenHelper(ctx);
		MealCursor meals = db.getMealByDate(Calendar.getInstance());
		meals.moveToFirst();
		double calories = 0.0;
		while(!meals.isAfterLast()) {
			Meal meal = meals.getMeal();
			FoodWithQuantityCursor foods = db.getFoodsWithQuantityByMealId(meal.getId());
			if(foods.getCount() > 0) {
				foods.moveToFirst();
				do {
					Food food = foods.getFood();
					double quantity = food.getQuantity();
					quantity = quantity > 0.0 ? quantity : 0.0; 
					calories += food.getCalories() * quantity;
				} while(foods.moveToNext());
			}
			foods.close();
			meals.moveToNext();
		}
		meals.close();		
		db.close();
		return calories;
	}
	
	public static double[] getConsumedCaloriesLastDays(Context ctx, int lastdays) {
		//calcular el consumo calor'ias en los ultimos dias
		// calories es un arreglo que almacena las cantidades de calorias consumidas
		// calories[0]= calorias consumidas hoy
		//calories[lastdays-1]=calorias consumidas hace "lastdays" dias
		
		AppDBOpenHelper db = new AppDBOpenHelper(ctx);
		
//		PARA APLICAR SI HAY REINICIO DE LA DIETA
		UserSettingsDBOpenHelper usDB = new UserSettingsDBOpenHelper(ctx);
		final UserSettings us = usDB.getSettings();
		
//		Inicio de la dieta
		final String start=us.getDietStartDay();
		Calendar startDiet=Calendar.getInstance();
		try {
			startDiet.setTime(UserSettingsDBOpenHelper.DATE_FORMATTER.parse(start));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		sumo la cantidad de dias solicitados a el d'ia de inicio
		final long daysafterDietinMillis=startDiet.getTimeInMillis()+lastdays*86400000;
		
		Calendar date=Calendar.getInstance();
//		si es mayor el dia de inicio m'as la cantidad de dias
//		entonces solo escojo la diferencia de dias con el d'ia inicial
		if(daysafterDietinMillis>date.getTimeInMillis()){
			int i=1;
			while(date.getTimeInMillis()-i*86400000>startDiet.getTimeInMillis() || i>7){
				i++;
			}
			lastdays=i;
		}
		
		usDB.close();
		
//		FIN DE LA MECANICA PARA EL REINICIO DE LA DIETA
		
		double calories[] = new double[lastdays];
		
		
		
		for(int i=0;i<lastdays;i++){
			MealCursor meals = db.getMealByDate(date);
			meals.moveToFirst();
				
			while(!meals.isAfterLast()) {
				Meal meal = meals.getMeal();
				FoodWithQuantityCursor foods = db.getFoodsWithQuantityByMealId(meal.getId());
				foods.moveToFirst();
				while(!foods.isAfterLast()) {
					double quantity = foods.getQuantity();
					quantity = quantity > 0.0 ? quantity : 0.0; 
					calories[i] += foods.getFood().getCalories() * quantity;
					foods.moveToNext();
				}
				foods.close();
				meals.moveToNext();
			}
			meals.close();
			
			long tempmillis=date.getTimeInMillis()-86400000; //resto un dia en milisegundos a la fecha actual
			
			date.setTimeInMillis(tempmillis);
		}
			
		db.close();
		return calories;
	}
	
}
