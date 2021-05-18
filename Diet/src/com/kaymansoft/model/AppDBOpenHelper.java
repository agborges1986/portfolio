package com.kaymansoft.model;

import java.util.Calendar;
import java.util.Collection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

import com.kaymansoft.model.cursors.CategoryCursor;
import com.kaymansoft.model.cursors.FoodCursor;
import com.kaymansoft.model.cursors.FoodExtraInfoCursor;
import com.kaymansoft.model.cursors.FoodWithQuantityCursor;
import com.kaymansoft.model.cursors.ImageCursor;
import com.kaymansoft.model.cursors.MealCursor;
import com.kaymansoft.model.cursors.MenuCursor;
import com.kaymansoft.model.cursors.WeightCursor;
import com.kaymansoft.model.elements.Category;
import com.kaymansoft.model.elements.Food;
import com.kaymansoft.model.elements.FoodExtraInfo;
import com.kaymansoft.model.elements.Image;
import com.kaymansoft.model.elements.Meal;
import com.kaymansoft.model.elements.Menu;
import com.kaymansoft.model.elements.Weight;

import static com.kaymansoft.model.MCons.*;

public class AppDBOpenHelper extends SQLiteOpenHelper {

	private static class ModelCursor extends SQLiteCursor 
	implements 	CategoryCursor, FoodCursor, FoodWithQuantityCursor,	MealCursor, MenuCursor,
	ImageCursor, WeightCursor, FoodExtraInfoCursor {

		public ModelCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
			super(db, driver, editTable, query);
		}

		public Menu getMenu() {
			checkPosition();

			long 	id 				= getLong(getColumnIndex(ID_FIELD));
			String 	name 			= getString(getColumnIndex(NAME_FIELD));
			String 	description 	= getString(getColumnIndex(DESCRIPTION_FIELD));
			boolean isUserDefined 	= (0 != getInt(getColumnIndex(IS_USER_DEFINED_FIELD)));

			Menu menu = new Menu(id, name, description, isUserDefined);

			if(!isNull(getColumnIndex(MENUS_IMAGE_FIELD))) {
				long imageId = getLong(getColumnIndex(MENUS_IMAGE_FIELD));
				menu.setImageId(imageId);
			} else {
				menu.setImageId(-1);
			}

			return menu;
		}

		public Meal getMeal() {
			checkPosition();

			long 		id 			= getLong(getColumnIndex(ID_FIELD));
			String 		date 		= getString(getColumnIndex(MEALS_DATE_FIELD));

			return new Meal(id, date);
		}

		public Food getFood() {
			checkPosition();

			long 	id 				= getLong(getColumnIndex(ID_FIELD));
			String 	name 			= getString(getColumnIndex(NAME_FIELD));
			String 	description 	= getString(getColumnIndex(DESCRIPTION_FIELD));
			boolean isUserDefined 	= ( 0!= getInt(getColumnIndex(IS_USER_DEFINED_FIELD)));
			double 	calories 		= getDouble(getColumnIndex(FOODS_CALORIES_FIELD));
			double 	fat 			= getDouble(getColumnIndex(FOODS_FAT_FIELD));
			double 	protein 		= getDouble(getColumnIndex(FOODS_PROTEIN_FIELD));
			double 	carbohydrates 	= getDouble(getColumnIndex(FOODS_CARBOHYDRATES_FIELD));
			String 	unit 			= getString(getColumnIndex(FOODS_UNIT_FIELD));

			Food food = new Food(id, name, description, unit, isUserDefined, calories, fat, 
					protein, carbohydrates);

			if(!isNull(getColumnIndex(FOODS_IMAGE_FIELD))) {
				long imageId = getLong(getColumnIndex(FOODS_IMAGE_FIELD));
				food.setImageId(imageId);
			} else {
				food.setImageId(-1);
			}
			//esto es para el foodWithQuantity
			double quantity = getQuantity();
			if(quantity > 0.0)
				food.setQuantity(quantity);			
			return food;
		}

		public Category getCategory() {
			checkPosition();

			long 	id 				= getLong(getColumnIndex(ID_FIELD));
			String 	name 			= getString(getColumnIndex(NAME_FIELD));
			String 	description 	= getString(getColumnIndex(DESCRIPTION_FIELD));
			boolean isUserDefined 	= (0 != getInt(getColumnIndex(IS_USER_DEFINED_FIELD)));

			Category category = new Category(id, name, description, isUserDefined);

			if(!isNull(getColumnIndex(CATEGORIES_IMAGE_FIELD))) {
				long imageId = getLong(getColumnIndex(CATEGORIES_IMAGE_FIELD));
				category.setImageId(imageId);
			} else {
				category.setImageId(-1);
			}

			return category;
		}

		public double getQuantity() {
			checkPosition();
			int index = getColumnIndex(FOODS_MEALS_QUANTITY_FIELD);
			if(index==-1)
				index = getColumnIndex(FOODS_MENUS_QUANTITY_FIELD);
			if(index!=-1) 			
				return getDouble(index);
			else 
				return -1.0;
		}

		public Weight getWeight() {
			long 	id 		= getLong(getColumnIndex(ID_FIELD));
			String 	date 	= getString(getColumnIndex(WEIGHTS_DATE_FIELD));
			double 	weight 	= getDouble(getColumnIndex(WEIGHTS_WEIGHT_FIELD));
			return new Weight(id, weight, date);
		}

		public Image getImage() {

			long id			= getLong(getColumnIndex(ID_FIELD));
			byte image[]	= getBlob(getColumnIndex(IMAGES_IMAGE_FIELD));		
			return new Image(id, image);

		}

