package com.holdyourcolour.instahelper.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.holdyourcolour.instahelper.R;
import com.holdyourcolour.instahelper.network.InstagramAPI;
import com.holdyourcolour.instahelper.view.dialogs.InstagramDialog;

/**
 * Created by HoldYourColour on 27.03.2017.
 */

public class LoginActivity extends AppCompatActivity{

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInstaAuthDialog();
            }
        });
    }

    private void showInstaAuthDialog() {

        InstagramDialog.OAuthDialogListener listener = new InstagramDialog.OAuthDialogListener() {
            @Override
            public void onComplete(String code) {
                Log.d(TAG, "OAuthDialogListener onComplete code = " + code);
                InstagramAPI.getAccessToken(LoginActivity.this, code);
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "Authorization failed " + error);
            }
        };
        InstagramDialog mAuthDialog = new InstagramDialog(this, InstagramAPI.AUTH_URL, listener);
        mAuthDialog.show();
    }


    public void goToMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
