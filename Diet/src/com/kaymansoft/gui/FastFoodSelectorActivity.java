package com.kaymansoft.gui;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaymansoft.R;
import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.ModelUtils;
import com.kaymansoft.model.cursors.CategoryCursor;
import com.kaymansoft.model.elements.Category;

public class FastFoodSelectorActivity extends Activity {

	static final String	SELECTED_FAST_FOOD_ID	= "com.kaymansoft.diet.fast_food_id";

	GridView			images;
	CursorAdapter		adapter;
	AppDBOpenHelper		db;

	private class InternalAdapter extends CursorAdapter {

		public InternalAdapter(Context context, Cursor c) {
			super(context, c, false);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			ImageView iv = (ImageView) view.findViewById(R.id.imageView1);
			Category cat = ((CategoryCursor) cursor).getCategory();
			ModelUtils.findAndSetCategoryImage(cat, iv, db);
			TextView name = (TextView) view.findViewById(R.id.textView1);
			name.setText(cat.getName());
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
			return LayoutInflater.from(context).inflate(R.layout.fast_food_list_item, null);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fast_food_selector);

		cacheViews();
		initComponents();
	}

	private void cacheViews() {
		images = (GridView) findViewById(R.id.gridView1);
	}

	private void initComponents() {

		images.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
				getIntent().putExtra(SELECTED_FAST_FOOD_ID, id);
				setResult(Activity.RESULT_OK, getIntent());
				finish();
			}

		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		db = new AppDBOpenHelper(this);

		CategoryCursor c = db.getFastFoodCategories();
		//startManagingCursor(c);
		adapter = new InternalAdapter(this, c);
		images.setAdapter(adapter);
	}

	@Override
	public void onPause() {
		super.onPause();
		db.close();
		db = null;
	}

}
