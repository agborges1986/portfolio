package com.kaymansoft.model.planning;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.util.Log;

import com.kaymansoft.calories.CaloriesConsumptionUtils;
import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.ModelUtils;
import com.kaymansoft.model.cursors.FoodCursor;
import com.kaymansoft.model.cursors.FoodExtraInfoCursor;
import com.kaymansoft.model.elements.Food;
import com.kaymansoft.model.elements.FoodExtraInfo;
import com.kaymansoft.model.planning.DayPlanning.MealType;
import com.kaymansoft.settings.UserSettings;
import com.kaymansoft.settings.UserSettingsDBOpenHelper;

public class MenuGenerator {
	
	private static final int MAX_GENERATED_MENUS_NUMBER = 40;
	private static final int MIN_GENERATED_MENUS_NUMBER = 1;
	private static final int MAX_FOODS_PER_MENU = 6;
	private static final int MIN_FOODS_PER_MENU = 3;
	private static final int MAX_FOODS_QUANTITY = 3;

	private static final int MAX_ITERATIONS_TO_FIND_FOOD = 30;
	private static final int MAX_ITERATIONS_TO_GEN_MENU = 200;

	private static final double MIN_MENU_EVALUATION = 0.3;
		
	private static final double[] FOOD_QUANTITY_ACCEPTANCES = new double[] { 
		1, //para una sola unidad del alimento 
		0.6, //para dos unidades
		0.3  //para tres unidades
		};
	//TODO GENERACIÓN preguntarle a Ariel los porcientos de calorías por comida
	private static final double BREAKFAST_CAL_PERCENT = 0.16; 
	private static final double SNACK_CAL_PERCENT = 0.08; 
	private static final double LUNCH_CAL_PERCENT = 0.3; 
	private static final double DINNER_CAL_PERCENT = 0.3; 
	
	private final Random rand = new Random(System.currentTimeMillis()); //generador de números aleatorios 
	
	private List<Food> foods;
	private Map<Long,FoodExtraInfo> extra;
	private UserSettings us;
	
	public enum DietStep { FirstFiveDays, FiveToTenDays, TenToFifteenDays, MoreThanFifteenDays };
	
	public MenuGenerator(AppDBOpenHelper db, UserSettings us) {
		this.us = us;
		FoodCursor fc = db.getFoodsHavingExtraInfo();		
		foods = ModelUtils.getAllFoods(fc);
		fc.close();
		
		FoodExtraInfoCursor ec = db.getFoodExtraInfos();
		extra = new HashMap<Long, FoodExtraInfo>(ec.getCount());
		while(ec.moveToNext()) {
			FoodExtraInfo extraInfo = ec.getExtraInfo();
			extra.put(extraInfo.getId(),extraInfo);
		}
		ec.close();	
	
	}
	
	/**
	 * Filtra la lista de alimentos cargados de acuerdo al tipo de comida 
	 * @param mealType tipo de comida para filtrar los alimentos
	 * @return los alimentos filtrados
	 */
	private List<Food> filterFoodsByMealType(MealType mealType) {
		List<Food> filteredFoods = new ArrayList<Food>(foods.size()); 
		for(Food f: foods) {
			boolean addIt = false;
			switch(mealType) {
			case Breakfast:
				addIt = extra.get(f.getId()).isBreakfast();
				break;		
			case MidMorningSnack:
				addIt = extra.get(f.getId()).isSnack();
				break;		
			case Lunch:
				addIt = extra.get(f.getId()).isLunch();
				break;		
			case MidAfternoonSnack:
				addIt = extra.get(f.getId()).isSnack();
				break;		
			case Dinner:
				addIt = extra.get(f.getId()).isDinner();
				break;		
			case MidNightSnack:
				addIt = extra.get(f.getId()).isSnack();
				break;		
			}
			if(addIt)
				filteredFoods.add(f);
		}
		return filteredFoods;
	}
	
