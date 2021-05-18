package com.kaymansoft.gui;

import java.util.Collection;

import android.app.ExpandableListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaymansoft.R;
import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.ModelUtils;
import com.kaymansoft.model.cursors.FoodCursor;
import com.kaymansoft.model.elements.Category;
import com.kaymansoft.model.elements.Food;

public class FoodListActivity extends ExpandableListActivity {

	public static final String	SELECTED_FOOD_ID	= "com.kaymansoft.diet.food_id";
	public static final String	SHOW_TOAST			= "com.kaymansoft.diet.show_toast";
	public static final String	MIN_CAL_FILTER		= "com.kaymansoft.diet.min_cal_filter";
	public static final String	MAX_CAL_FILTER		= "com.kaymansoft.diet.max_cal_filter";
	public static final String	CATEGORIES_FILTER	= "com.kaymansoft.diet.categories_filter";

	private String				query;															//esto es para las búsquedas con el diálogo

	double						min_cal				= -1.0, max_cal = -1.0;
	Collection<Category>		cat_filter;

	AppDBOpenHelper				db;

	InternalAdapter				adapter;
	boolean						isSingleChoice		= false;

	static final int			NEW_CODE			= 1039;
	static final int			EDIT_CODE			= 1029;

	private static class FoodGroupHolder {

		private TextView	name, description;
		private ImageView	image;

		public FoodGroupHolder(View view) {
			name = (TextView) view.findViewById(R.id.textView1);
			description = (TextView) view.findViewById(R.id.textView2);
			image = (ImageView) view.findViewById(R.id.imageView1);
		}

		public void populateFrom(Cursor cursor, AppDBOpenHelper db) {
			Food element = ((FoodCursor) cursor).getFood();
			name.setText(element.getName());
			if (element.getDescription() == null || element.getDescription().equals(""))
				description.setVisibility(View.GONE);
			else {
				name.setSingleLine(true);
				name.setMarqueeRepeatLimit(-1);
				name.setEllipsize(TruncateAt.MARQUEE);
				description.setVisibility(View.VISIBLE);
				description.setText(element.getDescription());
			}
			ModelUtils.findAndSetFoodImage(element, image, db);
		}

	}

	private static class FoodChildHolder {

		private TextView	fatValue;
		private TextView	carbohydratesValue;
		private TextView	proteinsValue;
		private TextView	caloriesValue;
		private TextView	unitValue;

		public FoodChildHolder(View view) {
			fatValue = (TextView) view.findViewById(R.id.food_expandable_properties_item_fat_value);
			carbohydratesValue = (TextView) view.findViewById(R.id.food_expandable_properties_item_carbohydrates_value);
			proteinsValue = (TextView) view.findViewById(R.id.food_expandable_properties_item_proteins_value);
			caloriesValue = (TextView) view.findViewById(R.id.food_expandable_properties_item_calories_value);
			unitValue = (TextView) view.findViewById(R.id.food_expandable_properties_item_unit_value);
		}

		public void populateFrom(Cursor cursor) {
			Food element = ((FoodCursor) cursor).getFood();
			fatValue.setText(Double.toString(element.getFat()));
			carbohydratesValue.setText(Double.toString(element.getCarbohydrates()));
			proteinsValue.setText(Double.toString(element.getProtein()));
			caloriesValue.setText(Double.toString(element.getCalories()));
			unitValue.setText(element.getUnit());
		}

	}

	private class InternalAdapter extends CursorTreeAdapter {

		Cursor	originalCursor;

		public InternalAdapter(Cursor cursor, Context context) {
			super(cursor, context);
			originalCursor = cursor;
			setFilterQueryProvider(new FilterQueryProvider() {
				public Cursor runQuery(CharSequence constraint) {
					String filter = (String) constraint;
					if ((filter != null) || (min_cal > -0.01) || (max_cal > 0.0) || (cat_filter != null)) {
						//FoodCursor c = db.getFoodsFilteredBy(filter);
						FoodCursor c = db.getFoodsFilteredBy(filter.trim(), min_cal, max_cal, cat_filter);
						//startManagingCursor(c);
						c.moveToFirst();
						return c;
					} else {
						return originalCursor;
					}
				}
			});

		}

		@Override
		protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
			((FoodChildHolder) view.getTag()).populateFrom(cursor);
		}

		@Override
		protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
			if (!isExpanded)
				view.findViewById(R.id.element_expandable_group_wrapper).setBackgroundResource(R.drawable.cc4_round_both);
			else
				view.findViewById(R.id.element_expandable_group_wrapper).setBackgroundResource(R.drawable.cc4_round_top);

