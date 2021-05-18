package com.kaymansoft.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kaymansoft.R;
import com.kaymansoft.ShopReportBuilder;
import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.ModelUtils;
import com.kaymansoft.model.cursors.FoodCursor;
import com.kaymansoft.model.elements.Food;
import com.kaymansoft.model.elements.Menu;
import com.kaymansoft.model.planning.DayPlanning;
import com.kaymansoft.model.planning.DayPlanning.MealType;
import com.kaymansoft.model.planning.PlanningUtils;
import com.kaymansoft.model.planning.PlanningUtils.WeekType;
import com.kaymansoft.model.planning.WeekPlanning;

public class PlanificationActivity extends Activity {

	private static final int	SELECT_MENU_CODE		= 1017;
	protected static final int	EDIT_MENU_CODE			= 1019;
	private int					currentEditing			= -1;					//para la llamada a Select Menu

	private TextView			monthAndYear;
	private Button				weekSelectorActual, weekSelectorNext, weekSelectorDefault;
	private ImageButton			shopReport;

	private TextView			dayNumber[]				= new TextView[7];
	Button						daySelector[]			= new Button[7];

	TextView					menuName[]				= new TextView[6];
	TextView					menuCalories[]			= new TextView[6];
	TextView					menuResume[]			= new TextView[6];
	ImageButton					deletePlanification[]	= new ImageButton[6];
	ImageButton					editPlanification[]		= new ImageButton[6];
	ImageButton					extraPlanification[]	= new ImageButton[6];

	Button						back, save;

	WeekPlanning				week;
	DayPlanning					dayOfWeek;

	OnClickListener				weekSelectorClickListener;
	OnClickListener				daySelectorClickListener;
	OnClickListener				deletePlanificationClickListener;
	OnClickListener				editPlanificationClickListener;
	OnClickListener				extraPlanificationClickListener;

	View						contextMenuView;

