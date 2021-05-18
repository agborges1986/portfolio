package com.kaymansoft.proximity.gui;

import java.util.LinkedList;
import java.util.List;

import com.kaymansoft.proximity.R;
import com.kaymansoft.proximity.adapters.CategoryAdapter;
import com.kaymansoft.proximity.client.Reportable;
import com.kaymansoft.proximity.model.CategoryDesc;
import com.kaymansoft.proximity.model.UserData;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Activity que permite la creación de un nuevo usuario.
 * @author Denis
 *
 */
public class NewUserActivity extends Activity {
	GridView gridView = null;
	CategoryAdapter adapter = null;
	Button accept, cancel;
	EditText userName, nickName, userPass;
	ImageView imageView = null;
	List<Integer> userCategories = new LinkedList<Integer>();
	int index;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_new_user);

		findViews();
				
		gridView.setOnItemClickListener(new CellClickListener());
		adapter = new CategoryAdapter(this);
		gridView.setAdapter(adapter);
		
		imageView.setOnClickListener(new ImageViewOnClickListener());

		accept.setOnClickListener(new AcceptListener());
		cancel.setOnClickListener(new CancelListener());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
        index = getIntent().getIntExtra("index", -1);
        if(index != -1){
        	imageView.setImageResource(CS.tiles[index]);
        	imageView.refreshDrawableState();
        }
        else{
        	index = 0;
        }
	}

	
	private void findViews() {
		accept = (Button) findViewById(R.id.accept);
		cancel = (Button) findViewById(R.id.cancel);
		
		gridView = (GridView) findViewById(R.id.gridView);
		imageView = (ImageView)findViewById(R.id.imageView1);
		
		userName = (EditText) findViewById(R.id.userNameEditText);
		nickName = (EditText) findViewById(R.id.nickNameEditText);
		userPass = (EditText) findViewById(R.id.userPassEditText);
	}
	
	private class AcceptListener implements View.OnClickListener {
		public void onClick(View view) {
			String uName = userName.getText().toString();
			String uNick = nickName.getText().toString();
			String uPass = userPass.getText().toString();

			int []uCategories = new int[userCategories.size()];
			for (int i = 0; i < uCategories.length; i++) {
				uCategories[i] = userCategories.get(i);
			}
			
			
			CS.client.register(uName, 2, uPass, uNick, uCategories, null, new Reportable<UserData>() {
				
				public void setRemainingReportsNumber(int cant) {
					// do nothing
				}
				
				public void report(UserData report, String errorMessage) {
					if(report == null){
						Toast.makeText(NewUserActivity.this, errorMessage, Toast.LENGTH_LONG).show();
						return;
					}else{
						CS.sessionId = report.sessionId;
						CS.tile = index;
						Toast.makeText(NewUserActivity.this, "sessionId = " + CS.sessionId, Toast.LENGTH_LONG).show();
						startActivity(new Intent(NewUserActivity.this, HomeActivity.class));
					}
				}
			});
		}
	}
	
	private class CancelListener implements View.OnClickListener{
		public void onClick(View v) {
			Intent intent = new Intent(NewUserActivity.this, SelectUserActivity.class);
			startActivity(intent);			
		}
		
	}
	
	private class ImageViewOnClickListener implements View.OnClickListener{
		public void onClick(View v) {
			Intent intent = new Intent(NewUserActivity.this, SelectAvatarActivity.class);
			startActivity(intent);			
		}
		
	}
	
	private class CellClickListener implements OnItemClickListener{

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			int index = arg2;
			Toast.makeText(NewUserActivity.this, "index = " + index, Toast.LENGTH_LONG).show();
			if(!userCategories.contains(Integer.valueOf(index))){
				userCategories.add(Integer.valueOf(index));
				CS.categories[index].setDescription("Marcado");
				arg1.setBackgroundColor(Color.YELLOW);
			}
			else{
				userCategories.remove(Integer.valueOf(index));
				CS.categories[index].setDescription("");
				arg1.setBackgroundColor(Color.WHITE);
			}

		}
		
	}
}

