package com.kaymansoft.model;

import java.text.SimpleDateFormat;

/**
 * Constantes usadas en el modelo
 * @author Alvaro Javier
 *
 */
public class MCons {

	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
	public static final String TIME_FORMAT = "HH:mm";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat(DATETIME_FORMAT);
	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);
	public static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat(TIME_FORMAT);
	
	public static final String IMAGES_TABLE 		= "Images";
	public static final String IMAGES_IMAGE_FIELD 	= "image";

	public static final String WEIGHTS_TABLE 		= "Weights";
	public static final String WEIGHTS_WEIGHT_FIELD = "weight";
	public static final String WEIGHTS_DATE_FIELD 	= "date";

	public static final String FOODS_CATEGORIES_TABLE 	= "FoodsCategories";
	public static final String FOODS_MENUS_TABLE 		= "FoodsMenus";
	public static final String FOODS_MEALS_TABLE 		= "FoodsMeals";

	public static final String ID_FIELD 				= "_id";
	public static final String NAME_FIELD 				= "name";
	public static final String DESCRIPTION_FIELD 		= "description";
	public static final String IS_USER_DEFINED_FIELD 	= "is_user_defined";

	public static final String FOODS_TABLE 					= "Foods";
	public static final String FOODS_TEMP_TABLE				= "TempFoodsView";
	public static final String FOODS_IMAGE_FIELD			= "image_id";
	public static final String FOODS_UNIT_FIELD 			= "unit";
	public static final String FOODS_CALORIES_FIELD 		= "calories";
	public static final String FOODS_CARBOHYDRATES_FIELD 	= "carbohydrates";
	public static final String FOODS_FAT_FIELD 				= "fat";
	public static final String FOODS_PROTEIN_FIELD 			= "protein";
	
	public static final String CATEGORIES_TABLE 		= "Categories";
	public static final String CATEGORIES_IMAGE_FIELD 	= "image_id";

	public static final String MENUS_TABLE 			= "Menus";
	public static final String MENUS_IMAGE_FIELD 	= "image_id";

	public static final String MEALS_TABLE 				= "Meals";
	public static final String MEALS_MENU_ID_FIELD 		= "menu_id";
	public static final String MEALS_DATE_FIELD 		= "date";

	public static final String MEALTYPES_START_TIME_FIELD 	= "start_time";
	public static final String MEALTYPES_END_TIME_FIELD 	= "end_time";

	public static final String FOODS_CATEGORIES_FOOD_ID_FIELD 		= "food_id";
	public static final String FOODS_CATEGORIES_CATEGORY_ID_FIELD 	= "category_id";

	public static final String FOODS_MENUS_MENU_ID_FIELD 	= "menu_id";
	public static final String FOODS_MENUS_FOOD_ID_FIELD 	= "food_id";
	public static final String FOODS_MENUS_QUANTITY_FIELD 	= "quantity";

	public static final String FOODS_MEALS_MEAL_ID_FIELD 	= "meal_id";
	public static final String FOODS_MEALS_FOOD_ID_FIELD 	= "food_id";
	public static final String FOODS_MEALS_QUANTITY_FIELD 	= "quantity";
	
	public static final String PLANNING_TABLE 				= "Planning";
	public static final String PLANNING_DATE_FIELD 		= "date";
	public static final String PLANNING_MEAL_NUMBER_FIELD 	= "meal_number";
	public static final String PLANNING_MENU_ID_FIELD 		= "menu_id";
	
	public static final String FOODS_EXTRA_INFO_TABLE 				= "FoodsExtraInfo";
	public static final String FOODS_EXTRA_INFO_IS_BREAKFAST_FIELD 	= "is_breakfast";
	public static final String FOODS_EXTRA_INFO_IS_SNACK_FIELD 		= "is_snack";
	public static final String FOODS_EXTRA_INFO_IS_LUNCH_FIELD 		= "is_lunch";
	public static final String FOODS_EXTRA_INFO_IS_DINNER_FIELD 	= "is_dinner";
	public static final String FOODS_EXTRA_INFO_PRIORITY_FIELD 		= "priority";
	
}
