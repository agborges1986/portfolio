package com.kaymansoft.gui;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaymansoft.R;
import com.kaymansoft.calories.CaloriesConsumptionUtils;
import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.MCons;
import com.kaymansoft.model.ModelUtils;
import com.kaymansoft.model.cursors.FoodCursor;
import com.kaymansoft.model.cursors.MenuCursor;
import com.kaymansoft.model.elements.Category;
import com.kaymansoft.model.elements.Food;
import com.kaymansoft.model.elements.Meal;
import com.kaymansoft.model.elements.Menu;
import com.kaymansoft.model.planning.PlanningUtils;
import com.kaymansoft.settings.UserSettings;
import com.kaymansoft.settings.UserSettingsDBOpenHelper;

public class TakeMealActivity extends Activity {

	CheckBox							withMenu;
	TextView							menu, totalCalories;
	ListView							foodsList;
	ImageButton							addFood;
	Button								save, cancel;

	Menu								selectedMenu;
	Food								clickedFood;
	ArrayList<Food>						foods;
	Set<Food>							menuFoods;
	InternalAdapter						adapter;

	private static final DecimalFormat	formatter				= new DecimalFormat("0.0");

	//para el diálogo de cambio de cantidad
	View								quantityView;
	EditText							quantityValue;

	static final int					SET_MENU_CODE			= 1029;
	static final int					ADD_FOOD_CODE			= 1027;
	static final int					GET_FOOD_GROUP_CODE		= 1007;
	static final int					GET_FAST_FOOD_CODE		= 1097;
	static final int					REPLACE_FOOD_CODE		= 1017;

	private static final int			CHANGE_QANTITY_DIALOG	= 129;

	private class InternalAdapter extends BaseAdapter implements ListAdapter {

		public int getCount() {
			return foods.size();
		}

		public Object getItem(int position) {
			return foods.get(position);
		}

		public long getItemId(int position) {
			return foods.get(position).getId();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View foodView = getLayoutInflater().inflate(R.layout.take_meal_food_list_item, null);
			final TextView foodName = (TextView) foodView.findViewById(R.id.textView1);
			final Food food = foods.get(position);
			foodName.setText(food.getName());
			final TextView foodUnit = (TextView) foodView.findViewById(R.id.textView2);
			foodUnit.setText(food.getUnit() + " (x " + formatter.format(food.getQuantity()) + " u)");
			final TextView foddTotalColories = (TextView) foodView.findViewById(R.id.textView3);
			foddTotalColories.setText(
					formatter.format(food.getQuantity() * food.getCalories()) + " cal.");
			return foodView;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_meal);

		cacheViews();
		initComponents();

	}

	private void cacheViews() {
		withMenu = (CheckBox) findViewById(R.id.checkBox1);
		menu = (TextView) findViewById(R.id.textView2);
		totalCalories = (TextView) findViewById(R.id.textView4);
		foodsList = (ListView) findViewById(R.id.listView1);
		addFood = (ImageButton) findViewById(R.id.imageButton1);

		save = (Button) findViewById(R.id.button1);
		cancel = (Button) findViewById(R.id.button3);

	}