		public FoodExtraInfo getExtraInfo() {
			FoodExtraInfo extra = new FoodExtraInfo();

			long id	= getLong(getColumnIndex(ID_FIELD));
			boolean isB = getTruthValue(FOODS_EXTRA_INFO_IS_BREAKFAST_FIELD);
			boolean isS	= getTruthValue(FOODS_EXTRA_INFO_IS_SNACK_FIELD);
			boolean isL	= getTruthValue(FOODS_EXTRA_INFO_IS_LUNCH_FIELD);
			boolean isD	= getTruthValue(FOODS_EXTRA_INFO_IS_DINNER_FIELD);
			int p 	= getInt(getColumnIndex(FOODS_EXTRA_INFO_PRIORITY_FIELD));

			extra.setId(id);
			extra.setBreakfast(isB);
			extra.setSnack(isS);
			extra.setLunch(isL);
			extra.setDinner(isD);
			extra.setPriority(p);

			return extra;
		}

		private boolean getTruthValue(String columnName) {
			if(!isNull(getColumnIndex(columnName)) && getInt(getColumnIndex(columnName))==1) {
				return true;
			} else {
				return false;
			}
		}

	}

	private static class ModelFactory implements SQLiteDatabase.CursorFactory {

		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new ModelCursor(db, masterQuery, editTable, query);
		}

	}

	private static final String DATABASE_NAME = "AppDB.db";
	private static final int DATABASE_VERSION = 1;

	private static final String COMMON_FIELDS =
			ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
					NAME_FIELD + " TEXT NOT NULL, " +
					DESCRIPTION_FIELD + " TEXT, " +
					IS_USER_DEFINED_FIELD + " INTEGER NOT NULL "
					;

	private static final String CREATE_IMAGES_TABLE =
			"CREATE TABLE " + MCons.IMAGES_TABLE + " ( " +
					ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
					IMAGES_IMAGE_FIELD + " BLOB NOT NULL " +
					" )";

	private static final String CREATE_WEIGHTS_TABLE =
			"CREATE TABLE " + WEIGHTS_TABLE + " ( " +
					ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
					WEIGHTS_DATE_FIELD + " TEXT NOT NULL, " +
					WEIGHTS_WEIGHT_FIELD + " REAL NOT NULL " +
					" )";

	private static final String CREATE_FOODS_TABLE =
			"CREATE TABLE " + FOODS_TABLE + " ( " +
					COMMON_FIELDS + " , " + 
					FOODS_UNIT_FIELD + " TEXT NOT NULL, " +
					FOODS_CALORIES_FIELD + " REAL NOT NULL, " +
					FOODS_CARBOHYDRATES_FIELD + " REAL, " +
					FOODS_FAT_FIELD + " REAL, " +
					FOODS_PROTEIN_FIELD + " REAL, " +
					FOODS_IMAGE_FIELD + " INTEGER " +
					" )";

	private static final String CREATE_CATEGORIES_TABLE =
			"CREATE TABLE " + CATEGORIES_TABLE + " ( " +
					COMMON_FIELDS + ", " +
					CATEGORIES_IMAGE_FIELD + " INTEGER " +
					" )";

	private static final String CREATE_MENUS_TABLE =
			"CREATE TABLE " + MENUS_TABLE + " ( " +
					COMMON_FIELDS + ", " +
					MENUS_IMAGE_FIELD + " INTEGER " +
					" )";

	private static final String CREATE_MEALS_TABLE =
			"CREATE TABLE " + MEALS_TABLE + " ( " +
					ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
					DESCRIPTION_FIELD + " TEXT, " +
					MEALS_DATE_FIELD + " TEXT NOT NULL, " +
					MEALS_MENU_ID_FIELD + " INTEGER, " +
					"CONSTRAINT fk_menu_id " +
					"FOREIGN KEY (" + MEALS_MENU_ID_FIELD + " ) REFERENCES " + 
					MENUS_TABLE + " ( " + ID_FIELD + " ) " +
					" )";

	private static final String CREATE_FOODS_CATEGORIES_TABLE =
			"CREATE TABLE " + FOODS_CATEGORIES_TABLE + " ( " +
					FOODS_CATEGORIES_FOOD_ID_FIELD + " INTEGER NOT NULL, " +
					FOODS_CATEGORIES_CATEGORY_ID_FIELD + " INTEGER NOT NULL, " +
					"PRIMARY KEY ( " + FOODS_CATEGORIES_FOOD_ID_FIELD + " ASC, " + 
					FOODS_CATEGORIES_CATEGORY_ID_FIELD + " ASC), " +
					"CONSTRAINT fk_food_id " +
					"FOREIGN KEY ( " + FOODS_CATEGORIES_FOOD_ID_FIELD + " ) REFERENCES " + 
					FOODS_TABLE + " ( " + ID_FIELD + " ) ON DELETE CASCADE ON UPDATE CASCADE, " +
					"CONSTRAINT fk_category_id " +
					"FOREIGN KEY ( " + FOODS_CATEGORIES_CATEGORY_ID_FIELD + " ) REFERENCES " + 
					CATEGORIES_TABLE + " ( " + ID_FIELD + " ) ON DELETE CASCADE ON UPDATE CASCADE " +
					" )";

	private static final String CREATE_FOODS_MENUS_TABLE =
			"CREATE TABLE " + FOODS_MENUS_TABLE + " ( " +
					FOODS_MENUS_MENU_ID_FIELD + " INTEGER NOT NULL, " +
					FOODS_MENUS_FOOD_ID_FIELD + " INTEGER NOT NULL, " +
					FOODS_MENUS_QUANTITY_FIELD + " REAL NOT NULL, " +
					"PRIMARY KEY ( " + FOODS_MENUS_MENU_ID_FIELD + " ASC, " + 
					FOODS_MENUS_FOOD_ID_FIELD + " ASC), " +
					"CONSTRAINT fk_food_id " +
					"FOREIGN KEY ( " + FOODS_MENUS_FOOD_ID_FIELD + " ) REFERENCES " + 
					FOODS_TABLE + " ( " + ID_FIELD + " ) ON DELETE CASCADE ON UPDATE CASCADE, " +
					"CONSTRAINT fk_menu_id " +
					"FOREIGN KEY ( " + FOODS_MENUS_MENU_ID_FIELD + " ) REFERENCES " + 
					MENUS_TABLE + " ( " + ID_FIELD + " ) ON DELETE CASCADE ON UPDATE CASCADE " +
					" )";

	private static final String CREATE_FOODS_MEALS_TABLE =
			"CREATE TABLE " + FOODS_MEALS_TABLE + " ( " +
					FOODS_MEALS_MEAL_ID_FIELD + " INTEGER NOT NULL, " +
					FOODS_MEALS_FOOD_ID_FIELD + " INTEGER NOT NULL, " +
					FOODS_MEALS_QUANTITY_FIELD + " REAL NOT NULL, " +
					"PRIMARY KEY ( " + FOODS_MEALS_MEAL_ID_FIELD + " ASC, " + 
					FOODS_MEALS_FOOD_ID_FIELD + " ASC), " +
					"CONSTRAINT fk_food_id " +
					"FOREIGN KEY ( " + FOODS_MEALS_FOOD_ID_FIELD + " ) REFERENCES " + 
					FOODS_TABLE + " ( " + ID_FIELD + " ) ON DELETE CASCADE ON UPDATE CASCADE, " +
					"CONSTRAINT fk_meal_id " +
					"FOREIGN KEY ( " + FOODS_MEALS_MEAL_ID_FIELD + " ) REFERENCES " + 
					MEALS_TABLE + " ( " + ID_FIELD + " ) " +
					" )";

	private static final String CREATE_PLANNING_TABLE = 
			"CREATE TABLE " + PLANNING_TABLE + " ( " +
					PLANNING_DATE_FIELD + " DATE NOT NULL, " +
					PLANNING_MEAL_NUMBER_FIELD + " INTEGER NOT NULL, " +
					PLANNING_MENU_ID_FIELD + " INTEGER NOT NULL, " +
					"PRIMARY KEY ( " + PLANNING_DATE_FIELD + " ASC, " + 
					PLANNING_MEAL_NUMBER_FIELD + " ASC), " +
					"CONSTRAINT fk_menu_id " +
					"FOREIGN KEY ( " + PLANNING_MENU_ID_FIELD + " ) REFERENCES " + 
					MENUS_TABLE + " ( " + ID_FIELD + " ) ON DELETE CASCADE ON UPDATE CASCADE, " +
					"CONSTRAINT check_meal_number " +
					"CHECK ( " + 	PLANNING_MEAL_NUMBER_FIELD + " >= 0 AND " + 
					PLANNING_MEAL_NUMBER_FIELD + " <= 5 ) " +
					")";

	static final String CREATE_FOOD_EXTRA_INFO_TABLE = 
			"CREATE TABLE " + FOODS_EXTRA_INFO_TABLE + " ( " + 
					ID_FIELD + " INTEGER NOT NULL, " +
					FOODS_EXTRA_INFO_IS_BREAKFAST_FIELD + " INTEGER, " +
					FOODS_EXTRA_INFO_IS_SNACK_FIELD + " INTEGER, " +
					FOODS_EXTRA_INFO_IS_LUNCH_FIELD + " INTEGER, " +
					FOODS_EXTRA_INFO_IS_DINNER_FIELD + " INTEGER, " +
					FOODS_EXTRA_INFO_PRIORITY_FIELD + " INTEGER, " +
					" PRIMARY KEY ( " + ID_FIELD + " ASC), " +
					"CONSTRAINT fk_food_id FOREIGN KEY ( " + ID_FIELD + " ) REFERENCES " + 
					FOODS_TABLE + " ( " + ID_FIELD + " ) ";

	public AppDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, new ModelFactory(), DATABASE_VERSION);
	}

	public static int getFieldPosition(String field, String[] fields) {
		int i = 0;
		while(i<fields.length && !fields[i].equals(field)) i++;
		if(i<fields.length)
			return i;
		else
			return -1;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_IMAGES_TABLE);
		db.execSQL(CREATE_WEIGHTS_TABLE);
		db.execSQL(CREATE_FOODS_TABLE);
		db.execSQL(CREATE_CATEGORIES_TABLE);
		db.execSQL(CREATE_MENUS_TABLE);
		db.execSQL(CREATE_MEALS_TABLE);
		db.execSQL(CREATE_FOODS_CATEGORIES_TABLE);
		db.execSQL(CREATE_FOODS_MENUS_TABLE);
		db.execSQL(CREATE_FOODS_MEALS_TABLE);
		db.execSQL(CREATE_PLANNING_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// nada por ahora
	}

	//utilities stuff
	private Cursor getTable(String tableName) {
		return getReadableDatabase().rawQuery(
				"SELECT * FROM " + tableName, 
				null);
	}

	private Cursor getTableFilteredBy(String tableName, String filter) {
		return getReadableDatabase().rawQuery(
				"SELECT * FROM " + tableName + 
				" WHERE " + NAME_FIELD 			+ " LIKE " + "'%" + filter + "%' OR " + 
				DESCRIPTION_FIELD 	+ " LIKE " + "'%" + filter + "%'",
				null);				
	}

	private Cursor getRow(String tableName, long id) {
		Cursor cur = getReadableDatabase().rawQuery(
				"SELECT * FROM " + tableName + " WHERE " + 
						ID_FIELD + "=" + String.valueOf(id)
						,null);
		cur.moveToFirst();
		return cur;		
	}

	private int deleteRow(String tableName, long id) {
		SQLiteDatabase db = getWritableDatabase();
		int res = db.delete(
				tableName, 
				ID_FIELD + " = " + String.valueOf(id),
				null);
		db.close();
		return res;
	}

	//Planifications stuff
	public Cursor getPlannings(Calendar fromDate, Calendar toDate) {
		String sql = "SELECT * FROM " + PLANNING_TABLE +
				" WHERE julianday(" + PLANNING_DATE_FIELD + ") >= julianday('" + DATE_FORMATTER.format(fromDate.getTime()) + "') AND " +
				" julianday(" +PLANNING_DATE_FIELD + ") <= julianday('" + DATE_FORMATTER.format(toDate.getTime()) + "') ";
		return getReadableDatabase().rawQuery(
				sql, 
				null);		
	}

	public int deletePlanning(Calendar date, int meal) {
		SQLiteDatabase db = getWritableDatabase();
		int res = -1;
		res = db.delete(
				PLANNING_TABLE,
				PLANNING_DATE_FIELD + " = '" + DATE_FORMATTER.format(date.getTime()) + "' " + 
						" AND " + PLANNING_MEAL_NUMBER_FIELD + " = " + meal,
						null);			
		db.close();
		return res;
	}

	public long updatePlanning(Calendar date, int meal, long menuId) {
		ContentValues planning = new ContentValues();
		planning.put(PLANNING_DATE_FIELD, DATE_FORMATTER.format(date.getTime()));
		planning.put(PLANNING_MEAL_NUMBER_FIELD, meal);
		planning.put(PLANNING_MENU_ID_FIELD, menuId);

		SQLiteDatabase db = getWritableDatabase();
		long res = db.update(
				PLANNING_TABLE,
				planning,
				PLANNING_DATE_FIELD + " = '" + DATE_FORMATTER.format(date.getTime()) + "' " + 
						" AND " + PLANNING_MEAL_NUMBER_FIELD + " = " + meal,
						null);
		db.close();
		return res;
	}

	public long addPlanning(Calendar date, int meal, long menuId) {
		ContentValues planning = new ContentValues();
		planning.put(PLANNING_DATE_FIELD, DATE_FORMATTER.format(date.getTime()));
		planning.put(PLANNING_MEAL_NUMBER_FIELD, meal);
		planning.put(PLANNING_MENU_ID_FIELD, menuId);

		SQLiteDatabase db = getWritableDatabase();
		long res = db.insert(PLANNING_TABLE, null, planning);
		db.close();
		return res;
	}

	public int cleanOldPlannings() {
		Calendar today = Calendar.getInstance();
		SQLiteDatabase db = getWritableDatabase();
		int res = -1;
		res = db.delete(PLANNING_TABLE,
				" julianday(" + PLANNING_DATE_FIELD + ")<julianday(" + DATE_FORMATTER.format(today.getTime()) + ")",
				null);			
		db.close();
		return res;
	}
	
	public int wipeOutPlannings() {
		SQLiteDatabase db = getWritableDatabase();
		int res = -1;
		res = db.delete(PLANNING_TABLE,null,null);			
		db.close();
		return res;
	}
	
	public long addGeneratedMenu(String name, String description, long imageId) {
		ContentValues newMenu = new ContentValues();
		newMenu.put(NAME_FIELD, name);
		newMenu.put(DESCRIPTION_FIELD, description);
		newMenu.put(MENUS_IMAGE_FIELD, imageId);
		newMenu.put(IS_USER_DEFINED_FIELD, 0);

		SQLiteDatabase db = getWritableDatabase();
		long res = db.insert(
				MENUS_TABLE, 
				null, 
				newMenu );
		db.close();
		return res;		
	}

	public int wipeOutGeneratedMenus() {
		SQLiteDatabase db = getWritableDatabase();
		int res = db.delete(
				MENUS_TABLE, 
				IS_USER_DEFINED_FIELD + " = 0",
				null);
		db.close();
		return res;
	}
	
	//Images stuff
	public ImageCursor getImages() {
		return (ImageCursor) getTable(MCons.IMAGES_TABLE);
	}

	public ImageCursor getImageByID(long id) {
		return (ImageCursor) getRow(MCons.IMAGES_TABLE,id);
	}

	public long addImage(byte img[]) {
		ContentValues newImage = new ContentValues();
		newImage.put(IMAGES_IMAGE_FIELD, img);

		SQLiteDatabase db = getWritableDatabase();
		long res = db.insert(
				MCons.IMAGES_TABLE, 
				null, 
				newImage);
		db.close();
		return res; 		
	}

	//Weights stuff
	public WeightCursor getWeights() {
		return (WeightCursor) getTable(WEIGHTS_TABLE);
	}

	public WeightCursor getLastWeightsFirst() {
		return (WeightCursor) getReadableDatabase().rawQuery(
				"SELECT * FROM " + WEIGHTS_TABLE + " ORDER BY " +
						WEIGHTS_DATE_FIELD + " DESC ",
						null);
	}

	public long addWeight(double weight, String date) {
		ContentValues newWeight = new ContentValues();
		newWeight.put(WEIGHTS_WEIGHT_FIELD, weight);
		newWeight.put(WEIGHTS_DATE_FIELD, date);

		SQLiteDatabase db = getWritableDatabase();
		long res = db.insert(
				WEIGHTS_TABLE, 
				null, 
				newWeight);
		db.close();
		return res;
	}

	//Foods stuff
	public FoodCursor getFoods() {
		return (FoodCursor) getTable(FOODS_TABLE);
	}	

	public FoodCursor getFoodsFilteredBy(String filter) {
		return (FoodCursor) getTableFilteredBy(FOODS_TABLE, filter);
	}

	public FoodCursor getFoodsFilteredBy(String textFilter, double minCal, double maxCal, Collection<Category> categories) {
		final String textFilterQuery = 
				"("+ NAME_FIELD + " LIKE '%"+textFilter+"%' OR " + DESCRIPTION_FIELD + " LIKE '%"+textFilter+"%')";
		final String minCaloriesFilterQuery =	
				"(" + FOODS_CALORIES_FIELD + " >= " + minCal + ")";				
		final String maxCaloriesFilterQuery = 
				"(" + FOODS_CALORIES_FIELD + " <= " + maxCal + ")";		

		boolean isTextFilter = ((textFilter!=null) && !textFilter.equals("")); 
		boolean isMinCalFilter = (minCal>=0.0);
		boolean isMaxFilter = (maxCal>=0.0);		

		boolean isAnyFilter1 = (isTextFilter || isMinCalFilter || isMaxFilter);
		//primer filtro que se aplica, representa el rango de calorias y el nombre
		StringBuilder filter1 = new StringBuilder("SELECT * FROM " + FOODS_TABLE);
		if(isAnyFilter1)  {
			filter1.append(" WHERE ");

			if(isTextFilter) {
				filter1.append(textFilterQuery);
				if(isMinCalFilter || isMaxFilter)
					filter1.append(" AND ");
			}
			if(isMinCalFilter) {
				filter1.append(minCaloriesFilterQuery);
				if(isMaxFilter)
					filter1.append(" AND ");
			}
			if(isMaxFilter) {
				filter1.append(maxCaloriesFilterQuery);
			}			
		} 

		boolean isCatFilter = ((categories!=null) && !categories.isEmpty()); 
		//si no se filtra por categorías
		if(!isCatFilter) {
			//devolver según el filtro inicial
			return (FoodCursor) getWritableDatabase().rawQuery(filter1.toString(), null);			
		} else {
			//si no se realiza ningún filtro inicial, cambiamos esta variable por el nombre de la tabla de alimentos
			if(!isAnyFilter1) {
				filter1 = new StringBuilder(FOODS_TABLE);
			}
			//construir la lista de Ids de categorías
			StringBuilder catIdList = new StringBuilder("(");
			for(Category cat : categories) {
				catIdList.append(",").append(cat.getId()); 
			}
			catIdList.append(")").deleteCharAt(1); //quitar la coma del principio
			//filtro completo
			String filterAll = 
					"SELECT DISTINCT " + 
							FOODS_TEMP_TABLE + "." + ID_FIELD + " AS " + ID_FIELD + " , " + 
							FOODS_TEMP_TABLE + "." + NAME_FIELD + " AS " + NAME_FIELD + " , " + 
							FOODS_TEMP_TABLE + "." + DESCRIPTION_FIELD + " AS " + DESCRIPTION_FIELD + " , " + 
							FOODS_TEMP_TABLE + "." + IS_USER_DEFINED_FIELD + " AS " + IS_USER_DEFINED_FIELD + " , " + 
							FOODS_TEMP_TABLE + "." + FOODS_UNIT_FIELD + " AS " + FOODS_UNIT_FIELD + " , " + 
							FOODS_TEMP_TABLE + "." + FOODS_CALORIES_FIELD + " AS " + FOODS_CALORIES_FIELD + " , " + 
							FOODS_TEMP_TABLE + "." + FOODS_CARBOHYDRATES_FIELD + " AS " + FOODS_CARBOHYDRATES_FIELD + " , " + 
							FOODS_TEMP_TABLE + "." + FOODS_FAT_FIELD + " AS " + FOODS_FAT_FIELD + " , " + 
							FOODS_TEMP_TABLE + "." + FOODS_PROTEIN_FIELD + " AS " + FOODS_PROTEIN_FIELD + " , " + 
							FOODS_TEMP_TABLE + "." + FOODS_IMAGE_FIELD + " AS " + FOODS_IMAGE_FIELD + 
							" FROM " + " ( " + filter1.toString() /*puede ser el nombre de una tabla*/+ ") AS " + FOODS_TEMP_TABLE + 
							" JOIN " + FOODS_CATEGORIES_TABLE +	
							" ON " + FOODS_TEMP_TABLE + "." + ID_FIELD + "=" + FOODS_CATEGORIES_TABLE + "." + FOODS_CATEGORIES_FOOD_ID_FIELD +
							" WHERE " + FOODS_CATEGORIES_CATEGORY_ID_FIELD + " IN " + catIdList;

			return (FoodCursor) getWritableDatabase().rawQuery(filterAll, null);			
		}
	}

	public FoodCursor getFoodById(long id) {
		return (FoodCursor) getRow(FOODS_TABLE,id);
	}

	public long addFood(String name, String description, String unit, double calories,
			double carbohydrates, double fat, double protein, long imageId) {

		ContentValues newFood = new ContentValues();
		newFood.put(NAME_FIELD, name);
		newFood.put(DESCRIPTION_FIELD, description);
		newFood.put(IS_USER_DEFINED_FIELD, 1);
		newFood.put(FOODS_UNIT_FIELD, unit);
		newFood.put(FOODS_CALORIES_FIELD, calories);
		newFood.put(FOODS_CARBOHYDRATES_FIELD, carbohydrates);
		newFood.put(FOODS_FAT_FIELD, fat);
		newFood.put(FOODS_PROTEIN_FIELD, protein);
		newFood.put(FOODS_IMAGE_FIELD, imageId);

		SQLiteDatabase db = getWritableDatabase();
		long res = db.insert(
				FOODS_TABLE, 
				null, 
				newFood );
		db.close();
		return res;		
	}

	public int updateFood(long id,String name, String description, String unit, 
			double calories, double carbohydrates, double fat, double protein, long imageId) {
		ContentValues updateFood = new ContentValues();
		//updateFood.put(ID_FIELD, id);
		updateFood.put(NAME_FIELD, name);
		updateFood.put(DESCRIPTION_FIELD, description);
		updateFood.put(IS_USER_DEFINED_FIELD, 1);
		updateFood.put(FOODS_UNIT_FIELD, unit);
		updateFood.put(FOODS_CALORIES_FIELD, calories);
		updateFood.put(FOODS_CARBOHYDRATES_FIELD, carbohydrates);
		updateFood.put(FOODS_FAT_FIELD, fat);
		updateFood.put(FOODS_PROTEIN_FIELD, protein);
		updateFood.put(FOODS_IMAGE_FIELD, imageId);

		SQLiteDatabase db = getWritableDatabase();
		int res = db.update(FOODS_TABLE, updateFood, 
				ID_FIELD + "=" +  String.valueOf(id),
				null);
		db.close();
		return res;
	}

	public int deleteFood(long id) {
		return deleteRow(FOODS_TABLE, id);
	}

	public CategoryCursor getCategoriesByFoodId(long foodId) {
		String sql = " SELECT " +
				CATEGORIES_TABLE + "." + ID_FIELD + " , " +
				CATEGORIES_TABLE + "." + NAME_FIELD + " , " +
				CATEGORIES_TABLE + "." + DESCRIPTION_FIELD + " , " +
				CATEGORIES_TABLE + "." + IS_USER_DEFINED_FIELD + " , " + 
				CATEGORIES_TABLE + "." + CATEGORIES_IMAGE_FIELD +  
				" FROM " + FOODS_CATEGORIES_TABLE + " JOIN " +
				CATEGORIES_TABLE + 
				" ON " + FOODS_CATEGORIES_TABLE + "." +
				FOODS_CATEGORIES_CATEGORY_ID_FIELD + " = " +
				CATEGORIES_TABLE + "." + ID_FIELD + 
				" WHERE " +
				FOODS_CATEGORIES_TABLE + "." + FOODS_CATEGORIES_FOOD_ID_FIELD +
				" = " + String.valueOf(foodId) + 
				" ORDER BY " + CATEGORIES_TABLE + "." + ID_FIELD + " DESC ";
		return (CategoryCursor) getReadableDatabase().rawQuery(
				sql,
				null);		
	}

	public FoodCursor getFoodsHavingExtraInfo() {
		return (FoodCursor) getReadableDatabase().rawQuery(
				" SELECT * FROM " + FOODS_TABLE + " JOIN " + FOODS_EXTRA_INFO_TABLE + 
				" ON " + FOODS_TABLE + "." + ID_FIELD + " = " + FOODS_EXTRA_INFO_TABLE + "." + ID_FIELD
				,
				null);
	}

	public FoodExtraInfoCursor getFoodExtraInfos() {
		return (FoodExtraInfoCursor) getTable(FOODS_EXTRA_INFO_TABLE);
	}

	public FoodExtraInfoCursor getFoodExtraInfoByFoodId(long foodId) {
		return (FoodExtraInfoCursor) getRow(FOODS_EXTRA_INFO_TABLE, foodId);
	}

	//Menus Stuff
	public MenuCursor getMenus() {
		return (MenuCursor) getTable(MENUS_TABLE);				
	}

	public MenuCursor getMenusFilteredBy(String filter) {
		return (MenuCursor) getTableFilteredBy(MENUS_TABLE, filter);
	}

	public MenuCursor getMenuById(long id) {
		return (MenuCursor) getRow(MENUS_TABLE, id);
	}

	public long addMenu(String name, String description, long imageId) {
		ContentValues newMenu = new ContentValues();
		newMenu.put(NAME_FIELD, name);
		newMenu.put(DESCRIPTION_FIELD, description);
		newMenu.put(MENUS_IMAGE_FIELD, imageId);
		newMenu.put(IS_USER_DEFINED_FIELD, 1);

		SQLiteDatabase db = getWritableDatabase();
		long res = db.insert(
				MENUS_TABLE, 
				null, 
				newMenu );
		db.close();
		return res;		
	}

	public int updateMenu(long id,String name, String description, long imageId) {
		ContentValues updateMenu = new ContentValues();
		updateMenu.put(NAME_FIELD, name);
		updateMenu.put(DESCRIPTION_FIELD, description);
		updateMenu.put(MENUS_IMAGE_FIELD, imageId);

		SQLiteDatabase db = getWritableDatabase();
		int res = db.update(MENUS_TABLE, updateMenu, 
				ID_FIELD + "=" +  String.valueOf(id),
				null);
		db.close();
		return res;
	}

	public int deleteMenu(long id) {
		return deleteRow(MENUS_TABLE, id);
	}

	public FoodWithQuantityCursor getFoodsWithQuantityByMenuId(long menuId) {
		String sql = " SELECT " +
				FOODS_TABLE + "." + ID_FIELD + " , " +
				FOODS_TABLE + "." + NAME_FIELD + " , " +
				FOODS_TABLE + "." + DESCRIPTION_FIELD + " , " +
				FOODS_TABLE + "." + IS_USER_DEFINED_FIELD + " , " +
				FOODS_TABLE + "." + FOODS_UNIT_FIELD + " , " +
				FOODS_TABLE + "." + FOODS_CALORIES_FIELD + " , " +
				FOODS_TABLE + "." + FOODS_CARBOHYDRATES_FIELD + " , " +
				FOODS_TABLE + "." + FOODS_FAT_FIELD + " , " +
				FOODS_TABLE + "." + FOODS_PROTEIN_FIELD + " , " +
				FOODS_TABLE + "." + FOODS_IMAGE_FIELD + " , " +
				FOODS_MENUS_TABLE + "." + FOODS_MENUS_QUANTITY_FIELD +
				" AS " + FOODS_MENUS_QUANTITY_FIELD +
				" FROM " + FOODS_TABLE + " JOIN " +
				FOODS_MENUS_TABLE + " JOIN " + MENUS_TABLE + 				
				" ON " + FOODS_TABLE + "." + ID_FIELD +
				" = " + FOODS_MENUS_TABLE + "." + FOODS_MENUS_FOOD_ID_FIELD +
				" AND " + FOODS_MENUS_TABLE + "." + FOODS_MENUS_MENU_ID_FIELD +
				" = " + MENUS_TABLE + "." + ID_FIELD + 
				" WHERE " +
				FOODS_MENUS_TABLE + "." + FOODS_MENUS_MENU_ID_FIELD +
				"=" + String.valueOf(menuId);
		return (FoodWithQuantityCursor) getReadableDatabase().rawQuery(
				sql,
				null);				
	}

	public long addFoodToMenu(long menuId, long foodId, double quantity) {
		ContentValues newFoodMenu = new ContentValues();
		newFoodMenu.put(FOODS_MENUS_FOOD_ID_FIELD, foodId);
		newFoodMenu.put(FOODS_MENUS_MENU_ID_FIELD, menuId);
		newFoodMenu.put(FOODS_MEALS_QUANTITY_FIELD, quantity);
		SQLiteDatabase db = getWritableDatabase();
		long res = db.insert(
				FOODS_MENUS_TABLE,
				null,
				newFoodMenu);
		db.close();
		return res;
	}

	public long updateFoodInMenu(long menuId, long foodId, double quantity) {
		ContentValues updateFoodsMenu = new ContentValues();
		updateFoodsMenu.put(FOODS_MENUS_FOOD_ID_FIELD, foodId);
		updateFoodsMenu.put(FOODS_MENUS_MENU_ID_FIELD, menuId);
		updateFoodsMenu.put(FOODS_MENUS_QUANTITY_FIELD, quantity);

		SQLiteDatabase db = getWritableDatabase();
		int res = db.update(FOODS_MENUS_TABLE, updateFoodsMenu, 
				FOODS_MENUS_FOOD_ID_FIELD + "=" +  String.valueOf(foodId) +  " AND " +
						FOODS_MENUS_MENU_ID_FIELD + "=" +  String.valueOf(menuId),
						null);
		db.close();
		return res;
	}

	public int removeFoodFromMenu(long menuId, long foodId) {
		SQLiteDatabase db = getWritableDatabase();
		int res = db.delete(
				FOODS_MENUS_TABLE,
				FOODS_MENUS_FOOD_ID_FIELD + " = " + foodId + " AND " +
						FOODS_MENUS_MENU_ID_FIELD + " = " + menuId,
						null);
		db.close();

		return res;
	}

	//Categories stuff
	public CategoryCursor getCategories() {
		return (CategoryCursor) getTable(CATEGORIES_TABLE);				
	}

	public CategoryCursor getFastFoodCategories() {
		return (CategoryCursor) getReadableDatabase().rawQuery(
				" SELECT * FROM " + CATEGORIES_TABLE + 
				" WHERE " + ID_FIELD + " >= 500 AND " + ID_FIELD + " <= 700 ",
				null);
	}

	public CategoryCursor getCategoryById(long id) {
		return (CategoryCursor) getRow(CATEGORIES_TABLE,id);				
	}

	public long addCategory(String name, String description, long imageId) {
		ContentValues newCategory = new ContentValues();
		newCategory.put(NAME_FIELD, name);
		newCategory.put(DESCRIPTION_FIELD, description);
		newCategory.put(CATEGORIES_IMAGE_FIELD, imageId);
		newCategory.put(IS_USER_DEFINED_FIELD, 1);
		SQLiteDatabase db = getWritableDatabase();
		long res = db.insert(
				CATEGORIES_TABLE, 
				null, 
				newCategory );
		db.close();
		return res;		
	}

	public int updateCategory(long id,String name, String description, long imageId) {
		ContentValues updateCategory = new ContentValues();
		updateCategory.put(NAME_FIELD, name);
		updateCategory.put(DESCRIPTION_FIELD, description);
		updateCategory.put(CATEGORIES_IMAGE_FIELD, imageId);

		SQLiteDatabase db = getWritableDatabase();
		int res = db.update(CATEGORIES_TABLE, updateCategory, 
				ID_FIELD + "=" +  String.valueOf(id),
				null);
		db.close();
		return res;
	}

	public int deleteCategory(long id) {
		return deleteRow(CATEGORIES_TABLE, id);
	}

	public long addFoodToCategory(long categoryId, long foodId) {
		ContentValues newCategoryFood = new ContentValues();
		newCategoryFood.put(FOODS_CATEGORIES_FOOD_ID_FIELD, foodId);
		newCategoryFood.put(FOODS_CATEGORIES_CATEGORY_ID_FIELD, categoryId);

		SQLiteDatabase db = getWritableDatabase();
		long res = db.insert(
				FOODS_CATEGORIES_TABLE, 
				null, 
				newCategoryFood );
		db.close();
		return res;
	}

	public int removeFoodFromCategory(long categoryId, long foodId) {
		SQLiteDatabase db = getWritableDatabase();
		int res = db.delete(
				FOODS_CATEGORIES_TABLE,
				FOODS_CATEGORIES_FOOD_ID_FIELD + " = " + foodId + " AND " +
						FOODS_CATEGORIES_CATEGORY_ID_FIELD + " = " + categoryId,
						null);
		db.close();
		return res;
	}

	//Meals stuff
	public MealCursor getMeals() {
		return (MealCursor) getTable(MEALS_TABLE);				
	}

	public MealCursor getMealById(long id) {
		return (MealCursor) getRow(MEALS_TABLE,id);				
	}

	public MealCursor getMealByDate(Calendar calendar) {
		String sql = "SELECT * FROM " + MEALS_TABLE + 
				" WHERE date(" + MEALS_TABLE + "." + MEALS_DATE_FIELD + ")='" +  MCons.DATE_FORMATTER.format(calendar.getTime()) + "'";
		return (MealCursor) getReadableDatabase().rawQuery(
				sql,
				null);
	}

	public long addMeal(String description, String date) {
		return addMeal(description, date, null);	
	}

	public long addMeal(String description, String date, Long menuId) {
		ContentValues newMeal = new ContentValues();
		newMeal.put(DESCRIPTION_FIELD, description);
		newMeal.put(MEALS_DATE_FIELD, date);
		newMeal.put(MEALS_MENU_ID_FIELD, menuId);
		SQLiteDatabase db = getWritableDatabase();
		long res = db.insert(
				MEALS_TABLE, 
				null, 
				newMeal );
		db.close();
		return res;	
	}

	public MenuCursor getMenuByMealId(long mealId) {
		return (MenuCursor) getReadableDatabase().rawQuery(
				"SELECT " +
						MENUS_TABLE + "." + ID_FIELD + " , " +
						MENUS_TABLE + "." + NAME_FIELD + " , " +
						MENUS_TABLE + "." + DESCRIPTION_FIELD + " , " +
						MENUS_TABLE + "." + IS_USER_DEFINED_FIELD +
						" FROM " + MENUS_TABLE + " JOIN " +	MEALS_TABLE + 
						" ON " + MEALS_TABLE + "." + MEALS_MENU_ID_FIELD +
						" = " + MENUS_TABLE + "." + ID_FIELD +
						" WHERE " + MEALS_TABLE + "." + ID_FIELD +
						" = " + String.valueOf(mealId),
						null);
	}

	public FoodWithQuantityCursor getFoodsWithQuantityByMealId(long mealId) {
		return (FoodWithQuantityCursor) getReadableDatabase().rawQuery(
				" SELECT " +
						FOODS_TABLE + "." + ID_FIELD + " , " +
						FOODS_TABLE + "." + NAME_FIELD + " , " +
						FOODS_TABLE + "." + DESCRIPTION_FIELD + " , " +
						FOODS_TABLE + "." + IS_USER_DEFINED_FIELD + " , " +
						FOODS_TABLE + "." + FOODS_UNIT_FIELD + " , " +
						FOODS_TABLE + "." + FOODS_CALORIES_FIELD + " , " +
						FOODS_TABLE + "." + FOODS_CARBOHYDRATES_FIELD + " , " +
						FOODS_TABLE + "." + FOODS_FAT_FIELD + " , " +
						FOODS_TABLE + "." + FOODS_PROTEIN_FIELD + " , " +
						FOODS_TABLE + "." + FOODS_IMAGE_FIELD + " , " +
						FOODS_MEALS_TABLE + "." + FOODS_MEALS_QUANTITY_FIELD +
						" FROM " + FOODS_TABLE + " JOIN " +
						FOODS_MEALS_TABLE + " JOIN " + MEALS_TABLE + 				
						" ON " + FOODS_TABLE + "." + ID_FIELD +
						" = " + FOODS_MEALS_TABLE + "." + FOODS_MEALS_FOOD_ID_FIELD +
						" AND " + FOODS_MEALS_TABLE + "." + FOODS_MEALS_MEAL_ID_FIELD +
						" = " + MEALS_TABLE + "." + ID_FIELD + 
						" WHERE " +
						FOODS_MEALS_TABLE + "." + FOODS_MEALS_MEAL_ID_FIELD +
						"=" + String.valueOf(mealId),
						null);				
	}

	public long addFoodToMeal(long mealId, long foodId, double quantity) {
		ContentValues newFoodMeal = new ContentValues();
		newFoodMeal.put(FOODS_MEALS_FOOD_ID_FIELD, foodId);
		newFoodMeal.put(FOODS_MEALS_MEAL_ID_FIELD, mealId);
		newFoodMeal.put(FOODS_MEALS_QUANTITY_FIELD, quantity);
		SQLiteDatabase db = getWritableDatabase();
		long res = db.insert(
				FOODS_MEALS_TABLE,
				null,
				newFoodMeal);
		db.close();
		return res;
	}

}
