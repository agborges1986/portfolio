package com.kaymansoft;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.cursors.FoodCursor;
import com.kaymansoft.model.elements.Food;
import com.kaymansoft.model.elements.Menu;
import com.kaymansoft.model.planning.DayPlanning;
import com.kaymansoft.model.planning.DayPlanning.MealType;
import com.kaymansoft.model.planning.WeekPlanning;

public class ShopReportBuilder {

	private Context						context;
	private WeekPlanning				currentWeek;
	private WeekPlanning				nextWeek;
	private WeekPlanning				defaultWeek;
	private Map<Food, Double>			dataCurrent;
	Map<Food, Double>					dataNext;

	private static final DecimalFormat	FORMATTER	= new DecimalFormat("#0.0");

	public ShopReportBuilder(Context ctx, WeekPlanning current, WeekPlanning next, WeekPlanning def) {
		context = ctx;
		currentWeek = current;
		nextWeek = next;
		defaultWeek = def;
	}

	public void collectData() {
		dataCurrent = collectWeekData(currentWeek, Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
		dataNext = collectWeekData(nextWeek, Calendar.SUNDAY);
	}

	private Map<Food, Double> collectDayData(DayPlanning dp, DayPlanning defaultDp, MealType firstMeal) {
		Map<Food, Double> data = new HashMap<Food, Double>();
		AppDBOpenHelper db = new AppDBOpenHelper(context);

		for (MealType mt : MealType.values()) {
			if (mt.compareTo(firstMeal) >= 0) {
				Menu menu = dp.getMenuForMeal(mt) != null ? dp.getMenuForMeal(mt) : defaultDp.getMenuForMeal(mt);
				if (menu != null) {
					FoodCursor foods = db.getFoodsWithQuantityByMenuId(menu.getId());
					while (foods.moveToNext()) {
						Food f = foods.getFood();
						if (!data.containsKey(f)) {
							data.put(f, f.getQuantity());
						} else {
							data.put(f, data.get(f) + f.getQuantity());
						}
					}
					foods.close();
				}
			}
		}

		db.close();
		return data;
	}

	private Map<Food, Double> collectWeekData(WeekPlanning wp, int firstDay) {
		Map<Food, Double> data = new HashMap<Food, Double>();
		for (int day = firstDay; day < Calendar.SATURDAY; day++) {
			Map<Food, Double> dayData = collectDayData(wp.getDayPlanning(day), defaultWeek.getDayPlanning(day), MealType.Breakfast);
			for (Food key : dayData.keySet()) {
				if (!data.containsKey(key)) {
					data.put(key, dayData.get(key));
				} else {
					data.put(key, data.get(key) + dayData.get(key));
				}
			}
		}
		return data;
	}

	public String generateReport(Map<Food, Double> data) {
		StringBuilder report = new StringBuilder();
		for (Food f : data.keySet()) {
			report.append("\t" + FORMATTER.format(data.get(f)) + " x " + f.getUnit() + " - " + f.getName() + "\n");
		}
		return report.toString();
	}

	public String generateCurrentWeekReport() {
		return generateReport(dataCurrent);
	}

	public String generateNextWeekReport() {
		return generateReport(dataNext);
	}

	public Map<Food, Double> getCurrentWeekData() {
		return dataCurrent;
	}

	public Map<Food, Double> getNextWeekData() {
		return dataNext;
	}

}