	DecimalFormat				formatter				= new DecimalFormat("#0.0");
	private OnClickListener		menuClickListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.planification);

		cacheViews();
		initComponents();
		showData();
	}

	private void cacheViews() {

		monthAndYear = (TextView) findViewById(R.id.textView2);

		weekSelectorActual = (Button) findViewById(R.id.button1);
		weekSelectorNext = (Button) findViewById(R.id.button2);
		weekSelectorDefault = (Button) findViewById(R.id.button3);

		dayNumber[0] = (TextView) findViewById(R.id.textView3);
		dayNumber[1] = (TextView) findViewById(R.id.textView4);
		dayNumber[2] = (TextView) findViewById(R.id.textView5);
		dayNumber[3] = (TextView) findViewById(R.id.textView6);
		dayNumber[4] = (TextView) findViewById(R.id.textView7);
		dayNumber[5] = (TextView) findViewById(R.id.textView8);
		dayNumber[6] = (TextView) findViewById(R.id.textView9);

		daySelector[0] = (Button) findViewById(R.id.button4);
		daySelector[1] = (Button) findViewById(R.id.button5);
		daySelector[2] = (Button) findViewById(R.id.button6);
		daySelector[3] = (Button) findViewById(R.id.button7);
		daySelector[4] = (Button) findViewById(R.id.button8);
		daySelector[5] = (Button) findViewById(R.id.button9);
		daySelector[6] = (Button) findViewById(R.id.button10);

		menuName[0] = (TextView) findViewById(R.id.textView10);
		menuName[1] = (TextView) findViewById(R.id.textView11);
		menuName[2] = (TextView) findViewById(R.id.textView12);
		menuName[3] = (TextView) findViewById(R.id.textView13);
		menuName[4] = (TextView) findViewById(R.id.textView14);
		menuName[5] = (TextView) findViewById(R.id.textView15);

		menuCalories[0] = (TextView) findViewById(R.id.textView22);
		menuCalories[1] = (TextView) findViewById(R.id.textView23);
		menuCalories[2] = (TextView) findViewById(R.id.textView24);
		menuCalories[3] = (TextView) findViewById(R.id.textView25);
		menuCalories[4] = (TextView) findViewById(R.id.textView26);
		menuCalories[5] = (TextView) findViewById(R.id.textView27);

		menuResume[0] = (TextView) findViewById(R.id.textView28);
		menuResume[1] = (TextView) findViewById(R.id.textView29);
		menuResume[2] = (TextView) findViewById(R.id.textView30);
		menuResume[3] = (TextView) findViewById(R.id.textView31);
		menuResume[4] = (TextView) findViewById(R.id.textView32);
		menuResume[5] = (TextView) findViewById(R.id.textView33);

		editPlanification[0] = (ImageButton) findViewById(R.id.imageButton7);
		editPlanification[1] = (ImageButton) findViewById(R.id.imageButton8);
		editPlanification[2] = (ImageButton) findViewById(R.id.imageButton9);
		editPlanification[3] = (ImageButton) findViewById(R.id.imageButton10);
		editPlanification[4] = (ImageButton) findViewById(R.id.imageButton11);
		editPlanification[5] = (ImageButton) findViewById(R.id.imageButton12);

		deletePlanification[0] = (ImageButton) findViewById(R.id.imageButton1);
		deletePlanification[1] = (ImageButton) findViewById(R.id.imageButton2);
		deletePlanification[2] = (ImageButton) findViewById(R.id.imageButton3);
		deletePlanification[3] = (ImageButton) findViewById(R.id.imageButton4);
		deletePlanification[4] = (ImageButton) findViewById(R.id.imageButton5);
		deletePlanification[5] = (ImageButton) findViewById(R.id.imageButton6);

		extraPlanification[0] = (ImageButton) findViewById(R.id.imageButton13);
		extraPlanification[1] = (ImageButton) findViewById(R.id.imageButton14);
		extraPlanification[2] = (ImageButton) findViewById(R.id.imageButton15);
		extraPlanification[3] = (ImageButton) findViewById(R.id.imageButton16);
		extraPlanification[4] = (ImageButton) findViewById(R.id.imageButton17);
		extraPlanification[5] = (ImageButton) findViewById(R.id.imageButton18);

		back = (Button) findViewById(R.id.button12);
		save = (Button) findViewById(R.id.button11);

		shopReport = (ImageButton) findViewById(R.id.imageButton19);

	}

	private void initComponents() {

		AppDBOpenHelper db = new AppDBOpenHelper(this);
		PlanningUtils.load(db);
		db.cleanOldPlannings();
		db.close();

		week = PlanningUtils.getCurrentWeekPlanning();
		dayOfWeek = week.getDayPlanning(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));

		weekSelectorClickListener = new OnClickListener() {

			public void onClick(View v) {
				weekSelectorActual.setEnabled(v != weekSelectorActual);
				weekSelectorNext.setEnabled(v != weekSelectorNext);
				weekSelectorDefault.setEnabled(v != weekSelectorDefault);

				if (v == weekSelectorActual) {
					week = PlanningUtils.getCurrentWeekPlanning();
				} else if (v == weekSelectorNext) {
					week = PlanningUtils.getNextWeekPlanning();
				} else if (v == weekSelectorDefault) {
					week = PlanningUtils.getDefaultWeekPlanning();
				}
				dayOfWeek = week.getDayPlanning(dayOfWeek.getWeekDay());
				//si es la semana actual y el día que pusimos es anterior a hoy
				//mover para hoy
				Calendar today = Calendar.getInstance();
				if (week.getWeekType() == WeekType.Actual && dayOfWeek.getDay().before(today))
					dayOfWeek = week.getDayPlanning(today.get(Calendar.DAY_OF_WEEK));
				showData();
			}
		};
		weekSelectorActual.setOnClickListener(weekSelectorClickListener);
		weekSelectorActual.setEnabled(false);
		weekSelectorNext.setOnClickListener(weekSelectorClickListener);
		weekSelectorNext.setEnabled(true);
		weekSelectorDefault.setOnClickListener(weekSelectorClickListener);
		weekSelectorDefault.setEnabled(true);

		daySelectorClickListener = new OnClickListener() {

			public void onClick(View v) {
				int j = -1;
				for (int i = 0; i < 7; i++) {
					daySelector[i].setEnabled(daySelector[i] != v);
					if (daySelector[i] == v)
						j = i;
				}
				if (j != -1) {
					//domingo es 1 y sabado es 7
					dayOfWeek = week.getDayPlanning(j + 1);
					showData();
				}
			}

		};
		for (int i = 0; i < 7; i++) {
			daySelector[i].setOnClickListener(daySelectorClickListener);
			daySelector[i].setEnabled(i != (dayOfWeek.getWeekDay() - 1));
			//si es la semana en curso
			if (week.getWeekType() == WeekType.Actual) {
				//hacer no clickeable los botones de los días anteriores a hoy
				daySelector[i].setVisibility(i >= (dayOfWeek.getWeekDay() - 1) ? View.VISIBLE : View.INVISIBLE);
			}
		}

		//estos tienen como tag el numero de la planificacion 0-5
		menuClickListener = new OnClickListener() {

			public void onClick(View v) {
				showMenuContent(Integer.valueOf((String) v.getTag()));
			}
		};
		for (TextView menuText : menuName) {
			menuText.setOnClickListener(menuClickListener);
		}
		//estos tienen como tag el numero de la planificacion 0-5
		deletePlanificationClickListener = new OnClickListener() {

			public void onClick(View v) {
				String menu = menuName[Integer.parseInt((String) v.getTag())].getText().toString();
				final int index = Integer.valueOf((String) v.getTag());
				AlertDialog.Builder builder = new AlertDialog.Builder(PlanificationActivity.this);
				Resources resources = getResources();
				builder.setMessage(TextUtils.expandTemplate(
						resources.getString(R.string.delete_planification_confirm_message),
						menu
						))
						.setPositiveButton(R.string.accept_text, new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								deletePlanificationRequest(index);
								dialog.dismiss();
							}
						})
						.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
				builder.create().show();
			}
		};
		for (ImageButton delete : deletePlanification) {
			delete.setEnabled(false);
			delete.setOnClickListener(deletePlanificationClickListener);
		}
		//estos tienen como tag el numero de la planificacion 0-5
		editPlanificationClickListener = new OnClickListener() {

			public void onClick(View v) {
				editPlanificationRequest(Integer.valueOf((String) v.getTag()));
			}
		};
		for (ImageButton edit : editPlanification) {
			edit.setOnClickListener(editPlanificationClickListener);
		}
		//estos tienen como tag el numero de la planificacion 0-5
		extraPlanificationClickListener = new OnClickListener() {

			public void onClick(View v) {
				openContextMenu(v);
			}
		};
		for (ImageButton extra : extraPlanification) {
			registerForContextMenu(extra);
			extra.setOnClickListener(extraPlanificationClickListener);
		}

		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		save.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				saveData();
			}
		});

		shopReport.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				generateShopReport();
			}
		});

	}

	private void generateShopReport() {
		ShopReportBuilder reporter = new ShopReportBuilder(this,
				PlanningUtils.getCurrentWeekPlanning(),
				PlanningUtils.getNextWeekPlanning(),
				PlanningUtils.getDefaultWeekPlanning());
		reporter.collectData();
		AlertDialog.Builder builder = new Builder(this);
		final CharSequence report = TextUtils.expandTemplate(
				getString(R.string.shop_report_message_text),
				reporter.generateCurrentWeekReport(),
				reporter.generateNextWeekReport());
		builder.setTitle(R.string.shop_report_text)
				.setMessage(report)
				.setPositiveButton(R.string.accept_text, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.setNeutralButton(R.string.save_to_file_text, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						saveShopReportToFile(report.toString());
						dialog.dismiss();
					}
				});
		builder.create().show();

	}

	protected void saveShopReportToFile(String report) {
		boolean sdcardPresent = false;
		boolean sdcardWrite = false;

		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			sdcardPresent = sdcardWrite = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			sdcardPresent = true;
			sdcardWrite = false;
		} else {
			sdcardPresent = sdcardWrite = false;
		}
		if (!sdcardPresent) {
			Toast.makeText(this, R.string.sdcard_not_mounted_text, Toast.LENGTH_LONG).show();
		} else if (!sdcardWrite) {
			Toast.makeText(this, R.string.sdcard_read_only_text, Toast.LENGTH_LONG).show();
		} else {
			//SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
			File path = new File(Environment.getExternalStorageDirectory() + "/DietManager");
			File file = new File(path, "Shopping Report.txt");
			OutputStream out = null;
			try {
				path.mkdirs();
				out = new FileOutputStream(file);
				OutputStreamWriter writter = new OutputStreamWriter(out);
				writter.write(getString(R.string.shop_report_text).toUpperCase() + ":\n");
				writter.write(report);
				writter.flush();
				out.close();
				Toast.makeText(this,
						TextUtils.expandTemplate(getString(R.string.shop_report_succesfully_write_text), "SDCard/DietManager/Shopping Report.txt"),
						Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				Toast.makeText(this,
						TextUtils.expandTemplate(getString(R.string.shop_report_cant_write_text), "SDCard/DietManager/Shopping Report.txt"),
						Toast.LENGTH_LONG).show();
				Log.d(PlanificationActivity.class.getName(), "Error al escribir al fichero de reporte, excepción:" + e.getMessage());
			}
		}
	}

	protected void showMenuContent(int i) {
		Menu menu = dayOfWeek.getMenuForMeal(MealType.values()[i]);
		if (menu != null) {
			AppDBOpenHelper db = new AppDBOpenHelper(this);
			FoodCursor c = db.getFoodsWithQuantityByMenuId(menu.getId());
			List<Food> foods = ModelUtils.getAllFoods(c);
			db.close();

			Resources res = getResources();

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(res.getString(R.string.menu_text) + ": " + menu.getName())
					.setCancelable(false)
					.setPositiveButton(R.string.accept_text, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
			StringBuilder foodList = new StringBuilder();
			double calories = 0.0;
			for (Food f : foods) {
				calories += f.getCalories() * f.getQuantity();
				foodList.append(f.getName() + "\n")
						.append("\t" + f.getUnit() + " (x" + formatter.format(f.getQuantity()) + ") ")
						.append(formatter.format(f.getCalories() * f.getQuantity()) + " cal.\n");

			}
			foodList.append("\n" + res.getString(R.string.total_calories_text) + " " + formatter.format(calories));
			builder.setMessage(foodList);
			AlertDialog alert = builder.create();
			alert.show();
		}

	}

	private void deletePlanificationRequest(int i) {
		MealType meal = MealType.values()[i];
		dayOfWeek.setMenuForMeal(meal, null);
		showData();
	}

	private void editPlanificationRequest(int i) {
		currentEditing = i;
		final Menu menu = dayOfWeek.getMenuForMeal(MealType.values()[i]);
		if (menu != null) {//si ya hay un menú planificado
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.edit_planification_question_text)
					.setTitle(R.string.edit_planification_title_text)
					.setPositiveButton(R.string.select_text, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(PlanificationActivity.this, MenuListActivity.class);
							intent.setAction(MenuListActivity.ACTION_SELECT);
							startActivityForResult(intent, SELECT_MENU_CODE);
							dialog.dismiss();
						}
					})
					.setNeutralButton(R.string.edit_text, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(PlanificationActivity.this, MenuEditorActivity.class);
							intent.putExtra(MenuEditorActivity.INTENT_TYPE, MenuEditorActivity.EDIT_MENU);
							intent.putExtra(MenuEditorActivity.MENU_ID, menu.getId());
							startActivityForResult(intent, EDIT_MENU_CODE);
							dialog.dismiss();
						}
					})
					.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							currentEditing = -1;
							dialog.cancel();
						}
					});
			builder.create().show();//mostrar el diálogo
		} else {//si no hay menú planificado, ir directamente a sleccionar uno		
			Intent intent = new Intent(this, MenuListActivity.class);
			intent.setAction(MenuListActivity.ACTION_SELECT);
			startActivityForResult(intent, SELECT_MENU_CODE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case SELECT_MENU_CODE:
					if (currentEditing > -1 && currentEditing < 6) {
						AppDBOpenHelper db = new AppDBOpenHelper(this);
						long menu_id = data.getLongExtra(MenuListActivity.SELECTED_MENU_ID, -1);
						if (menu_id != -1) {
							Menu menu = db.getMenuById(menu_id).getMenu();
							dayOfWeek.setMenuForMeal(MealType.values()[currentEditing], menu);
						}
						db.close();
						showData();
					}
					break;
				case EDIT_MENU_CODE:
				default://por defecto actualizar los datos pues casi cualquier actividad que se llame es para modificar datos
					showData();
					break;
			}
		}
		currentEditing = -1;
	}

	private void showData() {
		//mostrar el mes y el año
		Calendar today = Calendar.getInstance();
		Calendar day = week.getWeekType() != WeekType.Default ? week.getFirstDay() : today;
		String year = String.valueOf(day.get(Calendar.YEAR));
		String month = getResources().getStringArray(R.array.month_text)[day.get(Calendar.MONTH)];
		monthAndYear.setText(month + " " + year);
		//mostrar el número de los días
		for (int i = 0; i < 7; i++, day.roll(Calendar.DAY_OF_YEAR, true)) {
			if (week.getWeekType() != WeekType.Default)
				dayNumber[i].setText(String.valueOf(day.get(Calendar.DAY_OF_MONTH)));
			else
				dayNumber[i].setText("--");
			//hacer los botones de nuevo clickeables
			daySelector[i].setVisibility(View.VISIBLE);
			//si es la semana en curso
			if (week.getWeekType() == WeekType.Actual) {
				//hacer no clickeable los botones de los días anteriores a hoy
				daySelector[i].setVisibility(i >= (today.get(Calendar.DAY_OF_WEEK) - 1) ? View.VISIBLE : View.INVISIBLE);
			}
		}
		//mostrar el nombre de los menús
		for (MealType meal : MealType.values()) {
			Menu menu = dayOfWeek.getMenuForMeal(meal);
			menuName[meal.ordinal()].setText(menu != null ? menu.getName() : "----");
			menuCalories[meal.ordinal()].setText(getMenuCalories(menu));
			menuResume[meal.ordinal()].setText(getMenuResume(menu));
			deletePlanification[meal.ordinal()].setEnabled(menu != null);
			extraPlanification[meal.ordinal()].setEnabled(menu != null);
		}

	}

	private String getMenuResume(Menu menu) {
		int foodCount = 0;
		double unitsCount = 0;
		if (menu != null) {
			AppDBOpenHelper db = new AppDBOpenHelper(this);
			FoodCursor c = db.getFoodsWithQuantityByMenuId(menu.getId());
			foodCount = c.getCount();
			while (c.moveToNext()) {
				Food food = c.getFood();
				unitsCount += food.getQuantity();
			}
			;
			db.close();
		}
		Resources res = getResources();
		String withS = foodCount > 1 ? "s (" : " (";
		return foodCount == 0 ? "" : foodCount + " " + res.getString(R.string.food_text) + withS + formatter.format(unitsCount) + "u)";
	}

	private String getMenuCalories(Menu menu) {
		double calories = -1;
		if (menu != null) {
			AppDBOpenHelper db = new AppDBOpenHelper(this);
			FoodCursor c = db.getFoodsWithQuantityByMenuId(menu.getId());
			while (c.moveToNext()) {
				Food food = c.getFood();
				calories += food.getCalories() * food.getQuantity();
			}
			;
			db.close();
		}
		return calories == -1 ? "-- cal" : formatter.format(calories) + " cal";
	}

	private void saveData() {
		Resources res = getResources();
		final ProgressDialog pd = ProgressDialog.show(
				this,
				res.getString(R.string.saving_planifications_text),
				res.getString(R.string.saving_planifications_wait_text),
				true,
				false);
		AsyncTask<Void, Void, Void> saver = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				AppDBOpenHelper db = new AppDBOpenHelper(PlanificationActivity.this);
				PlanningUtils.save(db);
				db.close();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				pd.hide();
				finish();
			}
		};
		saver.execute((Void) null);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.planification_menu, menu);
		menu.setHeaderTitle(R.string.actions_text);
		contextMenuView = v;

		MenuItem item;
		switch (week.getWeekType()) {
			case Actual:
				item = menu.getItem(0);//on this week
				if (item != null && item.getItemId() == R.id.item1) {
					item.setVisible(false);
				}
				if (menu.size() > 3) {
					item = menu.getItem(3);//apply to this week
					if (item != null && item.getItemId() == R.id.item4) {
						item.setVisible(false);
					}
				}
				break;
			case Next:
				item = menu.getItem(1);//on next week
				if (item != null && item.getItemId() == R.id.item2) {
					item.setVisible(false);
				}
				if (menu.size() > 3) {
					item = menu.getItem(4);//apply to next week
					if (item != null && item.getItemId() == R.id.item5) {
						item.setVisible(false);
					}
				}
				break;
			case Default:
				item = menu.getItem(2);//on default week
				if (item != null && item.getItemId() == R.id.item3) {
					item.setVisible(false);
				}
				if (menu.size() > 3) {
					item = menu.getItem(5);//apply as default
					if (item != null && item.getItemId() == R.id.item6) {
						item.setVisible(false);
					}
				}
				break;
			default:
				break;
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int i = Integer.parseInt((String) contextMenuView.getTag());
		MealType mealType = MealType.values()[i];
		Menu menu = dayOfWeek.getMenuForMeal(mealType);

		if (menu == null)
			return super.onContextItemSelected(item);

		switch (item.getItemId()) {

			case R.id.item4://aplicar a esta semana
				applyToDay(
						PlanningUtils.getCurrentWeekPlanning(),
						dayOfWeek.getDay(),
						mealType,
						menu);
				return true;
			case R.id.item5://aplicar a la próxima semana
				applyToDay(
						PlanningUtils.getNextWeekPlanning(),
						dayOfWeek.getDay(),
						mealType,
						menu);
				return true;
			case R.id.item6://aplicar por defecto
				applyToDay(
						PlanningUtils.getDefaultWeekPlanning(),
						dayOfWeek.getDay(),
						mealType,
						menu);
				return true;

			case R.id.item7://aplicar a toda la semana (actual)
				applyToAllWeek(PlanningUtils.getCurrentWeekPlanning(), mealType, menu);
				return true;
			case R.id.item8://aplicar al fin de semana (actual)
				applyToWeekend(PlanningUtils.getCurrentWeekPlanning(), mealType, menu);
				return true;
			case R.id.item9://aplicar a días laborables (actual)
				applyToWorkingDays(PlanningUtils.getCurrentWeekPlanning(), mealType, menu);
				return true;

			case R.id.item10://aplicar a toda la semana (próxima)
				applyToAllWeek(PlanningUtils.getNextWeekPlanning(), mealType, menu);
				return true;
			case R.id.item11://aplicar al fin de semana (próxima)
				applyToWeekend(PlanningUtils.getNextWeekPlanning(), mealType, menu);
				return true;
			case R.id.item12://aplicar a días laborables (próxima)
				applyToWorkingDays(PlanningUtils.getNextWeekPlanning(), mealType, menu);
				return true;

			case R.id.item13://aplicar a toda la semana (por defecto)
				applyToAllWeek(PlanningUtils.getDefaultWeekPlanning(), mealType, menu);
				return true;
			case R.id.item14://aplicar al fin de semana (por defecto)
				applyToWeekend(PlanningUtils.getDefaultWeekPlanning(), mealType, menu);
				return true;
			case R.id.item15://aplicar a días laborables (por defecto)
				applyToWorkingDays(PlanningUtils.getDefaultWeekPlanning(), mealType, menu);
				return true;

			default:
				return super.onContextItemSelected(item);
		}
	}

	private void applyToDay(WeekPlanning week, Calendar dayOfWeek, MealType mealType, Menu menu) {
		if (week != null && menu != null) {
			week.getDayPlanning(dayOfWeek.get(Calendar.DAY_OF_WEEK)).setMenuForMeal(mealType, menu);
		}
	}

	private void applyToAllWeek(WeekPlanning week, MealType mealType, Menu menu) {
		if (week != null && menu != null) {
			week.getDayPlanning(Calendar.MONDAY).setMenuForMeal(mealType, menu);
			week.getDayPlanning(Calendar.TUESDAY).setMenuForMeal(mealType, menu);
			week.getDayPlanning(Calendar.WEDNESDAY).setMenuForMeal(mealType, menu);
			week.getDayPlanning(Calendar.THURSDAY).setMenuForMeal(mealType, menu);
			week.getDayPlanning(Calendar.FRIDAY).setMenuForMeal(mealType, menu);
			week.getDayPlanning(Calendar.SATURDAY).setMenuForMeal(mealType, menu);
			week.getDayPlanning(Calendar.SUNDAY).setMenuForMeal(mealType, menu);
		}
	}

	private void applyToWeekend(WeekPlanning week, MealType mealType, Menu menu) {
		if (week != null && menu != null) {
			week.getDayPlanning(Calendar.SATURDAY).setMenuForMeal(mealType, menu);
			week.getDayPlanning(Calendar.SUNDAY).setMenuForMeal(mealType, menu);
		}
	}

	private void applyToWorkingDays(WeekPlanning week, MealType mealType, Menu menu) {
		if (week != null && menu != null) {
			week.getDayPlanning(Calendar.MONDAY).setMenuForMeal(mealType, menu);
			week.getDayPlanning(Calendar.TUESDAY).setMenuForMeal(mealType, menu);
			week.getDayPlanning(Calendar.WEDNESDAY).setMenuForMeal(mealType, menu);
			week.getDayPlanning(Calendar.THURSDAY).setMenuForMeal(mealType, menu);
			week.getDayPlanning(Calendar.FRIDAY).setMenuForMeal(mealType, menu);
		}
	}

}