	/**
	 * Genera menus con alimentos dentro de la lista especificada y con un maximo de calorías para cada uno.
	 * Opcionalmente también se tiene en cuenta un rango para el máximo (por encima o por debajo).
	 * La cantidad de menús generados se controla con maxMenus y min Menus (se garantiza el cumplimiento, es decir la 
	 * cantidad de menús generados es como mínimo <b>minMenus</b> y a lo sumo <b>maxMenus</b>). 
	 * @param foodsForGen lista de alimentos que se va a utilizar para gerar los menús
	 * @param minMenus mínima cantida de menús a generar (><b>MIN_GENERATED_MENUS_NUMBER</b>)
	 * @param maxMenus máxima cantidad menús a generar (<=<b>MAX_GENERATED_MENUS_NUMBER</b>)
	 * @param maxCalories máxima cantidad de calorías por menú (se puede relajar con el rango)
	 * @param range rango de relajación para la restricción de cantidad máxima de calorías
	 * @return una lista con los menús que se generaron
	 */
	private List<List<Food>> generateMenus(List<Food> foodsForGen, int minMenus, int maxMenus, double maxCalories, double range) {
		//TODO GENERACIÓN Aquí va el pollo del arroz con pollo
		
		//alimentos por prioridades
		Map<Integer,List<Food>> foodsByPriority = new HashMap<Integer, List<Food>>();
		for(int i=0;i<10;i++)
			foodsByPriority.put(i+1, new ArrayList<Food>());
		//agrupar los alimentos por prioridades
		for(Food f : foodsForGen) {
			foodsByPriority.get(extra.get(f.getId()).getPriority()).add(f);
		}
				
		List<List<Food>> menus = new ArrayList<List<Food>>(); //lista de menús generados
		if(maxMenus < 1) //si la cantidad de máxima de menús es menor que uno, pues no se hace nada
			return menus;
		
		minMenus = minMenus<MIN_GENERATED_MENUS_NUMBER ? MIN_GENERATED_MENUS_NUMBER : minMenus; 
		maxMenus = maxMenus>MAX_GENERATED_MENUS_NUMBER ? MAX_GENERATED_MENUS_NUMBER : maxMenus; 
		minMenus = minMenus>maxMenus? maxMenus : minMenus; //si min>max, actualizar min=max
		
		//decidir aleatoriamente una cantidad de menús a generar
		int menusToGenerate = minMenus + (int) (Math.floor((maxMenus - minMenus)*(rand.nextDouble()*0.3)));
		
		//intentos de generación de menús 
		int k = 0;
		//condición de parada para la generación
		boolean stop = false;
		while(!stop) {
			List<Food> menu = generateOneMenu(foodsByPriority,maxCalories + range);
			if(evaluateMenu(menu,maxCalories) > MIN_MENU_EVALUATION) {
				menus.add(menu);
			}
			//si se excedió el máximo número de iteraciones, parar
			stop = (++k>MAX_ITERATIONS_TO_GEN_MENU); 
			//parar si se generaron todos los menús deseados
			stop = stop || (menus.size()==menusToGenerate);
		}			
		
		return menus;
	}
	
	/**
	 * Evalúa un menú de acuerdo a los alimentos que lo compoenen y a las calorías del mismo
	 * @param menu menú a evaluar
	 * @param maxCalories máximo de calorías para el menú
	 * @return un evaluación entre 0 y 1 (1 mejor evaluación)
	 */
	private double evaluateMenu(List<Food> menu, double maxCalories) {
		double eval = 0.0;
		double cals = 0.0;
		for(Food f : menu) {
			eval += extra.get(f.getId()).getPriority()/10.0;
			cals += f.getCalories()*f.getQuantity();
		}
		eval /= (double)menu.size();
		eval -= Math.abs(cals - maxCalories)/maxCalories;
		return eval>0.0? eval : 0.0;
	}

	/**
	 * Genera 1 menú
	 * @param foodsByPriority alimentos agrupados por prioridades
	 * @param maxCalories máximo de calorías para el menú
	 * @return un menú que cumple las restricciones
	 */
	private List<Food> generateOneMenu(Map<Integer,List<Food>> foodsByPriority, double maxCalories) {
		List<Food> menu = new ArrayList<Food>();
		Set<Long> foodIds = new HashSet<Long>();
		
		//obetener el número de alimentos para este menú
		int foodsInThisMenu = rand.nextInt(MAX_FOODS_PER_MENU - MIN_FOODS_PER_MENU + 1) + MIN_FOODS_PER_MENU;
		//acumulado de calorías en el menú
		double cals = 0.0;
		//intentos de búsqueda de alimento
		int k=0;
		while(menu.size()<foodsInThisMenu && k++<MAX_ITERATIONS_TO_FIND_FOOD) {
			//escoger un alimento
			Food picked = pickOneFood(foodsByPriority);
			//verificar que no está ya en el menú
			if(foodIds.contains(picked.getId()))
				continue;			
			//clonar el alimento seleccionado
			Food f = new Food();
			f.setId(picked.getId());
			f.setCalories(picked.getCalories());
			f.setQuantity(-1);
			//para cada cantidad posible de alimentos en el menú, aleatoriamente decidir si se agrega
			//la cantidad actual o no
			for(int i=0;i<MAX_FOODS_QUANTITY;i++) {
				if(
					rand.nextDouble() < FOOD_QUANTITY_ACCEPTANCES[i] && 
					cals + f.getCalories()*(i+1) <= maxCalories //verificar la cantidad de calorías
				) {
					f.setQuantity(i+1);
				} else {
					break;
				}
			}
			//si se escogió alguna cantidad para este alimento
			if(f.getQuantity()>0) {
				menu.add(f);//agregarlo al menú y
				cals += f.getQuantity() * f.getCalories();//actualizar acumulado de calorías
				//también actualizar el conjunto de ids
				foodIds.add(f.getId());
			}
		}		
		//devolver el menú construido
		return menu;
	}
	
