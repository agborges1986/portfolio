package com.kaymansoft.gui;

import java.text.DecimalFormat;

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
import com.kaymansoft.model.cursors.FoodWithQuantityCursor;
import com.kaymansoft.model.cursors.MenuCursor;
import com.kaymansoft.model.elements.Food;
import com.kaymansoft.model.elements.ModelElement;

public class MenuListActivity extends ExpandableListActivity {
	private AppDBOpenHelper				db;
	private InternalAdapter				adapter;

	private boolean						selecting			= false;

	private String						query;														//esto es para las búsquedas con el diálogo

	static final String					SELECTED_MENU_ID	= "com.kaymansoft.diet.menu_id";
	public static final String			SHOW_TOAST			= "com.kaymansoft.diet.show_toast";

	public static final String			ACTION_SELECT		= "com.kaymansoft.diet.select_menu";

	private static final int			NEW_CODE			= 1039;
	private static final int			EDIT_CODE			= 1029;

	private static final DecimalFormat	formatter			= new DecimalFormat("#0.0");

	private static class MenuGroupHolder {

		private TextView	name, description, cals;
		private ImageView	image;

		public MenuGroupHolder(View view) {
			name = (TextView) view.findViewById(R.id.textView1);
			description = (TextView) view.findViewById(R.id.textView2);
			cals = (TextView) view.findViewById(R.id.textView3);
			image = (ImageView) view.findViewById(R.id.imageView1);
		}

		public void populateFrom(Cursor cursor, AppDBOpenHelper db) {
			com.kaymansoft.model.elements.Menu element = ((MenuCursor) cursor).getMenu();
			name.setText(element.getName());
			if (element.getDescription() == null || element.getDescription().equals(""))
				description.setVisibility(View.INVISIBLE);
			else {
				name.setSingleLine(true);
				name.setMarqueeRepeatLimit(-1);
				name.setEllipsize(TruncateAt.MARQUEE);
				description.setVisibility(View.VISIBLE);
				description.setText(element.getDescription());
			}
			cals.setText(formatter.format(ModelUtils.getMenuTotalCalories(element, db)) + " cal.");
			ModelUtils.findAndSetMenuImage(element, image, db);
		}
	}

	private static class MenuChildHolder {

		private TextView	foodName, quantity;

		public MenuChildHolder(View view) {
			foodName = (TextView) view.findViewById(R.id.textView1);
			quantity = (TextView) view.findViewById(R.id.textView2);
		}

		public void populateFrom(Cursor cursor) {
			Food element = ((FoodWithQuantityCursor) cursor).getFood();
			foodName.setText(element.getName());
			quantity.setText(
					element.getUnit() +
							" (x" + formatter.format(element.getQuantity()) + " u) " +
							formatter.format(element.getCalories() * element.getQuantity()) + " cal.");
		}
	}

	private class InternalAdapter extends CursorTreeAdapter {

		public InternalAdapter(Cursor cursor, Context context) {
			super(cursor, context);
			setFilterQueryProvider(new FilterQueryProvider() {
				public Cursor runQuery(CharSequence constraint) {
					String filter = (String) constraint;
					if (filter == null || filter.trim().equals(""))
						return getCursor();
					else {
						MenuCursor c = db.getMenusFilteredBy(filter);
						//startManagingCursor(c);
						return c;
					}
				}
			});
		}

		@Override
		protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
			((MenuChildHolder) view.getTag()).populateFrom(cursor);
			if (isLastChild)
				view.setBackgroundResource(R.drawable.cc4_round_bottom);
			else
				view.setBackgroundResource(R.drawable.cc4_round_none);
		}

		@Override
		protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
			if (!isExpanded)
				view.findViewById(R.id.element_expandable_group_wrapper).setBackgroundResource(R.drawable.cc4_round_both);
			else
				view.findViewById(R.id.element_expandable_group_wrapper).setBackgroundResource(R.drawable.cc4_round_top);

			((MenuGroupHolder) view.getTag()).populateFrom(cursor, db);
		}

		@Override
		protected Cursor getChildrenCursor(Cursor groupCursor) {
			FoodWithQuantityCursor c = db.getFoodsWithQuantityByMenuId(((MenuCursor) groupCursor).getMenu().getId());
			//startManagingCursor(c);
			return c;
		}

		@Override
		protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
			View view = LayoutInflater.from(context).inflate(R.layout.menu_expandable_child_item, null);
			view.setTag(new MenuChildHolder(view));
			return view;
		}

		@Override
		protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
			View view = LayoutInflater.from(context).inflate(R.layout.menu_expandable_group_item, null);
			view.setTag(new MenuGroupHolder(view));
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
		registerForContextMenu(getExpandableListView());

		//getExpandableListView().setTextFilterEnabled(true);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		getExpandableListView().setIndicatorBounds(metrics.widthPixels - 80, metrics.widthPixels);

		setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
		handleIntent(getIntent());

	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		selecting = ACTION_SELECT.equals(intent.getAction());
		if (intent.getBooleanExtra(SHOW_TOAST, true) && selecting)
			Toast.makeText(this, R.string.long_click_to_select_text, Toast.LENGTH_SHORT).show();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			query = intent.getStringExtra(SearchManager.QUERY);
		} else {
			query = "";
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_editing_menu, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		ExpandableListContextMenuInfo mi = (ExpandableListContextMenuInfo) menuInfo;
		if (ExpandableListView.getPackedPositionType(mi.packedPosition) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.list_editing_context_menu, menu);
			menu.setHeaderTitle(R.string.actions_text);

			AppDBOpenHelper db = new AppDBOpenHelper(this);
			ModelElement men = db.getMenuById(mi.id).getMenu();
			MenuItem item = menu.findItem(R.id.item3);//item eliminar
			if (men.isUserDefined() && !selecting) {
				item.setEnabled(true);
				item.setVisible(true);
			} else {
				item.setVisible(false);
				item.setEnabled(false);
			}
			db.close();
		}

		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	protected void onResume() {
		super.onResume();
		db = new AppDBOpenHelper(this);
		Cursor c = db.getMenus();
		//startManagingCursor(c);
		adapter = new InternalAdapter(c, this);
		setListAdapter(adapter);
		adapter.getFilter().filter(query);
		onSearchRequested();
	}

	@Override
	protected void onPause() {
		super.onPause();
		db.close();
		db = null;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.item1: //select
				getIntent().putExtra(SELECTED_MENU_ID, info.id);
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

	private void onAddRequest() {
		Intent editIntent = new Intent(this, MenuEditorActivity.class);
		editIntent.putExtra(MenuEditorActivity.INTENT_TYPE, MenuEditorActivity.NEW_MENU);
		startActivityForResult(editIntent, NEW_CODE);
	}

	private void onDeleteRequest(long id) {
		if (db.getMenuById(id).getMenu().isUserDefined()) {
			db.deleteMenu(id);
			Cursor old = adapter.getCursor();

			Cursor c = db.getMenus();
			//startManagingCursor(c);
			adapter.changeCursor(c);

			//stopManagingCursor(old);
			old.close();
		}
	}

	private void onEditRequest(long id) {
		Intent editIntent = new Intent(this, MenuEditorActivity.class);
		editIntent.putExtra(MenuEditorActivity.INTENT_TYPE, MenuEditorActivity.EDIT_MENU);
		editIntent.putExtra(MenuEditorActivity.MENU_ID, id);
		startActivityForResult(editIntent, EDIT_CODE);
	}

}
