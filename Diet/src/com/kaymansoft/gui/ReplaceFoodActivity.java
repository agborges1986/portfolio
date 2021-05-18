package com.kaymansoft.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kaymansoft.R;
import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.ModelUtils;
import com.kaymansoft.model.elements.Category;
import com.kaymansoft.model.elements.Food;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ReplaceFoodActivity extends Activity {
	
	TextView originalCalories,foodName;
	EditText range;
	ListView categoriesList;
	ImageView inc,dec;
	Button back,replace;
	
	Food food;
	List<Category> all_categories;
	Set<Category> marked_categories;
	InternalAdapter adapter;
	
	private static final int REPLACE_FOOD_CODE = 1017;
	
	public static final String ORIGINAL_FOOD_ID = "com.kaymansoft.diet.food_id";
	public static final String REPLACE_FOOD_ID = "com.kaymansoft.diet.replace_food_id";
	
	OnCheckedChangeListener categoriesListCheckedlistener = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked)
				marked_categories.add((Category)buttonView.getTag());
			else
				marked_categories.remove((Category)buttonView.getTag());
		}
	};
	
	private class InternalAdapter extends BaseAdapter implements ListAdapter {

		public int getCount() {
			return all_categories.size();
		}

		public Object getItem(int position) {
			return all_categories.get(position);
		}

		public long getItemId(int position) {
			return all_categories.get(position).getId();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View categoryView = getLayoutInflater().inflate(R.layout.replace_food_categories_list_item, null);
			CheckBox categoryName = (CheckBox) categoryView.findViewById(R.id.checkBox1);
			Category tag = all_categories.get(position);
			categoryName.setText(tag.getName());
			categoryName.setTag(tag);
			categoryName.setChecked(marked_categories.contains(tag));
			categoryName.setOnCheckedChangeListener(categoriesListCheckedlistener);
			return categoryView;
		}

	}	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.replace_food);
		
		cacheViews();
		initComponents();
		
		handleIntent(getIntent());
		showData();
	}

	private void cacheViews() {
		
		originalCalories = (TextView) findViewById(R.id.textView4);
		foodName = (TextView) findViewById(R.id.textView7);
		range = (EditText) findViewById(R.id.editText1);
		categoriesList = (ListView) findViewById(R.id.listView1);
		inc = (ImageView) findViewById(R.id.imageView2);
		dec = (ImageView) findViewById(R.id.imageView3);
		
		back = (Button) findViewById(R.id.button3);
		replace = (Button) findViewById(R.id.button1);
				
	}

	private void initComponents() {
		
		range.setText("20");
		
		inc.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int value = Integer.parseInt(range.getText().toString());
				if (value==0) { // si el valor es cero
					dec.setEnabled(true); //habilitar decrementacion
				}
				value += 1;
				if(value==200) {//si el nuevo calor es 200
					inc.setEnabled(false);//deshabilitar incrementacion
				}
				range.setText(String.valueOf(value));
			}
		});
		dec.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int value = Integer.parseInt(range.getText().toString());
				if (value==199) { // si el valor es 199
					inc.setEnabled(true); //habilitar incrementacion
				}
				value -= 1;
				if(value==0) {//si el nuevo calor es cero
					dec.setEnabled(false);//deshabilitar decrementacion
				}
				range.setText(String.valueOf(value));
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		replace.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ReplaceFoodActivity.this,FoodListActivity.class);
				double delta = Double.parseDouble(range.getText().toString());
				intent.putExtra(FoodListActivity.MIN_CAL_FILTER, food.getCalories()-delta);
				intent.putExtra(FoodListActivity.MAX_CAL_FILTER, food.getCalories()+delta);
				intent.putExtra(FoodListActivity.CATEGORIES_FILTER, (Serializable)marked_categories);
				startActivityForResult(intent, REPLACE_FOOD_CODE);
			}
		});
		
		all_categories = new ArrayList<Category>();
		adapter = new InternalAdapter();
		categoriesList.setAdapter(adapter);
		categoriesList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	
		categoriesList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				CheckBox categoryName = (CheckBox) view.findViewById(R.id.checkBox1);
				categoryName.toggle();
				if(categoryName.isChecked()) {
					marked_categories.add((Category)categoryName.getTag());
				} else {
					marked_categories.remove((Category)categoryName.getTag());
				}				
			}
		});
		
	}
	
	private void showData() {
		
		adapter.notifyDataSetChanged();
		
		originalCalories.setText(String.valueOf((int)food.getCalories()));
		foodName.setText(food.getName());		
				
	}

	private void handleIntent(Intent intent) {
		long foodId = intent.getLongExtra(ORIGINAL_FOOD_ID, -1);
		if(foodId!=-1) {
			AppDBOpenHelper db = new AppDBOpenHelper(this);
			all_categories.clear();
			all_categories.addAll(ModelUtils.getAllCategories(db.getCategories()));					
			food = db.getFoodById(foodId).getFood();
			marked_categories = new HashSet<Category>();
			marked_categories.addAll(ModelUtils.getAllCategories(db.getCategoriesByFoodId(foodId)));
			db.close();
		} else {
			food = null;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_OK) {
			if(data.getLongExtra(FoodListActivity.SELECTED_FOOD_ID, -1) != -1) {
				getIntent().putExtra(REPLACE_FOOD_ID, data.getLongExtra(FoodListActivity.SELECTED_FOOD_ID, -1));
				setResult(RESULT_OK,getIntent());
				finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
