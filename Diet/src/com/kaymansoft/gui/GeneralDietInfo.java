package com.kaymansoft.gui;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.kaymansoft.R;
import com.kaymansoft.graph.CaloriesConsumedGraph;
import com.kaymansoft.graph.WeigthGraph;
import com.kaymansoft.model.elements.Category;

public class GeneralDietInfo extends Activity {

	Button					foods, menus;
	LinearLayout			inflater;
	ScrollView				generalscroll;
	TextView				titlecalories, titleweigth;
	WeigthGraph				graf;
	CaloriesConsumedGraph	graf2;
	DisplayMetrics			metrics;

	static final int		GET_FOOD_GROUP_CODE	= 1007;
	static final int		GET_FAST_FOOD_CODE	= 1097;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.general_diet_info);

		cacheViews();
		initComponents();

	}

	private void cacheViews() {
		foods = (Button) findViewById(R.id.button2);
		menus = (Button) findViewById(R.id.button1);
		generalscroll = (ScrollView) findViewById(R.id.ScrollView1);
	}

	private void initComponents() {

		foods.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(GeneralDietInfo.this, FastFoodGenericsSelectorActivity.class);
				startActivityForResult(intent, GET_FOOD_GROUP_CODE);
			}
		});

		menus.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(GeneralDietInfo.this, MenuListActivity.class);
				intent.putExtra(MenuListActivity.SHOW_TOAST, false);
				startActivity(intent);
			}
		});

		inflater = new LinearLayout(this);
		inflater.setOrientation(LinearLayout.VERTICAL);

		titlecalories = new TextView(this);

		graf = new WeigthGraph(this);
		graf2 = new CaloriesConsumedGraph(this);

		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		graf.addGraph(this, metrics.widthPixels - 20, metrics.heightPixels * 1 / 2);
		graf2.addGraph(this, metrics.widthPixels - 20, metrics.heightPixels * 1 / 2);

		inflater.addView(graf, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		inflater.addView(graf2, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		generalscroll.addView(inflater);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		graf.resetSeries(this);
		graf2.resetSeries(this);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case GET_FOOD_GROUP_CODE:
					int group = data.getIntExtra(FastFoodGenericsSelectorActivity.SELECTED_GROUP, -1);

					if (group == FastFoodGenericsSelectorActivity.GENERIC_GROUP) {

						ArrayList<Category> justGenerics = new ArrayList<Category>(1);
						Category fake = new Category();
						fake.setId(1);
						justGenerics.add(fake);

						Intent intent = new Intent(GeneralDietInfo.this, FoodListActivity.class);
						intent.putExtra(FoodListActivity.SHOW_TOAST, false);
						intent.putExtra(FoodListActivity.CATEGORIES_FILTER, justGenerics);
						startActivity(intent);

					} else if (group == FastFoodGenericsSelectorActivity.FAST_FOOD_GROUP) {

						Intent intent = new Intent(GeneralDietInfo.this, FastFoodSelectorActivity.class);
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

						Intent intent = new Intent(GeneralDietInfo.this, FoodListActivity.class);
						intent.putExtra(FoodListActivity.SHOW_TOAST, false);
						intent.putExtra(FoodListActivity.CATEGORIES_FILTER, justThisFastFood);
						startActivity(intent);

					}

					break;

			}
		}
	}

}
