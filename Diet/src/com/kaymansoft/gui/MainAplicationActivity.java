package com.kaymansoft.gui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TabHost;

import com.kaymansoft.R;
import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.planning.Planner;
import com.kaymansoft.model.planning.PlanningUtils;
import com.kaymansoft.settings.UserSettings;
import com.kaymansoft.settings.UserSettingsDBOpenHelper;

public class MainAplicationActivity extends TabActivity {

	private TabHost			tabHost;
	private AlertDialog		helpDialog;
	private AlertDialog		hintsDialog;
	private List<String>	hintsToShow;
	private Random			rand;
	private CheckBox		dontHint;
	private boolean			showing_hints	= true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.application_main_tab);

		cacheViews();
		initComponents();

		checkHints();
	}

	private void checkHints() {
		if (showing_hints)
			hintsDialog.show();
	}

	private void cacheViews() {
		tabHost = getTabHost();
	}

	private void initComponents() {
		Resources res = getResources();
		TabHost.TabSpec spec;
		Intent intent;

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, DietInfoActivity.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("diet").setIndicator(res.getString(R.string.diet_text),
				//res.getDrawable(android.R.drawable.ic_menu_info_details))
				res.getDrawable(R.drawable.diet_info_icon_selector))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, UserResumeActivity.class);
		spec = tabHost.newTabSpec("user").setIndicator(res.getString(R.string.user_text),
				res.getDrawable(R.drawable.user_info_icon_selector))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, GeneralDietInfo.class);
		spec = tabHost.newTabSpec("general").setIndicator(res.getString(R.string.general_text),
				res.getDrawable(R.drawable.general_diet_icon_selector))
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);

		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.help_text)
				.setMessage(R.string.help_content_text)
				.setPositiveButton(R.string.accept_text, new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		helpDialog = builder.create();

		rand = new Random(System.currentTimeMillis());

		updateHints();

		showing_hints = !PreferenceManager.getDefaultSharedPreferences(this).getBoolean("hints_check", false);
		String hint = hintsToShow.remove(rand.nextInt(hintsToShow.size()));

		View view = getLayoutInflater().inflate(R.layout.hints, null);

		Builder builder2 = new AlertDialog.Builder(this);
		builder2.setTitle(R.string.hints_text)
				.setMessage(hint)
				.setView(view)
				.setNeutralButton(R.string.next_hint_text, new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (hintsToShow.isEmpty())
							updateHints();
						String hint = hintsToShow.remove(rand.nextInt(hintsToShow.size()));
						AlertDialog alertDialog = (AlertDialog) dialog;
						alertDialog.setMessage(hint);
						dialog.dismiss();
					}
				})
				.setNegativeButton(R.string.close_text, new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						showing_hints = false;
						dialog.cancel();
					}
				});
		hintsDialog = builder2.create();
		hintsDialog.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				checkHints();
			}
		});

		dontHint = (CheckBox) view.findViewById(R.id.checkBox1);

		dontHint.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				showing_hints = !isChecked;
				Editor editor = PreferenceManager.getDefaultSharedPreferences(MainAplicationActivity.this).edit();
				editor.putBoolean("hints_check", isChecked);
				editor.commit();
			}
		});
	}

	private void updateHints() {
		hintsToShow = new ArrayList<String>();
		for (String hint : getResources().getStringArray(R.array.hints_text))
			hintsToShow.add(hint);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_aplication, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.item1://about
				startActivityForResult(new Intent(this, AboutActivity.class), 0);
				return true;
			case R.id.item2://prefs
				startActivityForResult(new Intent(this, PreferencesActivity.class), 0);
				return true;
			case R.id.item4://planning
				startActivityForResult(new Intent(this, PlanificationActivity.class), 0);
				return true;
			case R.id.item3://exit
				finish();
				return true;
			case R.id.item5://help
				helpDialog.show();
				return true;
			case R.id.item6://reset diet
				resetDiet();
				return true;
			case R.id.item7://share
				shareApp();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}

	private void shareApp() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		Bundle extras = new Bundle();
		extras.putString(Intent.EXTRA_TEXT, getString(R.string.share_mesage_text));
		extras.putString(Intent.EXTRA_SUBJECT, getString(R.string.share_subject_text));
		intent.putExtras(extras);
		startActivity(Intent.createChooser(intent, getString(R.string.share_text)));
	}

	private void regenerateMenus(final UserSettings us) {

		Resources res = getResources();
		final ProgressDialog pd = ProgressDialog.show(
				this,
				res.getString(R.string.reset_diet_text),
				res.getString(R.string.deleting_gen_menus_and_plannings_text),
				true,
				false);
		AsyncTask<Void, Integer, Void> task = new AsyncTask<Void, Integer, Void>() {
			private static final int	END_WIPE	= 1;

			@Override
			protected Void doInBackground(Void... params) {
				AppDBOpenHelper db = new AppDBOpenHelper(MainAplicationActivity.this);

				db.wipeOutGeneratedMenus();
				db.wipeOutPlannings();

				publishProgress(END_WIPE);

				PlanningUtils.load(db);

				Resources res = getResources();

				Planner p = new Planner(us, db);
				p.setDefaultMenuDescription(res.getString(R.string.planification_text).toUpperCase());
				p.setMenuNamesPerMeal(
						res.getString(R.string.breakfast_text),
						res.getString(R.string.snack_text),
						res.getString(R.string.lunch_text),
						res.getString(R.string.dinner_text));
				p.generatePlanning();

				db.close();
				return null;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				switch (values[0]) {
					case END_WIPE:
						pd.setMessage(getString(R.string.generating_menus_wait_text));
						break;
					default:
						break;
				}
			}

			@Override
			protected void onPostExecute(Void result) {
				pd.hide();
				tabHost.setCurrentTab(0);
			}
		};
		task.execute((Void) null);

	}

	private void resetDiet() {

		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.reset_diet_text)
				.setMessage(R.string.reset_diet_confirm_text)
				.setPositiveButton(R.string.accept_text, new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						UserSettingsDBOpenHelper usDB = new UserSettingsDBOpenHelper(MainAplicationActivity.this);
						final UserSettings us = usDB.getSettings();
						usDB.close();
						us.setDietStartDay(UserSettingsDBOpenHelper.DATE_FORMATTER.format(Calendar.getInstance().getTime()));
						us.save();
						regenerateMenus(us);
						dialog.dismiss();
					}
				})
				.setNegativeButton(R.string.cancel_text, new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.create().show();

	}

}
