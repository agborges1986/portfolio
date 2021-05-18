package com.kaymansoft.gui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaymansoft.R;
import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.ModelUtils;
import com.kaymansoft.model.cursors.CategoryCursor;
import com.kaymansoft.model.elements.Category;

public class CategoryListActivity extends ListActivity {

	AppDBOpenHelper				db;
	InternalAdapter				adapter;
	ListView					list;

	private static final int	NEW_CODE				= 1039;
	private static final int	EDIT_CODE				= 1029;

	static final String			SELECTED_CATEGORY_ID	= "com.kaymansoft.diet.category_id";

	private static class CategoryHolder {

		private TextView	name, desc;
		private ImageView	image;

		public CategoryHolder(View view) {
			name = (TextView) view.findViewById(R.id.textView1);
			desc = (TextView) view.findViewById(R.id.textView2);
			image = (ImageView) view.findViewById(R.id.imageView1);
		}

		public void populateFrom(Cursor cursor, AppDBOpenHelper db) {
			Category element = ((CategoryCursor) cursor).getCategory();
			name.setText(element.getName());
			desc.setText(element.getDescription());
			ModelUtils.findAndSetCategoryImage(element, image, db);
		}
	}

	private class InternalAdapter extends CursorAdapter {

		public InternalAdapter(Context context, Cursor c) {
			super(context, c);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View view = LayoutInflater.from(context).inflate(R.layout.category_list_item, null);
			view.setTag(new CategoryHolder(view));
			return view;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			((CategoryHolder) view.getTag()).populateFrom(cursor, db);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.category_list);

		initComponents();

		handleIntent(getIntent());
	}

	public void initComponents() {
		list = getListView();
		registerForContextMenu(list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_editing_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_editing_context_menu, menu);
		menu.setHeaderTitle(R.string.actions_text);

		AdapterContextMenuInfo mi = (AdapterContextMenuInfo) menuInfo;
		AppDBOpenHelper db = new AppDBOpenHelper(this);
		Category cat = db.getCategoryById(mi.id).getCategory();
		MenuItem item = menu.findItem(R.id.item3);
		if (cat.isUserDefined()) {
			item.setEnabled(true);
			item.setVisible(true);
		} else {
			item.setEnabled(false);
			item.setVisible(false);
		}
		db.close();

		super.onCreateContextMenu(menu, v, menuInfo);
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.item1://select
				getIntent().putExtra(SELECTED_CATEGORY_ID, info.id);
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
			case R.id.item1:
				onAddRequest();
				return true;
			case R.id.item2:
				if (getListView().isSelected())
					;
				onDeleteRequest(getListView().getSelectedItemId());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}

	private void onAddRequest() {
		Intent editIntent = new Intent(this, CategoryEditorActivity.class);
		editIntent.putExtra(CategoryEditorActivity.INTENT_TYPE, CategoryEditorActivity.NEW_CATEGORY);
		startActivityForResult(editIntent, NEW_CODE);
	}

	private void onDeleteRequest(long id) {
		if (db.getCategoryById(id).getCategory().isUserDefined()) {
			db.deleteCategory(id);

			Cursor old = adapter.getCursor();

			Cursor c = db.getCategories();
			//startManagingCursor(c);
			adapter.changeCursor(c);

			//stopManagingCursor(old);
			old.close();
		}
	}

	private void onEditRequest(long id) {
		Intent editIntent = new Intent(this, CategoryEditorActivity.class);
		editIntent.putExtra(CategoryEditorActivity.INTENT_TYPE, CategoryEditorActivity.EDIT_CATEGORY);
		editIntent.putExtra(CategoryEditorActivity.CATEGORY_ID, id);
		startActivityForResult(editIntent, EDIT_CODE);
	}

	private void handleIntent(Intent intent) {
		Toast.makeText(this, R.string.long_click_to_select_text, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		db = new AppDBOpenHelper(this);
		Cursor c = db.getCategories();
		//startManagingCursor(c);
		adapter = new InternalAdapter(this, c);

		getListView().setAdapter(adapter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		db.close();
		db = null;
	}

}