			((FoodGroupHolder) view.getTag()).populateFrom(cursor, db);

		}

		protected Cursor getChildrenCursor(Cursor groupCursor) {
			FoodCursor c = db.getFoodById(((FoodCursor) groupCursor).getFood().getId());
			//startManagingCursor(c);
			return c;
		}

		@Override
		protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
			View view = LayoutInflater.from(context).inflate(R.layout.food_expandable_child_item, null);
			view.setTag(new FoodChildHolder(view));
			return view;
		}

		@Override
		protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
			View view = LayoutInflater.from(context).inflate(R.layout.element_expandable_group_item, null);
			view.setTag(new FoodGroupHolder(view));
			if (!isExpanded)
				view.findViewById(R.id.element_expandable_group_wrapper).setBackgroundResource(R.drawable.cc4_round_both);
			else
				view.findViewById(R.id.element_expandable_group_wrapper).setBackgroundResource(R.drawable.cc4_round_top);
			return view;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.element_expandable_list);

		initComponents();

		setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
		handleIntent(getIntent());
	}

	private void initComponents() {

		ExpandableListView list = getExpandableListView();
		registerForContextMenu(list);
		//list.setTextFilterEnabled(true);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		list.setIndicatorBounds(metrics.widthPixels - 80, metrics.widthPixels);
	}

	@Override
	protected void onResume() {
		super.onResume();
		db = new AppDBOpenHelper(this);

		Cursor c = db.getFoods();
		//startManagingCursor(c);
		adapter = new InternalAdapter(c, this);
		adapter.getFilter().filter(query);
		setListAdapter(adapter);
		onSearchRequested();
	}

	@Override
	protected void onPause() {
		db.close();
		db = null;
		super.onPause();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	@SuppressWarnings("unchecked")
	private void handleIntent(Intent intent) {
		if (intent.getBooleanExtra(SHOW_TOAST, true)) {
			Toast.makeText(this, R.string.long_click_to_select_text, Toast.LENGTH_SHORT).show();
		}
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			query = intent.getStringExtra(SearchManager.QUERY);
			//adapter.getFilter().filter(query);
		} else {
			query = "";
			cat_filter = (Collection<Category>) intent.getSerializableExtra(CATEGORIES_FILTER);
			min_cal = intent.getDoubleExtra(MIN_CAL_FILTER, -1.0);
			max_cal = intent.getDoubleExtra(MAX_CAL_FILTER, -1.0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_editing_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		ExpandableListContextMenuInfo mi = (ExpandableListContextMenuInfo) menuInfo;
		if (ExpandableListView.getPackedPositionType(mi.packedPosition) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.list_editing_context_menu, menu);
			menu.setHeaderTitle(R.string.actions_text);

			AppDBOpenHelper db = new AppDBOpenHelper(this);
			Food food = db.getFoodById(mi.id).getFood();
			MenuItem item = menu.findItem(R.id.item3);
			if (food.isUserDefined()) {
				item.setEnabled(true);
				item.setVisible(true);
			} else {
				item.setEnabled(false);
				item.setVisible(false);
			}
			db.close();
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	public boolean onContextItemSelected(MenuItem item) {
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.item1: //select
				getIntent().putExtra(SELECTED_FOOD_ID, info.id);
				setResult(RESULT_OK, getIntent());
				finish();
				return super.onContextItemSelected(item);
			case R.id.item4://edit
				onEditRequest(info.id);
				return true;
			case R.id.item3://delete
				onDeleteRequest(info.id);
				return true;
			default:
				return super.onContextItemSelected(item);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.item1://add
				onAddRequest();
				return true;
			case R.id.item2://delete all
				if (getExpandableListView().isSelected())
					;
				onDeleteRequest(getExpandableListView().getSelectedId());
				return true;
			case R.id.item3://search
				onSearchRequested();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onGroupExpand(int groupPosition) {
		//getExpandableListView().getChildAt(groupPosition).setBackgroundResource(R.drawable.cc4_round_top);
		super.onGroupExpand(groupPosition);
	}

	@Override
	public void onGroupCollapse(int groupPosition) {
		//getExpandableListView().getChildAt(groupPosition).setBackgroundResource(R.drawable.cc4_round_both);
		super.onGroupCollapse(groupPosition);
	}

	private void onAddRequest() {
		Intent editIntent = new Intent(this, FoodEditorActivity.class);
		editIntent.putExtra(FoodEditorActivity.INTENT_TYPE, FoodEditorActivity.NEW_FOOD);
		//startActivityForResult(editIntent, NEW_CODE);		
		startActivity(editIntent);
	}

	private void onDeleteRequest(long id) {
		if (db.getFoodById(id).getFood().isUserDefined()) {
			db.deleteFood(id);

			Cursor old = adapter.getCursor();

			Cursor c = db.getFoods();
			//startManagingCursor(c);
			adapter.changeCursor(c);

			//stopManagingCursor(old);
			old.close();
		}
	}

	private void onEditRequest(long id) {
		Intent editIntent = new Intent(this, FoodEditorActivity.class);
		editIntent.putExtra(FoodEditorActivity.INTENT_TYPE, FoodEditorActivity.EDIT_FOOD);
		editIntent.putExtra(FoodEditorActivity.FOOD_ID, id);
		//startActivityForResult(editIntent, EDIT_CODE);		
		startActivity(editIntent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			//TODO no se si debería hacer algo?
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}