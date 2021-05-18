package com.kaymansoft.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.kaymansoft.R;
import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.ModelUtils;
import com.kaymansoft.model.cursors.FoodCursor;
import com.kaymansoft.model.cursors.FoodWithQuantityCursor;
import com.kaymansoft.model.cursors.MenuCursor;
import com.kaymansoft.model.elements.Category;
import com.kaymansoft.model.elements.Food;
import com.kaymansoft.model.elements.Menu;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

public class MenuEditorActivity extends Activity {
	
	public static final String MENU_ID = "com.kayansoft.diet.menu_id";
	public static final String INTENT_TYPE = "com.kayansoft.diet.intent_type";
	
	public static final int NEW_MENU = 129;
	public static final int EDIT_MENU = 123;
	
	static final int ADD_FOOD_CODE = 1027;
	static final int CHANGE_IMAGE_CODE = 1037;
	static final int GET_FOOD_GROUP_CODE = 1007;
	static final int GET_FAST_FOOD_CODE = 1097;
	
	private static final int CHANGE_QANTITY_DIALOG = 1129;
	private static final int REPLACE_FOOD_CODE = 1017;
	
	private boolean isNew = false;
	
	EditText menuName, menuDescription;
	ImageButton addFood;
	Button save, back, reload;
	ImageView menuImage;
	TextView totalCalories;
	
	Menu menu;
	
	ArrayList<Food> foods;
	ArrayList<Food> originalFoods; //alimentos originales del menu en la BD (solo para cuando se esta editando)
	InternalAdapter adapter;

	private ListView foodsList;

	private Food clickedFood;
	
