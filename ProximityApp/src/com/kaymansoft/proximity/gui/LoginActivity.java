package com.kaymansoft.proximity.gui;

import com.kaymansoft.proximity.R;
import com.kaymansoft.proximity.R.layout;
import com.kaymansoft.proximity.R.menu;
import com.kaymansoft.proximity.client.Reportable;
import com.kaymansoft.proximity.model.UserData;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	EditText userName, userPass;
	Button accept, cancel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        findViews();
        
		accept.setOnClickListener(new AcceptListener());
		cancel.setOnClickListener(new CancelListener());
    }
    
    private void findViews() {
        userName = (EditText)findViewById(R.id.editText1);
        userPass = (EditText)findViewById(R.id.editText2);       
        
        accept = (Button)findViewById(R.id.button2);
        cancel = (Button)findViewById(R.id.button1);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }
    
	private class AcceptListener implements View.OnClickListener {
		public void onClick(View view) {
			String uName = userName.getText().toString();
			String uPass = userPass.getText().toString();
			
			CS.client.login(uName, uPass, new Reportable<UserData>() {
				
				public void setRemainingReportsNumber(int cant) {
					// do nothing
				}
				
				public void report(UserData report, String errorMessage) {
					if(report == null){
						Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
						return;
					}else{
						CS.sessionId = report.sessionId;
						Toast.makeText(LoginActivity.this, "sessionId = " + CS.sessionId, Toast.LENGTH_LONG).show();
						Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
						startActivity(intent);
						finish();
					}
				}
			});
		}
	}
	
	private class CancelListener implements View.OnClickListener{

		public void onClick(View v) {
			Intent intent = new Intent(LoginActivity.this, SelectUserActivity.class);
			startActivity(intent);		
			finish();
		}
		
	}
}