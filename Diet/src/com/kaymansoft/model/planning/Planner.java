package com.kaymansoft.model.planning;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.util.Log;

import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.elements.Food;
import com.kaymansoft.model.planning.MenuGenerator.DietStep;
import com.kaymansoft.settings.UserSettings;
import com.kaymansoft.settings.UserSettingsDBOpenHelper;

public class Planner {
	
	private UserSettings us;
	private MenuGenerator generator;
	private AppDBOpenHelper db;
	private final Random rand = new Random(System.currentTimeMillis());
	private static int breakfastCounter = 0; //desayunos generados 
	private static int snackCounter = 0; //meriendas generados 
	private static int lunchCounter = 0; //almuerzos generados 
	private static int dinnerCounter = 0; //cenas generadas 
	
	private Map<DietStep, List<List<Food>>> breakfasts;
	private Map<DietStep, List<List<Food>>> snacks;
	private Map<DietStep, List<List<Food>>> lunchs;
	private Map<DietStep, List<List<Food>>> dinners;
	
	private boolean usingDefaultName = true;
	private boolean usingDefaultDesc = true;
	private static final String DEFAULT_NAME = "Generated Menu";
	private static final String DEFAULT_DESC = "PLANNING";
	private String bName,sName,lName,dName,desc;
	
	public Planner(UserSettings us, AppDBOpenHelper db) {
		this.us = us;
		this.db = db;
		
		generator = new MenuGenerator(db, us);
		
		breakfasts = generator.generateBreakfasts();
		snacks = generator.generateSnacks();
		lunchs = generator.generateLunchs();
		dinners = generator.generateDinners();
				
	}
	
	public void setMenuNamesPerMeal(String breakfastName,String snackName,String lunchName,String dinnerName) {
		bName = breakfastName;
		sName = snackName;
		lName = lunchName;
		dName = dinnerName;
		usingDefaultName = false;
	}
	
	public void setDefaultMenuDescription(String description) {
		desc = description;
		usingDefaultDesc = false;
	}
	
	public void generatePlanning() {
		
		Calendar day = Calendar.getInstance();
		SimpleDateFormat dateFormatter = UserSettingsDBOpenHelper.DATE_FORMATTER;
		try {
			day.setTime(dateFormatter.parse(us.getDietStartDay()));
		} catch (ParseException e) {
			Log.e(Planner.class.getName(), "Error parseando la fecha de inicio de la dieta");
			return;
		}
		
		Calendar defaultDay = PlanningUtils.getDefaultWeekPlanning().getFirstDay();
		
		generateMenus(5,day, 		DietStep.FirstFiveDays);
		generateMenus(5,day, 		DietStep.FiveToTenDays);
		generateMenus(5,day, 		DietStep.TenToFifteenDays);
		generateMenus(7,defaultDay,	DietStep.MoreThanFifteenDays);
		
		
	}
	
	private void generateMenus(int daysInStep, Calendar day, DietStep step) {
		//escoger los 5 desayunos
		List<List<Food>> selectedBreakfast = pickUp(daysInStep,breakfasts.get(step));
		
		//escoger las 15 meriendas
		List<List<Food>> selectedSnacks = pickUp(daysInStep*3,snacks.get(step));
		
		//escoger los 5 almuerzos
		List<List<Food>> selectedLunchs = pickUp(daysInStep,lunchs.get(step));
		
		//escoger las 5 cenas
		List<List<Food>> selectedDinners = pickUp(daysInStep,dinners.get(step));
		
		//planificar los desayunos para este paso de la dieta
		for(int i=0;i<daysInStep;i++,day.add(Calendar.DAY_OF_YEAR, 1)) {
			//poner el desyuno para este día
			long menuId = makeMenu(usingDefaultName? DEFAULT_NAME : bName + " " + (++breakfastCounter), selectedBreakfast.get(i));
			if(menuId!=-1)
				db.addPlanning(day, 0 /*desayuno*/, menuId);
			
			//poner una merienda por la mañana
			menuId = makeMenu(usingDefaultName? DEFAULT_NAME : sName + " " + (++snackCounter), selectedSnacks.get(i));
			if(menuId!=-1)
				db.addPlanning(day, 1 /*merienda mañana*/, menuId);

			//poner almuerzo
			menuId = makeMenu(usingDefaultName? DEFAULT_NAME : lName + " " + (++lunchCounter), selectedLunchs.get(i));
			if(menuId!=-1)
				db.addPlanning(day, 2 /*almuerzo*/, menuId);
			
			//poner una merienda por la tarde
			menuId = makeMenu(usingDefaultName? DEFAULT_NAME : sName + " " + (++snackCounter), selectedSnacks.get(5+i));
			if(menuId!=-1)
				db.addPlanning(day, 3 /*merienda tarde*/, menuId);
			
			//poner cena
			menuId = makeMenu(usingDefaultName? DEFAULT_NAME : dName + " " + (++dinnerCounter), selectedDinners.get(i));
			if(menuId!=-1)
				db.addPlanning(day, 4 /*cena*/, menuId);
			
			//poner una merienda por la tarde
			menuId = makeMenu(usingDefaultName? DEFAULT_NAME : sName + " " + (++snackCounter), selectedSnacks.get(10+i));
			if(menuId!=-1)
				db.addPlanning(day, 5 /*merienda noche*/, menuId);
		}
	}

	/**
	 * Crea el menú en la BD con los alimentos de la lista
	 * @param menuName nombre para el nuevo menú
	 * @param foodList lista de alimentos con sus cantidades
	 * @return el id del nuevo menú
	 */
	private long makeMenu(String menuName, List<Food> foodList) {
		long menuId = db.addGeneratedMenu(menuName, usingDefaultDesc? DEFAULT_DESC : desc, -1);
		for(Food food : foodList) {
			db.addFoodToMenu(menuId, food.getId(), food.getQuantity());
		}
		return menuId;		
	}

	/**
	 * Escoge algunos menús al azar
	 * @param count contidad de menús a escoger
	 * @param menuList lista de menús
	 * @return una lista con 5 menús
	 */
	private List<List<Food>> pickUp(int count, List<List<Food>> menuList) {
		List<List<Food>> res = new ArrayList<List<Food>>(5);
		for(int i=0;i<count;i++) {
			int j = rand.nextInt(menuList.size());
			res.add(menuList.get(j));
		}
		return res;
	}

}
