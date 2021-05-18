package com.kaymansoft.proximity.gui;

import com.kaymansoft.proximity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

/**
 * Activity que muestra las opciones:
 * <li>Nuevo usuario
 * <li>Usuario habitual
 * <li>Usuario de facebook
 * <p>Para escoger que tipo de usuario se desea.
 * 
 * @author Denis
 *
 */

public class SelectUserActivity extends Activity  {
    Button newUser, habitualUser, facebookUser;
    Animation buttonClickAnimation;
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        newUser = (Button)findViewById(R.id.new_user);
        habitualUser = (Button)findViewById(R.id.habitual_user);
        facebookUser = (Button)findViewById(R.id.facebook_user);

        newUser.setOnClickListener(new NewUserOnClickListener());
        habitualUser.setOnClickListener(new HabitualUserOnClickListener());
        facebookUser.setOnClickListener(new FacebookUserOnClickListener());

        buttonClickAnimation = AnimationUtils.loadAnimation(SelectUserActivity.this, R.animator.button_click_animation);
        
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        newUser.setBackgroundResource(R.drawable.a_new_user);
        habitualUser.setBackgroundResource(R.drawable.a_habitual_user);
    }

    private class NewUserOnClickListener implements View.OnClickListener{
        public void onClick(View view) {
            newUser.setBackgroundResource(R.drawable.a_new_user_pressed);
            newUser.startAnimation(buttonClickAnimation);

            startActivity(new Intent(SelectUserActivity.this, NewUserActivity.class));
        }
    }
    private class HabitualUserOnClickListener implements View.OnClickListener{
        public void onClick(View view) {
            habitualUser.setBackgroundResource(R.drawable.a_habitual_user_pressed);
            habitualUser.startAnimation(buttonClickAnimation);
            
            startActivity(new Intent(SelectUserActivity.this, LoginActivity.class));  
        }
    }
    private class FacebookUserOnClickListener implements View.OnClickListener{
        public void onClick(View view) {

        }
    }

}