	/**
	 * Escoge un alimento teniendo en cuenta las prioridades
	 * @param foodsByPriority alimentos agrupados por prioridades
	 * @return un alimento de la lista
	 */

	private Food pickOneFood(Map<Integer,List<Food>> foodsByPriority) {
		Food f = null;
		for(int p=10;p>0;p--) {
			List<Food> foods = foodsByPriority.get(p); 
			if(foods.size() != 0) {
				Food f2 = foods.get(rand.nextInt(foods.size()));
				if(f==null) f = f2;
				//segun 1/p decidir si se busca en otra prioridad mas pequeña o no
				if(rand.nextDouble() > 1.0/(double)p) {
					f = f2;
					break;
				}
			}
		}
		return f;
	}

	
	/**
	 * Genera un conjunto de menús para el tipo de comida especificado teniendo en cuenta el paso de la dieta
	 * (primeros 5 días, de 5 a 10, de 10 a 15, más de 15) 
	 * @param mealType máximo total de calorías para los menús generados 
	 * @param step paso de la dieta para el cual se generan los menús
	 * @return una lista con los menús generados, <code>null</code> en caso de error
	 */
	private List<List<Food>> generate(MealType mealType, DietStep step) {
				
		Calendar day = Calendar.getInstance();//utiliario para obtener las calorias recomendadas para consumo
		try {
			day.setTime(UserSettingsDBOpenHelper.DATE_FORMATTER.parse(us.getDietStartDay()));
		} catch (ParseException e) {
			Log.e(MenuGenerator.class.getName(), "Error parseando la fecha de inicio de dieta");
			return null;
		}		
		day.add(Calendar.DAY_OF_YEAR,1);//ya aquí está posicionado en un día dentro de los primeros 5
		
		switch (step) {
		case FiveToTenDays:
			day.add(Calendar.DAY_OF_YEAR,5);
			break;
		case TenToFifteenDays:
			day.add(Calendar.DAY_OF_YEAR,10);
			break;
		case MoreThanFifteenDays:
			day.add(Calendar.DAY_OF_YEAR,15);
			break;
		default:
			break;
		}
		
		double maxCalories = CaloriesConsumptionUtils.getRecommendedDailyCaloriesConsumption(us, day);
		
		//límite de calorías para los menús generados
		double caloriesBound = maxCalories * SNACK_CAL_PERCENT;
		switch(mealType) {
		case Breakfast:
			caloriesBound = maxCalories * BREAKFAST_CAL_PERCENT;
			break;		
		case MidMorningSnack:
			caloriesBound = maxCalories * SNACK_CAL_PERCENT;
			break;		
		case Lunch:
			caloriesBound = maxCalories * LUNCH_CAL_PERCENT;
			break;		
		case MidAfternoonSnack:
			caloriesBound = maxCalories * SNACK_CAL_PERCENT;
			break;		
		case Dinner:
			caloriesBound = maxCalories * DINNER_CAL_PERCENT;
			break;		
		case MidNightSnack:
			caloriesBound = maxCalories * SNACK_CAL_PERCENT;
			break;		
		}
		
		List<Food> foodsForGeneration = filterFoodsByMealType(mealType);
		//TODO refinar los parámetros siguientes	
		return generateMenus(foodsForGeneration, 5, 15, caloriesBound, caloriesBound*0.1);		
	}
	
	private Map<DietStep,List<List<Food>>> generateFor(MealType mealtype) {
		Map<DietStep,List<List<Food>>> res = new HashMap<DietStep, List<List<Food>>>();
		res.put(DietStep.FirstFiveDays, generate(mealtype, DietStep.FirstFiveDays));
		res.put(DietStep.FiveToTenDays, generate(mealtype, DietStep.FiveToTenDays));
		res.put(DietStep.TenToFifteenDays, generate(mealtype, DietStep.TenToFifteenDays));
		res.put(DietStep.MoreThanFifteenDays, generate(mealtype, DietStep.MoreThanFifteenDays));
		return res;
	}
	
	public Map<DietStep,List<List<Food>>> generateBreakfasts() {
		return generateFor(MealType.Breakfast);
	}
	
	public Map<DietStep,List<List<Food>>> generateSnacks() {
		return generateFor(MealType.MidMorningSnack);//aquí se pude usar MidAfternoonSnack o MidNightSnack
	}
	
	public Map<DietStep,List<List<Food>>> generateLunchs() {
		return generateFor(MealType.Lunch);
	}
	
	public Map<DietStep,List<List<Food>>> generateDinners() {
		return generateFor(MealType.Dinner);
	}

}
