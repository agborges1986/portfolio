package com.kaymansoft.gui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.kaymansoft.R;
import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.ModelUtils;
import com.kaymansoft.model.cursors.CategoryCursor;
import com.kaymansoft.model.cursors.FoodCursor;
import com.kaymansoft.model.elements.Category;
import com.kaymansoft.model.elements.Food;

public class FoodEditorActivity extends Activity {

	public static final String	FOOD_ID				= "food_id";
	public static final String	INTENT_TYPE			= "intent_type";

	public static final int		NEW_FOOD			= 129;
	public static final int		EDIT_FOOD			= 123;

	static final int			CHANGE_IMAGE_CODE	= 1037;
	static final int			ADD_CATEGORY_CODE	= 1039;

	private boolean				isNew				= false;

	EditText					foodName, foodDescription, foodUnit, foodCalories, foodFat, foodCarbohydrates, foodProtein;
	ListView					foodCategories;
	ImageView					foodImage;

	private ArrayList<Category>	categories;
	private ArrayList<Category>	originalCategories;

	ArrayAdapter<Category>		adapter;

	private ImageButton			addCategory;

	private Button				reloadButton;
	private Button				saveButton;
	private Button				backButton;

	private Bundle				data;

	private Food				food;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.food_editor);

		cacheViews();
		initComponents();

		handleIntent(getIntent());
	}

	/**
	 * Guarda referencias a las vistas mas usadas
	 */
	private void cacheViews() {
		foodName = (EditText) findViewById(R.id.editText1);
		foodDescription = (EditText) findViewById(R.id.editText2);
		foodUnit = (EditText) findViewById(R.id.editText01);
		foodCalories = (EditText) findViewById(R.id.editText02);
		foodFat = (EditText) findViewById(R.id.editText03);
		foodCarbohydrates = (EditText) findViewById(R.id.editText05);
		foodProtein = (EditText) findViewById(R.id.editText04);
		foodCategories = (ListView) findViewById(R.id.listView1);
		foodImage = (ImageView) findViewById(R.id.imageView1);

		reloadButton = (Button) findViewById(R.id.button2);
		saveButton = (Button) findViewById(R.id.button1);
		backButton = (Button) findViewById(R.id.button3);

		addCategory = (ImageButton) findViewById(R.id.imageButton1);
	}

	/**
	 * Inicializa componentes necesarios de la actividad: 1- El alimento usado
	 * internamente 2- Los listener para los clicks de los botones
	 */
	private void initComponents() {
		food = new Food();

		categories = new ArrayList<Category>();

		adapter = new ArrayAdapter<Category>(
				this,
				R.layout.food_editor_category_list_item,
				categories
				);
		foodCategories.setAdapter(adapter);

		foodImage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeImageRequest();
			}
		});

		registerForContextMenu(foodCategories);

		addCategory.setOnClickListener(new OnClickListener() {
			//TODO pensar esto mejor
			public void onClick(View v) {
				onAddCategoryRequest();
			}
		});

		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onSaveRequest();
			}
		});
		reloadButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onReloadRequest();
			}
		});
		backButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * Agrega una categoría al alimeto que se está editando
	 */
	protected void onAddCategoryRequest() {
		Intent intent = new Intent(FoodEditorActivity.this, CategoryListActivity.class);
		startActivityForResult(intent, ADD_CATEGORY_CODE);
	}

	/**
	 * Resetea los datos mostrados en pantalla con los originales del alimento
	 * que se está editando
	 */
	private void onReloadRequest() {
		showData();
	}

	/**
	 * Cambia la imagen de la comida que se está editando
	 */
	private void onChangeImageRequest() {
		Intent intent = new Intent(FoodEditorActivity.this, ImageListActivity.class);
		startActivityForResult(intent, CHANGE_IMAGE_CODE);
	}

	/**
	 * Guarda los datos definitivamente en la BD, esta operación no puede ser
	 * revertida
	 */
	private void onSaveRequest() {
		//TODO mejorar el procesamiento de errores
		setData();//enviar los cambios al alimento interno
		AppDBOpenHelper db = new AppDBOpenHelper(this);
		if (isNew) { //si es un nuevo alimento
			ModelUtils.addFood(food, db); //agregarlo
			for (Category c : categories) { //agrega esta comida a sus categorías
				db.addFoodToCategory(c.getId(), food.getId());
			}
		} else {//si solo se editó
			ModelUtils.updateFood(food, db);
			//para cada categoría
			for (Category c2 : categories) {
				if (!originalCategories.contains(c2)) { //si esta categoría es nueva
					long res = db.addFoodToCategory(c2.getId(), food.getId());
					if (res == -1)
						Log.d(getClass().getName(), "Error agregando el alimento a la categoría");
				}
			}
			//ahora nos quedamos con las categorías originales que fueron eliminadas 
			originalCategories.removeAll(categories);
			for (Category c : originalCategories) { //y eliminamos esta comida de las categorías respectivas
				db.removeFoodFromCategory(c.getId(), food.getId());
			}

		}
		if (categories.isEmpty()) {//si no se especificaron categorías para este alimento
			db.addFoodToCategory(1, food.getId());//ponerlo en la categoría de los genéricos
			//esto es para que no hayan alimentos sin categorías, como Ariel me comentó ;)
		}
		db.close();
		setResult(RESULT_OK);
		finish();
	}

	/**
	 * Muestra los datos del alimento que se está editando
	 */
	private void showData() {
		//si se está editando
		//if(!isNew) {
		foodName.setText(food.getName());
		foodDescription.setText(food.getDescription());
		foodUnit.setText(food.getUnit());
		foodCalories.setText(String.valueOf(food.getCalories()));
		foodFat.setText(String.valueOf(food.getFat()));
		foodCarbohydrates.setText(String.valueOf(food.getCarbohydrates()));
		foodProtein.setText(String.valueOf(food.getProtein()));
		AppDBOpenHelper db = new AppDBOpenHelper(this);
		ModelUtils.findAndSetFoodImage(food, foodImage, db);
		db.close();
		adapter.notifyDataSetChanged();
		//}	    
	}

	/**
	 * Guarda los datos mostrados en pantalla en el alimento que se está
	 * editando
	 */
	private void setData() {
		food.setName(foodName.getText().toString());
		food.setDescription(foodDescription.getText().toString());
		food.setUnit(foodUnit.getText().toString());
		food.setCalories(Double.parseDouble(foodCalories.getText().toString()));
		food.setFat(Double.parseDouble(foodFat.getText().toString()));
		food.setCarbohydrates(Double.parseDouble(foodCarbohydrates.getText().toString()));
		food.setProtein(Double.parseDouble(foodProtein.getText().toString()));
	}

	/**
	 * Aquí se procesan los datos que se pasaron como parámetros para
	 * determinar: 1- Si se recibió algún dato 2- Si el intento de la actividad
	 * es para editar un alimento o para agregar uno nuevo
	 */
	private void handleIntent(Intent intent) {
		data = intent.getExtras(); //obtener los datos
		if (data != null) { //si se recibió algo
			AppDBOpenHelper db = new AppDBOpenHelper(this);
			switch (data.getInt(INTENT_TYPE)) { //verificar el intento
				case NEW_FOOD://nuevo alimento
					isNew = true;
					reloadButton.setVisibility(View.GONE);

					Resources res = getResources();

					food = new Food();
					food.setCalories(0.0);
					food.setFat(0.0);
					food.setCarbohydrates(0.0);
					food.setProtein(0.0);
					food.setUnit("1 " + res.getString(R.string.unit_text));
					food.setName(res.getString(R.string.new_food_text));
					food.setDescription(res.getString(R.string.desc_text));

					showData();

					break;
				case EDIT_FOOD: //editar un alimento
					isNew = false;
					reloadButton.setVisibility(View.VISIBLE);
					long foodId = data.getLong(FOOD_ID); //obtener el id del alimento a editar

					FoodCursor c = db.getFoodById(foodId);
					food = c.getFood(); //obtener el alimento
					c.close();

					CategoryCursor c1 = db.getCategoriesByFoodId(food.getId());
					categories.clear();
					categories.addAll(ModelUtils.getAllCategories(c1));
					c1.close();

					originalCategories = new ArrayList<Category>(categories);

					showData(); //mostrar los datos

					break;
			}
			db.close();
		}
	}

	/**
	 * Muestra la imagen de sta comida
	 */
	private void showImage() {
		long imageId = food.getImageId();
		if (imageId == -1) {
			foodImage.setImageResource(R.drawable.default_food_icon);
		} else {
			AppDBOpenHelper db = new AppDBOpenHelper(this);
			//Image img = db.getImageByID(imageId).getImage();
			ModelUtils.findAndSetFoodImage(food, foodImage, db);
			db.close();
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.food_editor_category_list_menu, menu);
		menu.setHeaderTitle(R.string.actions_text);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
			case R.id.item1://drop category
				categories.remove(info.position);
				adapter.notifyDataSetChanged();
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		handleIntent(intent);
		super.onNewIntent(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			AppDBOpenHelper db = new AppDBOpenHelper(this);
			switch (requestCode) {
				case ADD_CATEGORY_CODE:
					long categoryId = data.getLongExtra(CategoryListActivity.SELECTED_CATEGORY_ID, -1);
					if (categoryId != -1) {
						Category cat = db.getCategoryById(categoryId).getCategory();
						if (!categories.contains(cat)) {
							categories.add(cat);
							adapter.notifyDataSetChanged();
						} else {
							Toast.makeText(
									this,
									getResources().getString(R.string.category_already_present_text),
									Toast.LENGTH_SHORT)
									.show();
						}
					}
					break;
				case CHANGE_IMAGE_CODE:
					long imageId = data.getLongExtra(ImageListActivity.SELECTED_IMAGE_ID, -1);
					food.setImageId(imageId);
					showImage();
					break;
			}
			db.close();
		}
	}

}