	private void initComponents() {

		quantityView = LayoutInflater.from(this).inflate(R.layout.chage_quantity_dialog_entry, null);
		quantityValue = (EditText) quantityView.findViewById(R.id.editText1);

		foods = new ArrayList<Food>();
		//		Food f = new Food();
		//		f.setName("Pan");
		//		f.setId(1);
		//		f.setQuantity(2.5);
		//		foods.add(f);
		adapter = new InternalAdapter();

		withMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				menu.setEnabled(withMenu.isChecked());
				if (withMenu.isChecked()) {
					Intent intent = new Intent(TakeMealActivity.this, MenuListActivity.class);
					intent.setAction(MenuListActivity.ACTION_SELECT);
					startActivityForResult(intent, SET_MENU_CODE);
				} else {
					setNoMenu();
				}
			}
		});
		//		() {
		//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		//				menu.setEnabled(isChecked);
		//				if(isChecked) {
		//					Intent intent = new Intent(TakeMealActivity.this, MenuListActivity.class);
		//					startActivityForResult(intent, SET_MENU_CODE);
		//				} else {
		//					setNoMenu();
		//				}
		//			}
		//		});

		menu.setEnabled(false);
		menu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(TakeMealActivity.this, MenuListActivity.class);
				startActivityForResult(intent, SET_MENU_CODE);
			}
		});

		addFood.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(TakeMealActivity.this, FastFoodGenericsSelectorActivity.class);
				startActivityForResult(intent, GET_FOOD_GROUP_CODE);
			}
		});

		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		save.setEnabled(false);
		save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (verifyMeal())
					saveMeal();
			}
		});

		registerForContextMenu(foodsList);
		foodsList.setAdapter(adapter);

		totalCalories.setText("0 cal.");

		getPlanification();

	}

	/**
	 * Obtiene y pone los datos del menú planificado para esta comida en caso de
	 * que haya
	 */
	private void getPlanification() {
		AppDBOpenHelper db = new AppDBOpenHelper(this);

		PlanningUtils.load(db);//primero cargar la planificación

		UserSettingsDBOpenHelper usDB = new UserSettingsDBOpenHelper(this);
		UserSettings us = usDB.getSettings();

		Menu menu = PlanningUtils.getMenuPlanningFor(us, Calendar.getInstance());
		usDB.close();
		if (menu != null) {
			setMenu(db, menu.getId());
		} else {
			setNoMenu();
		}

		db.close();
	}

	/**
	 * Verifica si se ha agregado algun alimento y cambia el estado del botón
	 * guardar
	 * 
	 * @return
	 */
	private boolean verifyAll() {
		if (verifyMeal()) {
			save.setEnabled(true);
			return true;
		} else {
			save.setEnabled(false);
			return false;
		}
	}

	/**
	 * Verifica si el usuario ha agregado algún alimento
	 * 
	 * @return
	 */
	private boolean verifyMeal() {
		if (withMenu.isChecked() && selectedMenu != null) {
			boolean res = true;
			//			AppDBOpenHelper db = new AppDBOpenHelper(this);
			//			FoodCursor c = db.getFoodsWithQuantityByMenuId(selectedMenu.getId());
			//			if(c.getCount() == 0) res = false;
			//			c.close();
			//			db.close();
			return res;
		}
		if (!foods.isEmpty())
			return true;
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		AppDBOpenHelper db = new AppDBOpenHelper(this);
		if (resultCode == RESULT_OK) {
			long foodId;
			switch (requestCode) {
				case ADD_FOOD_CODE:
					foodId = data.getLongExtra(FoodListActivity.SELECTED_FOOD_ID, -1);
					if (foodId != -1) {
						FoodCursor c = db.getFoodById(foodId);
						Food newFood = c.getFood();
						c.close();
						addFood(newFood);
						adapter.notifyDataSetChanged();
					}
					break;
				case GET_FOOD_GROUP_CODE:
					int group = data.getIntExtra(FastFoodGenericsSelectorActivity.SELECTED_GROUP, -1);

					if (group == FastFoodGenericsSelectorActivity.GENERIC_GROUP) {

						ArrayList<Category> justGenerics = new ArrayList<Category>(1);
						Category fake = new Category();
						fake.setId(1);
						justGenerics.add(fake);

						Intent intent = new Intent(TakeMealActivity.this, FoodListActivity.class);
						intent.putExtra(FoodListActivity.CATEGORIES_FILTER, justGenerics);
						startActivityForResult(intent, ADD_FOOD_CODE);

					} else if (group == FastFoodGenericsSelectorActivity.FAST_FOOD_GROUP) {

						Intent intent = new Intent(TakeMealActivity.this, FastFoodSelectorActivity.class);
						startActivityForResult(intent, GET_FAST_FOOD_CODE);

					}
					break;
				case GET_FAST_FOOD_CODE:

					long catId = data.getLongExtra(FastFoodSelectorActivity.SELECTED_FAST_FOOD_ID, -1);

					if (catId != -1) {
						ArrayList<Category> justThisFastFood = new ArrayList<Category>(1);
						Category fake = new Category();
						fake.setId(catId);
						justThisFastFood.add(fake);

						Intent intent = new Intent(TakeMealActivity.this, FoodListActivity.class);
						intent.putExtra(FoodListActivity.CATEGORIES_FILTER, justThisFastFood);
						startActivityForResult(intent, ADD_FOOD_CODE);

					}

					break;
				case SET_MENU_CODE:
					long menuId = data.getLongExtra(MenuListActivity.SELECTED_MENU_ID, -1);
					setMenu(db, menuId);
					break;
				case REPLACE_FOOD_CODE:
					foodId = data.getLongExtra(ReplaceFoodActivity.REPLACE_FOOD_ID, -1);
					if (foodId != -1) {
						FoodCursor c = db.getFoodById(foodId);
						Food newFood = c.getFood();
						c.close();
						replaceFood(newFood);
						adapter.notifyDataSetChanged();
					}
					break;
			}
			verifyAll();
		}
		if (resultCode == RESULT_CANCELED) {
			switch (requestCode) {
				case SET_MENU_CODE:
					withMenu.setChecked(selectedMenu != null);
					menu.setEnabled(selectedMenu != null);
					break;
			}
		}
		db.close();
	}

	private void setMenu(AppDBOpenHelper db, long menuId) {
		if (menuId != -1) {
			MenuCursor c = db.getMenuById(menuId);
			selectedMenu = c.getMenu(); //fijar el menú seleccionado
			c.close();
			addFoodsFromMenu(db, menuId); //adicionar alimentos presentes en el menú
		}
		withMenu.setChecked(menuId != -1);
		menu.setEnabled(menuId != -1);

		showTotalCalories();
	}

	private void showTotalCalories() {
		double calories = 0.0;
		for (Food f : foods) {
			calories += f.getQuantity() * f.getCalories();
		}

		DecimalFormat formatter = new DecimalFormat("#0.0");
		totalCalories.setText(formatter.format(calories));

		UserSettingsDBOpenHelper db = new UserSettingsDBOpenHelper(this);
		double todayCalories = CaloriesConsumptionUtils.getTodayConsumedCalories(this, db.getSettings());
		double recommendedCalories = CaloriesConsumptionUtils.getRecommendedDailyCaloriesConsumption(db.getSettings());
		if (todayCalories > recommendedCalories) {
			Toast.makeText(this, getResources().getString(R.string.exceeded_calories_consumption_text), Toast.LENGTH_LONG).show();
		} else if (calories + todayCalories > recommendedCalories) {
			Toast.makeText(this, getResources().getString(R.string.will_exceed_calories_text), Toast.LENGTH_LONG).show();
		}
		db.close();

	}

	private void replaceFood(Food newFood) {
		if (foods.contains(newFood)) {
			Toast.makeText(this, R.string.food_already_added_text, Toast.LENGTH_LONG).show();
			return;
		}
		int pos = foods.indexOf(clickedFood);
		if (pos != -1) {
			foods.remove(pos);
			foods.add(pos, newFood);
		}
		//como cambiamos la comida del menu ya no estamos usando este menu específicamente
		//withMenu.setChecked(false);
		verifyAll();
		showTotalCalories();
	}

	private void addFoodsFromMenu(AppDBOpenHelper db, long menuId) {
		menu.setText(selectedMenu.getName());
		//obtener los alimnetos del menú
		FoodCursor fc = db.getFoodsWithQuantityByMenuId(menuId);
		//almacenar los alimentos en este conjunto
		menuFoods = new HashSet<Food>(fc.getCount());
		//para cada alimento del menú
		while (fc.moveToNext()) {
			Food food = fc.getFood();
			menuFoods.add(food);//agregarlo al conjunto del menú
			//si no está ya presente en la lista de alimentos
			if (!foods.contains(food)) {
				foods.add(food); //agregarlo a la lista de alimentos
			}
		}
		fc.close();
		adapter.notifyDataSetChanged();
		verifyAll();
		showTotalCalories();
	}

	private void setNoMenu() {
		withMenu.setChecked(false);
		menu.setEnabled(false);
		menu.setText(R.string.none_text);
		menu.setEnabled(false);
		withMenu.setChecked(false);
		if (selectedMenu != null) {//si se había seleccionado previamente un menú
			foods.removeAll(menuFoods);//quitar todos los alimentos del menú anterior
		}
		adapter.notifyDataSetChanged();
		selectedMenu = null;
		verifyAll();
		showTotalCalories();
	}

	private void addFood(Food newFood) {
		if (foods.contains(newFood)) {
			Toast.makeText(this, R.string.food_already_added_text, Toast.LENGTH_LONG).show();
			return;
		}
		foods.add(newFood);
		verifyAll();
		showTotalCalories();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.take_meal_food_list_menu, menu);
		menu.setHeaderTitle(R.string.actions_text);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
			case R.id.item1: //eliminar
				//if(menuFoods==null || !menuFoods.contains(foods.get(info.position))) {
				foods.remove(info.position);
				//} else {
				//	Toast.makeText(this, R.string.food_in_menu_text, Toast.LENGTH_LONG).show();
				//}
				adapter.notifyDataSetChanged();
				verifyAll();
				showTotalCalories();
				return true;
			case R.id.item2: //cambiar cantidad
				clickedFood = foods.get(info.position);
				quantityValue.setText(formatter.format(clickedFood.getQuantity()));
				showDialog(CHANGE_QANTITY_DIALOG);
				adapter.notifyDataSetChanged();
				showTotalCalories();
				return true;
			case R.id.item3: //reemplazar alimento
				clickedFood = foods.get(info.position);
				Intent intent = new Intent(this, ReplaceFoodActivity.class);
				intent.putExtra(ReplaceFoodActivity.ORIGINAL_FOOD_ID, clickedFood.getId());
				startActivityForResult(intent, REPLACE_FOOD_CODE);
				showTotalCalories();
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case CHANGE_QANTITY_DIALOG:
				quantityValue.setText(formatter.format(clickedFood.getQuantity()));
				return new AlertDialog.Builder(this)
						.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle(R.string.change_quantity_dialog_title)
						.setView(quantityView)
						.setPositiveButton(R.string.accept_text, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								double value = Double.parseDouble(quantityValue.getText().toString());
								if (value >= 0.0) {
									clickedFood.setQuantity(value);
									adapter.notifyDataSetChanged();
									showTotalCalories();
								}
							}
						})
						.setNegativeButton(R.string.cancel_text, null)
						.create();
		}
		return super.onCreateDialog(id);
	}

	protected void saveMeal() {
		SimpleDateFormat formatter = MCons.DATETIME_FORMATTER;

		Meal newMeal = new Meal();
		newMeal.setDate(formatter.format(Calendar.getInstance().getTime()));

		AppDBOpenHelper db = new AppDBOpenHelper(this);
		long mealId = ModelUtils.addMeal(newMeal, db);

		//agregar las comidas del menu
		//		if(withMenu.isChecked() && selectedMenu!= null) {
		//			FoodWithQuantityCursor menuFoods = db.getFoodsWithQuantityByMenuId(selectedMenu.getId());
		//			menuFoods.moveToFirst();
		//			Food food = menuFoods.getFood();
		//			while(!menuFoods.isAfterLast()) {
		//				db.addFoodToMeal(mealId, food.getId(), food.getQuantity());
		//				menuFoods.moveToNext();
		//			}
		//		}

		//agregar las comidas de la lista
		for (Food food : foods) {
			db.addFoodToMeal(mealId, food.getId(), food.getQuantity());
		}

		db.close();

		setResult(RESULT_OK);

		//cuando se acepta esta actividad se ocultar la notificación lanzada para el BB
		NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mgr.cancel(AlarmWindowActivity.MEAL_TIME_NOTIFICATION_ID);

		finish();
	}

}
