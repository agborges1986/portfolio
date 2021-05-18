package com.kaymansoft.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import com.kaymansoft.R;
import com.kaymansoft.model.cursors.CategoryCursor;
import com.kaymansoft.model.cursors.FoodCursor;
import com.kaymansoft.model.cursors.FoodWithQuantityCursor;
import com.kaymansoft.model.cursors.ImageCursor;
import com.kaymansoft.model.cursors.MealCursor;
import com.kaymansoft.model.cursors.MenuCursor;
import com.kaymansoft.model.elements.Category;
import com.kaymansoft.model.elements.Food;
import com.kaymansoft.model.elements.Image;
import com.kaymansoft.model.elements.Meal;
import com.kaymansoft.model.elements.Menu;

import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

public class ModelUtils {
	
	private static HashMap<Long,BitmapDrawable> loadedImages;
	
	//loading images stuff
	public static BitmapDrawable getBitmapDrawable(long imageId, AppDBOpenHelper db) {
		if(imageId == -1)
			return null;
		if(loadedImages == null) {
			loadedImages = new HashMap<Long, BitmapDrawable>();
		}
		if(!loadedImages.containsKey(imageId)) {
			Image image = ((ImageCursor)db.getImageByID(imageId)).getImage();
			if(image.getImage() == null)
				return null;
			InputStream is = new ByteArrayInputStream(image.getImage());		
			BitmapDrawable bmp = new BitmapDrawable(is);
			loadedImages.put(imageId, bmp);
		}
		return loadedImages.get(imageId);
	}

	public static void clearLoadedImages() {
		loadedImages.clear();
		loadedImages = null;
	}
	
