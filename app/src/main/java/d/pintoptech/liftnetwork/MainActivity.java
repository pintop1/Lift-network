package d.pintoptech.liftnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import d.pintoptech.liftnetwork.beneficiary.MainPage;
import d.pintoptech.liftnetwork.prefs.UserInfo;
import d.pintoptech.liftnetwork.prefs.UserSession;
import d.pintoptech.liftnetwork.sponsor.MainPageSponsor;

public class MainActivity extends AppCompatActivity {

    private ImageView LOGO;
    private static int TIME_OUT=5000;
    private UserSession userSession;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LOGO = findViewById(R.id.logo);

        userInfo = new UserInfo(this);
        userSession = new UserSession(this);

        logUser();

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.logo_anim);
        LOGO.startAnimation(myanim);
    }

    private void logUser(){
        String type = userInfo.getKeyType();
        if(userSession.isUserLoggedIn()){
            if(type.equalsIgnoreCase("sponsor")){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(MainActivity.this, MainPageSponsor.class);
                        startActivity(i);
                        finish();
                    }
                },TIME_OUT);
            }else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(MainActivity.this, MainPage.class);
                        startActivity(i);
                        finish();
                    }
                },TIME_OUT);
            }
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(MainActivity.this,AuthActivity.class);
                    startActivity(i);
                    finish();
                }
            },TIME_OUT);
        }
    }
}