	DecimalFormat formatter = new DecimalFormat("#0.0");
	private Bundle data;	
	
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
			((TextView) foodView.findViewById(R.id.textView1)).setText(foods.get(position).getName());
			((TextView) foodView.findViewById(R.id.textView2)).setText(
					foods.get(position).getUnit() + 
					" x" + formatter.format(foods.get(position).getQuantity()) + " u");
			((TextView) foodView.findViewById(R.id.textView3)).setText(
					formatter.format(foods.get(position).getQuantity() * foods.get(position).getCalories()) + " cal.");
			return foodView;
		}

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.menu_editor);
		
		cacheViews();
		initComponents();
		
		handleIntent(getIntent());
		
	}
	
	public void cacheViews() {
		
		menuName		= (EditText) findViewById(R.id.editText1);
		menuDescription = (EditText) findViewById(R.id.editText2);
		foodsList		= (ListView) findViewById(R.id.listView1);
		
		menuImage = (ImageView) findViewById(R.id.imageView1);
		
		addFood = (ImageButton) findViewById(R.id.imageButton1);
		
		save 	= (Button) findViewById(R.id.button1);
		back 	= (Button) findViewById(R.id.button3);
		
		totalCalories = (TextView) findViewById(R.id.textView4);
		
	}
	
	public void initComponents() {
		menu = new Menu();
		
		foods = new ArrayList<Food>();
		adapter = new InternalAdapter();
		
		registerForContextMenu(foodsList);
		foodsList.setAdapter(adapter);
		
		menuImage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeImageRequest();								
			}
		});
		
		addFood.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onAddRequest();
			}
			
		});
		
		save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onSaveRequest();				
			}			
		});
		save.setEnabled(false);
		
		back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();				
			}
		});
				
	}
	
	private void onChangeImageRequest() {
		Intent intent = new Intent(MenuEditorActivity.this, ImageListActivity.class);
		startActivityForResult(intent, CHANGE_IMAGE_CODE);		
	}

	private void onAddRequest() {
		Intent intent = new Intent(MenuEditorActivity.this, FastFoodGenericsSelectorActivity.class);
		startActivityForResult(intent, GET_FOOD_GROUP_CODE);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		handleIntent(intent);
		super.onNewIntent(intent);
	}
	
	private void handleIntent(Intent intent) {
		data = intent.getExtras(); //obtener los datos
		if(data!=null) { //si se recibió algo			
			switch (data.getInt(INTENT_TYPE)) { //verificar el intento
				case NEW_MENU://nuevo menu
					isNew = true;
					menuName.setText(R.string.new_menu_text);
					//reload.setVisibility(View.GONE);
					break;
				case EDIT_MENU: //editar un menu
					isNew = false;
					//reload.setVisibility(View.VISIBLE);
					long menuId = data.getLong(MENU_ID); //obtener el id del menu a editar
					AppDBOpenHelper db = new AppDBOpenHelper(this); 
					MenuCursor c = db.getMenuById(menuId); 
					menu = c.getMenu(); //obtener el menu
					c.close();
					
					FoodWithQuantityCursor c2 = db.getFoodsWithQuantityByMenuId(menuId);
					while(c2.moveToNext()) {
						foods.add(c2.getFood());
					}
					
					originalFoods = new ArrayList<Food>(foods);
					
					c2.close();
					
					db.close();
					showData(); //mostrar los datos
					break;		
			}
		}
		verifyAll();			
	}
	
	private void showImage() {
		long imageId = menu.getImageId();
		if(imageId==-1) {
			menuImage.setImageResource(R.drawable.default_menu_icon);
		} else {
			AppDBOpenHelper db = new AppDBOpenHelper(this);
			ModelUtils.findAndSetMenuImage(menu,menuImage,db);
			db.close();
		}
	}
	
	private void showData() {
		//si se está editando
		if(!isNew) {
			menuName.setText(menu.getName());
			menuDescription.setText(menu.getDescription());
			adapter.notifyDataSetChanged();
			showTotalCalories();
			//ModelUtils.findAndSetFoodImage(food, foodImage, db);			
		}
	}
	
	private void showTotalCalories() {
		double calories = 0.0;
		for(Food f : foods) {
			calories += f.getQuantity() * f.getCalories();
		}		
		totalCalories.setText(formatter.format(calories));		
	}
	
	private void setData() {
		menu.setName(menuName.getText().toString());
		menu.setDescription(menuDescription.getText().toString());				    
	}
	
	private void onSaveRequest() {
		//TODO mejorar el procesamiento de errores
		setData();//enviar los cambios al menu interno
		AppDBOpenHelper db = new AppDBOpenHelper(this);
		if(isNew) { //si es un nuevo menu
			long menuId = ModelUtils.addMenu(menu, db); //agregarlo
			menu.setId(menuId);
			for(Food f : foods) {
				db.addFoodToMenu(menu.getId(), f.getId(), f.getQuantity());
			}
		} else {//si solo se editó
			ModelUtils.updateMenu(menu, db);
			for(Food f : foods) {
				if(isOriginal(f)) { //si ya este menu tenia esta comida
					//actualizar
					db.updateFoodInMenu(menu.getId(), f.getId(), f.getQuantity());
				} else {//si es una comida nueva
					//agregarla
					db.addFoodToMenu(menu.getId(), f.getId(), f.getQuantity());
				}
			}
			//ahora nos quedamos con las comidas originales que fueron eliminadas
			originalFoods.removeAll(foods);
			for(Food f : originalFoods) {//y las eliminamos definitivamente
				db.removeFoodFromMenu(menu.getId(), f.getId());
			}
		}
		db.close();
		setResult(RESULT_OK);
		finish();
	}
	
	private boolean isOriginal(Food food) {
		if(originalFoods==null || originalFoods.isEmpty())
			return false;
		return originalFoods.contains(food);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			AppDBOpenHelper db = new AppDBOpenHelper(this);
			switch(requestCode) {
				case ADD_FOOD_CODE:
					long foodId = data.getLongExtra(FoodListActivity.SELECTED_FOOD_ID, -1);
					if(foodId != -1) {
						FoodCursor c = db.getFoodById(foodId);
						Food newFood = c.getFood();
						c.close();
						addFood(newFood);
						adapter.notifyDataSetChanged();
					}
					break;
				case GET_FOOD_GROUP_CODE:
					int group = data.getIntExtra(FastFoodGenericsSelectorActivity.SELECTED_GROUP, -1);
					
					if(group == FastFoodGenericsSelectorActivity.GENERIC_GROUP) {
					
						ArrayList<Category> justGenerics = new ArrayList<Category>(1);
						Category fake = new Category();
						fake.setId(1);
						justGenerics.add(fake);
						
						Intent intent = new Intent(MenuEditorActivity.this, FoodListActivity.class);
						intent.putExtra(FoodListActivity.CATEGORIES_FILTER, justGenerics);
						startActivityForResult(intent, ADD_FOOD_CODE);					
						
					} else if(group == FastFoodGenericsSelectorActivity.FAST_FOOD_GROUP) {
						
						Intent intent = new Intent(MenuEditorActivity.this, FastFoodSelectorActivity.class);
						startActivityForResult(intent, GET_FAST_FOOD_CODE);
						
					}
					break;
				case GET_FAST_FOOD_CODE:
					
					long catId = data.getLongExtra(FastFoodSelectorActivity.SELECTED_FAST_FOOD_ID, -1);
					
					if(catId != -1) {
						ArrayList<Category> justThisFastFood = new ArrayList<Category>(1);
						Category fake = new Category();
						fake.setId(catId);
						justThisFastFood.add(fake);
						
						Intent intent = new Intent(MenuEditorActivity.this, FoodListActivity.class);
						intent.putExtra(FoodListActivity.CATEGORIES_FILTER, justThisFastFood);
						startActivityForResult(intent, ADD_FOOD_CODE);					
						
					}
					
					break;
				case REPLACE_FOOD_CODE:
					foodId = data.getLongExtra(ReplaceFoodActivity.REPLACE_FOOD_ID, -1);
					if(foodId != -1) {
						FoodCursor c = db.getFoodById(foodId);
						Food newFood = c.getFood();
						c.close();
						replaceFood(newFood);
						adapter.notifyDataSetChanged();
					}
					break;
					
				case CHANGE_IMAGE_CODE:
					long imageId = data.getLongExtra(ImageListActivity.SELECTED_IMAGE_ID, -1);
					menu.setImageId(imageId);
					showImage();
					break;				
			}
			db.close();
			verifyAll();
		}
	}
	
	private void replaceFood(Food newFood) {
		if(foods.contains(newFood)) {
			Toast.makeText(this, R.string.food_already_added_text, Toast.LENGTH_LONG).show();
			return;
		}
		int pos = foods.indexOf(clickedFood);
		if(pos!=-1) {
			foods.remove(pos);
			foods.add(pos,newFood);
		}		
	}

	private void addFood(Food newFood) {
		//verificar que el nuevo alimento no este en la lista ya
		if(foods.contains(newFood)) {
			Toast.makeText(this, R.string.food_already_added_text, Toast.LENGTH_SHORT).show();
			return;
		}
		foods.add(newFood);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CHANGE_QANTITY_DIALOG:
			final View quantityView = LayoutInflater.from(this).inflate(R.layout.chage_quantity_dialog_entry, null);
			final EditText quantityValue = (EditText) quantityView.findViewById(R.id.editText1);
			quantityValue.setText(formatter.format(clickedFood.getQuantity()));
			return new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle(R.string.change_quantity_dialog_title)
			.setView(quantityView)
			.setPositiveButton(R.string.accept_text, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					double value = Double.parseDouble(quantityValue.getText().toString());
					if(value > 0.0)	clickedFood.setQuantity(value);
				}
			})
			.setNegativeButton(R.string.cancel_text,null)
			.create();
		}
		return super.onCreateDialog(id);
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
		case R.id.item1://remove
			foods.remove(info.position);
			adapter.notifyDataSetChanged();
			verifyAll();
			return true;
		case R.id.item2://change quantity
			clickedFood = foods.get(info.position);
			showDialog(CHANGE_QANTITY_DIALOG);
			adapter.notifyDataSetChanged();
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
	
	private boolean verifyAll() {
		boolean res = verifyData();
		save.setEnabled(res);
		return res;
	}

	private boolean verifyData() {
		String name = menuName.getText().toString();
		if(name==null || name.equals("")) {
			return false;
		}
		if(foods.isEmpty()){
			return false;
		}
		return true;
	}
	
}
