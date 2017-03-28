package com.holdyourcolour.instahelper.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.holdyourcolour.instahelper.R;
import com.holdyourcolour.instahelper.database.InstagramSession;

/**
 * Created by HoldYourColour on 27.03.2017.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        InstagramSession session = InstagramSession.getInstance(this);
        if (session.getAccessToken() == null){
            goToLoginActivity();
        } else {
            goToMainActivity();
        }
    }

    private void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void goToLoginActivity(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