	//food stuff
	public static ArrayList<Food> getAllFoods(FoodCursor cursor) {
		ArrayList<Food> foods = new ArrayList<Food>(cursor.getCount());
		int position = cursor.getPosition();
		try {
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
				foods.add(cursor.getFood());
			}
		} catch (RuntimeException e){
			throw e;
		} finally {
			cursor.moveToPosition(position);			
		}
		return foods;	
	}

	public static int updateFood(Food food, AppDBOpenHelper db) {
		return db.updateFood(
				food.getId(),
				food.getName(),
				food.getDescription(),
				food.getUnit(),
				food.getCalories(),
				food.getCarbohydrates(),
				food.getFat(),
				food.getProtein(),
				food.getImageId()
				);		
	}
	
	public static long addFood(Food food, AppDBOpenHelper db) {
		long foodId = db.addFood(
				food.getName(),
				food.getDescription(),
				food.getUnit(), 
				food.getCalories(),
				food.getCarbohydrates(),
				food.getFat(),
				food.getProtein(),
				food.getImageId()
				);
		food.setId(foodId);
		return foodId;
	}

	public static ArrayList<Double> getAllFoodQuantities(FoodWithQuantityCursor cursor) {
		ArrayList<Double> quantities = new ArrayList<Double>(cursor.getCount());
		int position = cursor.getPosition();
		try {
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
				quantities.add(cursor.getQuantity());
			}
		} catch (RuntimeException e){
			throw e;
		} finally {
			cursor.moveToPosition(position);			
		}
		return quantities;
	}
	
	public static ArrayList<Food> getAllFoodsWithQuantity(FoodWithQuantityCursor cursor) {
		ArrayList<Food> foodsWithQuantity = new ArrayList<Food>(cursor.getCount());
		int position = cursor.getPosition();
		try {
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
				foodsWithQuantity.add(cursor.getFood());
			}
		} catch (RuntimeException e){
			throw e;
		} finally {
			cursor.moveToPosition(position);			
		}
		return foodsWithQuantity;
	}
	
	//categories stuff
	public static ArrayList<Category> getAllCategories(CategoryCursor cursor) {
		ArrayList<Category> categories = new ArrayList<Category>(cursor.getCount());
		int position = cursor.getPosition();
		
		while(cursor.moveToNext()) {
			categories.add(cursor.getCategory());
		}
		
		cursor.moveToPosition(position);			
		
		return categories;	
	}
	
	public static int updateCategory(Category category, AppDBOpenHelper db) {
		return db.updateCategory(category.getId(),category.getName(),category.getDescription(), category.getImageId());
	}
	
	public static long addCategory(Category category, AppDBOpenHelper db) {
		long categoryId = db.addCategory(category.getName(), category.getDescription(), category.getImageId());
		category.setId(categoryId);
		return categoryId;
	}
	
	//menus stuff
	
	public static double getMenuTotalCalories(Menu menu,AppDBOpenHelper db) {
		double calories = 0.0;
		for(Food f : ModelUtils.getAllFoods(db.getFoodsWithQuantityByMenuId(menu.getId()))) {
			calories += f.getQuantity() * f.getCalories();
		}
		return calories;
	}

	public static ArrayList<Menu> getAllMenus(MenuCursor cursor) {
		ArrayList<Menu> menus = new ArrayList<Menu>(cursor.getCount());
		int position = cursor.getPosition();
		try {
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
				menus.add(cursor.getMenu());
			}
		} catch (RuntimeException e){
			throw e;
		} finally {
			cursor.moveToPosition(position);			
		}
		return menus;	
			
	}
	
	public static int updateMenu(Menu menu, AppDBOpenHelper db) {
		return db.updateMenu(menu.getId(), menu.getName(), menu.getDescription(), menu.getImageId());
	}
	
	public static long addMenu(Menu menu, AppDBOpenHelper db) {
		long menuId = db.addMenu(menu.getName(),menu.getDescription(), menu.getImageId());
		menu.setId(menuId);
		return menuId;
	}
	
	//meals stuffs
	public static ArrayList<Meal> getAllMeals(MealCursor cursor) {
		ArrayList<Meal> meals = new ArrayList<Meal>(cursor.getCount());
		int position = cursor.getPosition();
		try {
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
				meals.add(cursor.getMeal());
			}
		} catch (RuntimeException e){
			throw e;
		} finally {
			cursor.moveToPosition(position);			
		}
		return meals;	
			
	}

	public static long addMeal(Meal meal, AppDBOpenHelper db) {
		long mealId = db.addMeal(meal.getDescription(), meal.getDate());
		meal.setId(mealId);
		return mealId;
	}
	
	//images Stuff
	public static void findAndSetFoodImage(Food food, ImageView iv, AppDBOpenHelper db) {
		
		long imageId = food.getImageId();
		//si el alimento no tiene imagen, buscar una entre sus categorías		
		if(imageId == -1) {
			CategoryCursor c1;
			try {
				c1 = db.getCategoriesByFoodId(food.getId());
			} catch (NullPointerException e) {
				//TODO: Aclarar este BUG!!!
				//Esto pasa a veces cuando se esta escroleando la lista de alimentos y se da back
				return;
			}
			//las dos lineas de codigo siguientes son para gaantizar que el cursor este en la posicion
			//anterior al final ;)
			c1.moveToFirst();//ponerlo al principio
			c1.moveToPrevious();//ponerlo antes del principio
			while(c1.moveToNext() && imageId==-1)
				imageId = c1.getCategory().getImageId();
			c1.close();
		}
		//si se encontró una imagen		
		if(imageId != -1) {
			//Log.i("IMage ID", String.valueOf(imageId));
			iv.setImageDrawable(getBitmapDrawable(imageId, db));		
		} else { //si no poner una por defecto
			iv.setImageResource(R.drawable.default_food_icon);
		}
//	
//		
//		
//		if(food.getDescription().equals("description")) {
//			@SuppressWarnings("unused")
//			int a = 10;
//			a+=1;
//		}
		
//		if(food.getImageId() != -1) { //verificar si tiene imagen propia
//			ImageCursor c = db.getImageByID(food.getImageId());
//			c.moveToFirst();
//			if(c.getCount()==1)
//				ModelUtils.setImage(iv,c.getImage());
//			c.close();
//		} else { //si no buscar en las imágenes de las categorías
//			if(db==null || food==null) {
//				Log.d(ModelUtils.class.getName(), "Coño!");
//				return;
//			}
//			CategoryCursor c1 = db.getCategoriesByFoodId(food.getId());
//			c1.moveToFirst();
//			if(c1.getCount()>0) {
//				Category category = null;
//				//buscar una categoría con imagen
//				while(!c1.isAfterLast()) {
//					category = c1.getCategory();
//					if(category.getImageId()!=-1)
//						break;
//					c1.moveToNext();
//				}
//				//si se encontró
//				if((category!=null) && (category.getImageId()!=-1)) {
//					//obtener la imagen
//					ImageCursor c2 = db.getImageByID(category.getImageId());
//					c2.moveToFirst();
//					if((c1.getCount()==1) && (c2.getCount()==1)) //verificar la imagen 
//						ModelUtils.setImage(iv,c2.getImage()); //ponerla
//					c2.close();
//				} else {
//					iv.setImageResource(R.drawable.default_food_icon);
//				}
//			} else {
//				iv.setImageResource(R.drawable.default_food_icon);
//			}
//			c1.close();
//		}
	
	}
	
	
	public static void findAndSetMenuImage(Menu menu, ImageView iv, AppDBOpenHelper db) {
		
//		loadAllImages(db);
//		
		long imageId = menu.getImageId();
		
		if(imageId != -1) {
			iv.setImageDrawable(getBitmapDrawable(imageId,db));		
		} else {
			iv.setImageResource(R.drawable.default_menu_icon);
		}
//	
//		
//		
//		if(menu.getImageId() != -1) { //verificar si tiene imagen propia
//			ImageCursor c = db.getImageByID(menu.getImageId());
//			c.moveToFirst();
//			if(c.getCount()==1)
//				ModelUtils.setImage(iv,c.getImage());
//			c.close();
//		} else { 
//			iv.setImageResource(R.drawable.default_menu_icon);			
//		}
	
	}
	
	
	public static void findAndSetCategoryImage(Category category, ImageView iv,	AppDBOpenHelper db) {
		
		long imageId = category.getImageId();
		
		if(imageId != -1) {
			iv.setImageDrawable(getBitmapDrawable(imageId,db));		
		} else {
			iv.setImageResource(R.drawable.default_category_icon);
		}
		
//		if(category.getImageId() != -1) { //verificar si tiene imagen propia
//			ImageCursor c = db.getImageByID(category.getImageId());
//			c.moveToFirst();
//			if(c.getCount()==1)
//				ModelUtils.setImage(iv,c.getImage());
//			c.close();
//		} else { 
//			iv.setImageResource(R.drawable.default_category_icon);			
//		}		
	}
	
}
